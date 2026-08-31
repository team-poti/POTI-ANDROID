package com.poti.android.presentation.user.editprofile

import com.poti.android.MainDispatcherRule
import com.poti.android.R
import com.poti.android.core.common.state.ApiState
import com.poti.android.domain.model.delivery.DeliveryInfo
import com.poti.android.domain.model.user.HistorySummary
import com.poti.android.domain.model.user.UserAccount
import com.poti.android.domain.model.user.UserMyPage
import com.poti.android.domain.model.user.UserProfile
import com.poti.android.domain.repository.FileUploadRepository
import com.poti.android.domain.repository.ImageRepository
import com.poti.android.domain.repository.UserRepository
import com.poti.android.domain.usecase.image.UploadImagesUseCase
import com.poti.android.domain.usecase.user.CheckNicknameDuplicationUseCase
import com.poti.android.domain.usecase.user.EditProfileUseCase
import com.poti.android.domain.usecase.user.GetUserMyPageUseCase
import com.poti.android.presentation.onboarding.model.ErrorText
import com.poti.android.presentation.user.editprofile.model.EditProfileUiIntent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verifyNoInteractions

@OptIn(ExperimentalCoroutinesApi::class)
class EditProfileViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var userRepository: FakeUserRepository
    private lateinit var imageRepository: ImageRepository
    private lateinit var fileUploadRepository: FileUploadRepository
    private lateinit var viewModel: EditProfileViewModel

    @Before
    fun setUp() {
        userRepository = FakeUserRepository()
        imageRepository = mock(ImageRepository::class.java)
        fileUploadRepository = mock(FileUploadRepository::class.java)
        viewModel = EditProfileViewModel(
            getUserMyPageUseCase = GetUserMyPageUseCase(userRepository),
            checkNicknameDuplicationUseCase = CheckNicknameDuplicationUseCase(userRepository),
            uploadImagesUseCase = UploadImagesUseCase(imageRepository, fileUploadRepository),
            editProfileUseCase = EditProfileUseCase(userRepository),
        )
    }

    @Test
    fun `ignores an older nickname result that completes after the latest result`() =
        runTest(mainDispatcherRule.testDispatcher) {
            advanceUntilIdle()
            val olderResult = userRepository.prepareNicknameResult("이전닉네임")
            val latestResult = userRepository.prepareNicknameResult("최신닉네임")

            viewModel.processIntent(EditProfileUiIntent.OnNicknameChange("이전닉네임"))
            runCurrent()
            viewModel.processIntent(EditProfileUiIntent.OnNicknameChange("최신닉네임"))
            runCurrent()

            latestResult.complete(Result.success(true))
            runCurrent()
            olderResult.complete(Result.success(false))
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals("최신닉네임", state.nickname)
            assertFalse(state.isNicknameValid)
            assertEquals(
                R.string.onboarding_nickname_error_duplicate,
                (state.nicknameError as ErrorText.StringResource).resId,
            )
        }

    @Test
    fun `patches profile only once when save intent is sent consecutively`() =
        runTest(mainDispatcherRule.testDispatcher) {
            advanceUntilIdle()
            makeNicknameValid("새닉네임")
            val patchResult = CompletableDeferred<Result<Unit>>()
            userRepository.patchProfileResult = patchResult

            viewModel.processIntent(EditProfileUiIntent.OnSaveClick)
            viewModel.processIntent(EditProfileUiIntent.OnSaveClick)
            runCurrent()

            assertEquals(1, userRepository.patchProfileCallCount)
            assertEquals(ApiState.Loading, viewModel.uiState.value.saveState)

            patchResult.complete(Result.success(Unit))
            advanceUntilIdle()

            assertEquals(1, userRepository.patchProfileCallCount)
            assertEquals(ApiState.Success(Unit), viewModel.uiState.value.saveState)
            assertEquals("새닉네임", userRepository.lastPatchedNickname)
            assertEquals(PROFILE_IMAGE_URL, userRepository.lastPatchedProfileImageUrl)
            verifyNoInteractions(imageRepository, fileUploadRepository)
        }

    @Test
    fun `allows save retry after profile patch fails`() =
        runTest(mainDispatcherRule.testDispatcher) {
            advanceUntilIdle()
            makeNicknameValid("재시도닉네임")
            userRepository.patchResults.add(Result.failure(IllegalStateException("patch failed")))
            userRepository.patchResults.add(Result.success(Unit))

            viewModel.processIntent(EditProfileUiIntent.OnSaveClick)
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.saveState is ApiState.Failure)

            viewModel.processIntent(EditProfileUiIntent.OnSaveClick)
            advanceUntilIdle()

            assertEquals(2, userRepository.patchProfileCallCount)
            assertEquals(ApiState.Success(Unit), viewModel.uiState.value.saveState)
        }

    private suspend fun TestScope.makeNicknameValid(nickname: String) {
        userRepository.prepareNicknameResult(nickname).complete(Result.success(false))
        viewModel.processIntent(EditProfileUiIntent.OnNicknameChange(nickname))
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.isNicknameValid)
    }

    private class FakeUserRepository : UserRepository {
        private val nicknameResults = mutableMapOf<String, CompletableDeferred<Result<Boolean>>>()

        val patchResults = ArrayDeque<Result<Unit>>()
        var patchProfileResult: CompletableDeferred<Result<Unit>>? = null
        var patchProfileCallCount = 0
            private set
        var lastPatchedNickname: String? = null
            private set
        var lastPatchedProfileImageUrl: String? = null
            private set

        fun prepareNicknameResult(nickname: String): CompletableDeferred<Result<Boolean>> =
            CompletableDeferred<Result<Boolean>>().also { nicknameResults[nickname] = it }

        override suspend fun postNicknameDuplicate(nickname: String): Result<Boolean> {
            val result = nicknameResults.getValue(nickname)
            return try {
                result.await()
            } catch (cancellation: CancellationException) {
                withContext(NonCancellable) { result.await() }
            }
        }

        override suspend fun getUserMyPage(): Result<UserMyPage> = Result.success(USER_MY_PAGE)

        override suspend fun patchProfile(
            nickname: String,
            profileImageUrl: String,
        ): Result<Unit> {
            patchProfileCallCount += 1
            lastPatchedNickname = nickname
            lastPatchedProfileImageUrl = profileImageUrl
            return patchProfileResult?.await() ?: patchResults.removeFirstOrNull() ?: Result.success(Unit)
        }

        override suspend fun patchOnboarding(
            nickname: String,
            favoriteArtistId: Long?,
        ): Result<Unit> = error("Not used")

        override suspend fun patchFavoriteArtist(artistId: Long): Result<Unit> = error("Not used")

        override suspend fun getUserProfile(userId: Long): Result<UserProfile> = error("Not used")

        override suspend fun getUserAccount(): Result<UserAccount> = error("Not used")

        override suspend fun getMyAddress(): Result<DeliveryInfo?> = error("Not used")

        override suspend fun saveMyAddress(deliveryInfo: DeliveryInfo): Result<Unit> = error("Not used")
    }

    private companion object {
        const val PROFILE_IMAGE_URL = "https://cdn.poti.kr/profiles/profile.jpg"

        val USER_MY_PAGE = UserMyPage(
            nickname = "기존닉네임",
            email = "poti@example.com",
            profileImageUrl = PROFILE_IMAGE_URL,
            ratingAvg = "4.8",
            activityMessage = "최근 3일 이내 활동",
            joinedAt = "2026-07-28",
            hasFavoriteArtist = true,
            favoriteArtistName = "IVE",
            participationSummary = HistorySummary(inProgress = 1, completed = 2),
            recruitSummary = HistorySummary(inProgress = 3, completed = 4),
        )
    }
}

package com.poti.android.data.remote.datasource

import com.poti.android.core.network.model.BaseResponse
import com.poti.android.data.remote.dto.request.user.EditProfileRequestDto
import com.poti.android.data.remote.dto.request.user.FavoriteArtistRequestDto
import com.poti.android.data.remote.dto.request.user.MyAddressRequestDto
import com.poti.android.data.remote.dto.request.user.NicknameDuplicateRequestDto
import com.poti.android.data.remote.dto.request.user.OnboardingRequestDto
import com.poti.android.data.remote.dto.response.user.AccountResponseDto
import com.poti.android.data.remote.dto.response.user.MyAddressResponseDto
import com.poti.android.data.remote.dto.response.user.MyPageResponseDto
import com.poti.android.data.remote.dto.response.user.NicknameDuplicateResponseDto
import com.poti.android.data.remote.dto.response.user.OnboardingResponseDto
import com.poti.android.data.remote.dto.response.user.ProfileResponseDto
import com.poti.android.data.remote.service.UserService
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userService: UserService,
) {
    suspend fun patchOnboarding(onboardingRequest: OnboardingRequestDto): BaseResponse<OnboardingResponseDto> =
        userService.patchOnboarding(onboardingRequest = onboardingRequest)

    suspend fun patchFavoriteArtist(favoriteArtistRequest: FavoriteArtistRequestDto): BaseResponse<Unit> =
        userService.patchFavoriteArtist(favoriteArtistRequest = favoriteArtistRequest)

    suspend fun postNicknameDuplicate(nicknameDuplicateRequest: NicknameDuplicateRequestDto): BaseResponse<NicknameDuplicateResponseDto> =
        userService.postNicknameDuplicate(nicknameDuplicateRequest = nicknameDuplicateRequest)

    suspend fun getUserMyPage(): BaseResponse<MyPageResponseDto> =
        userService.getUserMyPage()

    suspend fun getUserProfile(userId: Long): BaseResponse<ProfileResponseDto> =
        userService.getUserProfile(userId)

    suspend fun getUserAccount(): BaseResponse<AccountResponseDto> =
        userService.getUserAccount()

    suspend fun getMyAddress(): BaseResponse<MyAddressResponseDto> =
        userService.getMyAddress()

    suspend fun patchMyAddress(myAddressRequest: MyAddressRequestDto): BaseResponse<Unit> =
        userService.patchMyAddress(myAddressRequest = myAddressRequest)

    suspend fun patchProfile(editProfileRequest: EditProfileRequestDto): BaseResponse<Unit> =
        userService.patchProfile(editProfileRequest = editProfileRequest)
}

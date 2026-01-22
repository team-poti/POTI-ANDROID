package com.poti.android.domain.repository

import com.poti.android.domain.model.user.UserMyPage
import com.poti.android.domain.model.user.UserProfile

interface UserRepository {
    suspend fun patchOnboarding(
        nickname: String,
        favoriteArtistId: Long?,
    ): Result<Unit>

    suspend fun postNicknameDuplicate(
        nickname: String,
    ): Result<Boolean>

    suspend fun getUserMyPage(): Result<UserMyPage>

    suspend fun getUserProfile(
        userId: Long,
    ): Result<UserProfile>
}

package com.poti.android.domain.repository

interface UserRepository {
    suspend fun patchOnboarding(
        nickname: String,
        favoriteArtistId: Long,
    ): Result<Unit>

    suspend fun postNicknameDuplicate(
        nickname: String,
    ): Result<Boolean>
}

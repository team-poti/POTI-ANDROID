package com.poti.android.domain.repository

import com.poti.android.domain.model.delivery.DeliveryInfo
import com.poti.android.domain.model.user.UserAccount
import com.poti.android.domain.model.user.UserMyPage
import com.poti.android.domain.model.user.UserProfile

interface UserRepository {
    suspend fun patchOnboarding(
        nickname: String,
        favoriteArtistId: Long?,
    ): Result<Unit>

    suspend fun patchFavoriteArtist(
        artistId: Long,
    ): Result<Unit>

    suspend fun postNicknameDuplicate(
        nickname: String,
    ): Result<Boolean>

    suspend fun getUserMyPage(): Result<UserMyPage>

    suspend fun getUserProfile(
        userId: Long,
    ): Result<UserProfile>

    suspend fun getUserAccount(): Result<UserAccount>

    suspend fun getMyAddress(): Result<DeliveryInfo?>

    suspend fun saveMyAddress(
        deliveryInfo: DeliveryInfo,
    ): Result<Unit>

    suspend fun patchProfile(
        nickname: String,
        profileImageUrl: String,
    ): Result<Unit>
}

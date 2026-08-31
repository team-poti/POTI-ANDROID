package com.poti.android.data.local.datasource

import android.content.Context
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.poti.android.data.auth.KakaoAccountManager
import com.poti.android.di.ApplicationScope
import com.poti.android.di.IoDispatcher
import com.poti.android.domain.model.auth.SocialType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class WithdrawalLocalDataCleaner @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:ApplicationScope private val applicationScope: CoroutineScope,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val kakaoAccountManager: KakaoAccountManager,
) {
    private val cacheCleanupJobLock = Any()
    private var cacheCleanupJob: Job? = null

    suspend fun unlinkKakaoAccount(socialType: SocialType?) {
        if (socialType != SocialType.KAKAO) return

        clearSafely("Failed to unlink Kakao account") {
            kakaoAccountManager.unlink()
        }
    }

    fun clearCachesInBackground() {
        val newJob = synchronized(cacheCleanupJobLock) {
            val previousJob = cacheCleanupJob
            applicationScope.launch(start = CoroutineStart.LAZY) {
                previousJob?.join()
                clearSafely("Failed to clear Coil caches") {
                    clearCoilCaches()
                }
                clearSafely("Failed to clear app cache files") {
                    clearAppCacheFiles()
                }
            }.also { job ->
                cacheCleanupJob = job
            }
        }

        newJob.invokeOnCompletion {
            synchronized(cacheCleanupJobLock) {
                if (cacheCleanupJob === newJob) cacheCleanupJob = null
            }
        }
        newJob.start()
    }

    suspend fun awaitCacheCleanup() {
        synchronized(cacheCleanupJobLock) { cacheCleanupJob }?.join()
    }

    @OptIn(ExperimentalCoilApi::class)
    private suspend fun clearCoilCaches() = withContext(ioDispatcher) {
        context.imageLoader.memoryCache?.clear()
        context.imageLoader.diskCache?.clear()
    }

    private suspend fun clearAppCacheFiles() = withContext(ioDispatcher) {
        context.cacheDir.listFiles()?.forEach { file ->
            if (!file.deleteRecursively()) {
                Timber.w("Failed to delete cache file: %s", file.name)
            }
        }
    }

    private suspend fun clearSafely(
        errorMessage: String,
        block: suspend () -> Unit,
    ) {
        runCatching { block() }
            .onFailure { error -> Timber.e(error, errorMessage) }
    }
}

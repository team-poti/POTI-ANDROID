package com.poti.android.core.permission

import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.poti.android.core.permission.datasource.PermissionDataSource
import com.poti.android.domain.usecase.auth.IsGuestUseCase
import com.poti.android.domain.usecase.notification.GetNotificationSettingUseCase
import com.poti.android.domain.usecase.notification.UpdateNotificationSettingUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val isGuestUseCase: IsGuestUseCase,
    private val getNotificationSettingUseCase: GetNotificationSettingUseCase,
    private val updateNotificationSettingUseCase: UpdateNotificationSettingUseCase,
    private val permissionDataSource: PermissionDataSource,
) {
    private var isServerNotificationEnabled = false
    private var hasPendingServerEnable = false

    fun isSystemNotificationGranted(): Boolean =
        NotificationManagerCompat.from(context).areNotificationsEnabled()

    suspend fun shouldShowPermissionModal(): Boolean {
        if (isGuestUseCase()) return false

        hasPendingServerEnable = false

        val setting = getNotificationSettingUseCase().getOrNull() ?: return false
        isServerNotificationEnabled = setting.isTradeEnabled || setting.isEventEnabled

        return !isSystemNotificationGranted() || !isServerNotificationEnabled
    }

    suspend fun requestNotificationPermission(shouldShowRationale: Boolean): PermissionRequestRoute {
        hasPendingServerEnable = !isServerNotificationEnabled

        if (isSystemNotificationGranted()) {
            enableAllNotifications()
            return PermissionRequestRoute.AlreadyGranted
        }

        if (!canShowSystemPermissionDialog(shouldShowRationale)) {
            return PermissionRequestRoute.SystemSetting
        }

        permissionDataSource.markSystemPermissionDialogShown()

        return PermissionRequestRoute.SystemDialog
    }

    suspend fun syncSystemPermissionToServer() {
        if (!isSystemNotificationGranted() || !hasPendingServerEnable) return

        enableAllNotifications()
    }

    private suspend fun canShowSystemPermissionDialog(shouldShowRationale: Boolean): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return false

        return !permissionDataSource.hasShownSystemPermissionDialog() || shouldShowRationale
    }

    private suspend fun enableAllNotifications() {
        updateNotificationSettingUseCase(
            isTradeEnabled = true,
            isEventEnabled = true,
        ).onSuccess {
            isServerNotificationEnabled = true
            hasPendingServerEnable = false
        }
    }
}

sealed interface PermissionRequestRoute {
    data object SystemDialog : PermissionRequestRoute

    data object SystemSetting : PermissionRequestRoute

    data object AlreadyGranted : PermissionRequestRoute
}

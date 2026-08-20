package com.poti.android.core.share

import android.content.Context
import android.content.Intent
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import timber.log.Timber

object KakaoShareLauncher {
    const val LINK_HOST = "kakaolink"
    const val PARAM_DEEP_LINK = "deepLink"

    fun share(
        context: Context,
        title: String,
        description: String,
        imageUrl: String,
        deepLink: String,
    ) {
        val template = FeedTemplate(
            content = Content(
                title = title,
                description = description,
                imageUrl = imageUrl,
                link = Link(
                    webUrl = deepLink,
                    mobileWebUrl = deepLink,
                    androidExecutionParams = mapOf(PARAM_DEEP_LINK to deepLink),
                ),
            ),
        )

        if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
            ShareClient.instance.shareDefault(context, template) { sharingResult, error ->
                if (sharingResult != null) {
                    context.startActivity(sharingResult.intent)
                } else {
                    Timber.e(error, "카카오톡 공유 실패")
                }
            }
        } else {
            shareWithWebSharer(context, template)
        }
    }

    private fun shareWithWebSharer(
        context: Context,
        template: FeedTemplate,
    ) {
        runCatching {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, WebSharerClient.instance.makeDefaultUrl(template)),
            )
        }.onFailure { Timber.e(it, "카카오 웹 공유 실패") }
    }
}

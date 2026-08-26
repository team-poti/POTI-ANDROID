package com.poti.android.core.share

import android.content.Context
import android.content.Intent
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import timber.log.Timber
import java.net.URLEncoder

object KakaoShareLauncher {
    const val LINK_HOST = "kakaolink"
    const val PARAM_DEEP_LINK = "deepLink"

    private const val PARTY_DETAIL_TEMPLATE_ID = 136577L

    private const val ARG_IMAGE = "image"
    private const val ARG_ARTIST = "artist"
    private const val ARG_TITLE = "title"
    private const val ARG_DESCRIPTION = "description"
    private const val ARG_PARTICIPANT_COUNT = "participant_count"
    private const val ARG_TOTAL_COUNT = "total_count"
    private const val ARG_HOST = "host"
    private const val ARG_POT_ID = "pot_id"
    private const val ARG_DEEP_LINK = "deep_link"

    fun sharePartyDetail(
        context: Context,
        artist: String,
        title: String,
        description: String,
        imageUrl: String,
        participantCount: Int,
        totalCount: Int,
        host: String,
        partyId: Long,
        deepLink: String,
    ) {
        val templateArgs = mapOf(
            ARG_IMAGE to imageUrl,
            ARG_ARTIST to artist,
            ARG_TITLE to title,
            ARG_DESCRIPTION to description,
            ARG_PARTICIPANT_COUNT to participantCount.toString(),
            ARG_TOTAL_COUNT to totalCount.toString(),
            ARG_HOST to host,
            ARG_POT_ID to partyId.toString(),
            ARG_DEEP_LINK to URLEncoder.encode(deepLink, Charsets.UTF_8.name()),
        )

        if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
            ShareClient.instance.shareCustom(context, PARTY_DETAIL_TEMPLATE_ID, templateArgs) { sharingResult, error ->
                if (sharingResult != null) {
                    context.startActivity(sharingResult.intent)
                } else {
                    Timber.e(error, "카카오톡 공유 실패")
                }
            }
        } else {
            shareWithWebSharer(context, templateArgs)
        }
    }

    private fun shareWithWebSharer(
        context: Context,
        templateArgs: Map<String, String>,
    ) {
        runCatching {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    WebSharerClient.instance.makeCustomUrl(PARTY_DETAIL_TEMPLATE_ID, templateArgs),
                ),
            )
        }.onFailure { Timber.e(it, "카카오 웹 공유 실패") }
    }
}

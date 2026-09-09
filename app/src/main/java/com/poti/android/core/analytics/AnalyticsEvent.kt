package com.poti.android.core.analytics

object AnalyticsEvent {
    const val APP_OPENED = "App Opened"
    const val ONBOARDING_COMPLETED = "Onboarding Completed"
    const val HOME_VIEWED = "Home Viewed"
    const val SPLIT_CARD_CLICKED = "Split Card Clicked"
    const val SEARCH_PERFORMED = "Search Performed"
    const val SEARCH_RESULT_CLICKED = "Search Result Clicked"
    const val SPLIT_DETAIL_VIEWED = "Split Detail Viewed"
    const val JOIN_BUTTON_CLICKED = "Join Button Clicked"
    const val PARTICIPANT_INFO_SUBMITTED = "Participant Info Submitted"
}

object AnalyticsEventProperty {
    const val ENTRY_POINT = "entry_point"
    const val IS_FIRST_OPEN = "is_first_open"
    const val FAVORITE_GROUP_ID = "favorite_group_id"
    const val SKIPPED = "skipped"
    const val CONTENT_TYPE = "content_type"
    const val SPLIT_ID = "split_id"
    const val GROUP_ID = "group_id"
    const val GOODS_ID = "goods_id"
    const val SECTION = "section"
    const val POSITION = "position"
    const val KEYWORD = "keyword"
    const val RESULT_COUNT = "result_count"
    const val SEARCH_TYPE = "search_type"
    const val RESULT_TYPE = "result_type"
    const val RESULT_ID = "result_id"
    const val SPLIT_STATUS = "split_status"
    const val SOURCE = "source"
}

object AnalyticsValue {
    const val DIRECT = "direct"
    const val NOTIFICATION = "notification"
    const val DEEP_LINK = "deep_link"
    const val FAVORITE_GROUP = "favorite_group"
    const val ALL = "all"
    const val GOODS = "goods"
    const val HOME = "home"
    const val SEARCH = "search"
    const val POPULAR = "popular"
    const val LATEST = "latest"
    const val CLOSING_SOON = "closing_soon"
}

fun goodsIdentifier(
    groupId: Long,
    goodsTitle: String,
): String = "$groupId:$goodsTitle"

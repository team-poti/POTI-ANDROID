package com.poti.android.core.analytics

object AnalyticsEvent {
    const val APP_OPENED = "App Opened"
    const val ONBOARDING_COMPLETED = "Onboarding Completed"
    const val HOME_VIEWED = "Home Viewed"
    const val HOME_SECTION_MORE_CLICKED = "Home Section More Clicked"
    const val GOODS_CARD_CLICKED = "Goods Card Clicked"
    const val SPLIT_CARD_CLICKED = "Split Card Clicked"
    const val SEARCH_PERFORMED = "Search Performed"
    const val SEARCH_RESULT_CLICKED = "Search Result Clicked"
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
    const val HOME_SECTION = "home_section"
    const val SORT_TYPE = "sort_type"
    const val POSITION = "position"
    const val KEYWORD = "keyword"
    const val RESULT_COUNT = "result_count"
    const val RESULT_TYPE = "result_type"
    const val RESULT_ID = "result_id"
    const val SOURCE = "source"
}

object AnalyticsValue {
    const val DIRECT = "direct"
    const val NOTIFICATION = "notification"
    const val DEEP_LINK = "deep_link"
    const val FAVORITE_GROUP = "favorite_group"
    const val ALL = "all"
    const val GOODS = "goods"
    const val RECOMMENDED = "recommended"
    const val DISCOVER = "discover"
    const val HOME = "home"
    const val HOME_SECTION_MORE = "home_section_more"
    const val LATEST = "latest"
    const val DEADLINE = "deadline"
    const val RATING = "rating"
}

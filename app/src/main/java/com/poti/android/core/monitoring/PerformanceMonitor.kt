package com.poti.android.core.monitoring

interface PerformanceMonitor {
    suspend fun <T> traceResult(
        name: String,
        attributes: Map<String, String> = emptyMap(),
        block: suspend () -> Result<T>,
    ): Result<T>
}

object PerformanceTraceName {
    const val HOME_LOAD = "home_load"
    const val SEARCH_LOAD = "search_load"
    const val SPLIT_LIST_LOAD = "split_list_load"
    const val SPLIT_DETAIL_LOAD = "split_detail_load"
    const val JOIN_SUBMIT = "join_submit"
}

object PerformanceAttribute {
    const val SOURCE = "source"
    const val LOAD_TYPE = "load_type"
    const val SORT_TYPE = "sort_type"
}

object PerformanceAttributeValue {
    const val INITIAL = "initial"
    const val NEXT = "next"
}

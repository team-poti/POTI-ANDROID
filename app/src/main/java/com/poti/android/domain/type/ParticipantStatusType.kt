package com.poti.android.domain.type

enum class ParticipantStatusType {
    RECRUITING, // 모집 중
    WAIT_PAY, // 입금 대기
    WAIT_PAY_CHECK, // 입금 확인 대기
    PAID, // 입금 완료
    READY, // 배송 대기
    SHIPPED, // 배송 시작
    DELIVERED, // 배송 완료
}

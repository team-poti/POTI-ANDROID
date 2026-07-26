package com.poti.android.presentation.party.create.model

data class DeliveryOptionUiModel(
    val deliveryId: Long,
    val name: String,
    val price: Int,
    val isSelected: Boolean,
)

package me.teenageorge.order.domain

import java.math.BigDecimal

data class OrderRequest(
    val consumerId: Int,
    val price: BigDecimal
)
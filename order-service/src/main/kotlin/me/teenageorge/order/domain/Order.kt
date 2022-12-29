package me.teenageorge.order.domain

import java.math.BigDecimal

data class Order(
    val id: Int,
    val consumerId: Int,
    val price: BigDecimal
    )
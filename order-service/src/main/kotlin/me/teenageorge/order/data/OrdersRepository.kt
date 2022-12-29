package me.teenageorge.order.data

import me.teenageorge.order.domain.Order
import me.teenageorge.order.domain.OrderRequest
import me.teenageorge.tables.Orders.ORDERS
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class OrdersRepository(
    private val dslContext: DSLContext,
    private val mapper: OrdersRecordMapper
) {

    fun create(orderRequest: OrderRequest): Mono<Order> {
        return Mono.from(
            insert.values(values(orderRequest))
                .returningResult(columns)
        )
            .map { mapper.map(it) }
    }

    fun get() =
        Flux.from(
            dslContext.select(columns)
                .from(ORDERS)
                .orderBy(ORDERS.CREATED_AT)
        ).map { mapper.map(it) }
            .collectList()

    val insert = dslContext.insertInto(ORDERS,
                                       ORDERS.CONSUMER_ID,
                                       ORDERS.PRICE)

    private fun values(orderRequest: OrderRequest) = listOf(
        orderRequest.consumerId,
        orderRequest.price
    )

    private val columns = listOf(
        ORDERS.ID,
        ORDERS.CONSUMER_ID,
        ORDERS.PRICE
    )
}
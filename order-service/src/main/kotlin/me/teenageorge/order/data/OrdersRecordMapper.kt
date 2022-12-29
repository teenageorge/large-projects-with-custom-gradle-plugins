package me.teenageorge.order.data

import me.teenageorge.order.domain.Order
import me.teenageorge.tables.Orders.ORDERS
import org.jooq.Record
import org.jooq.RecordMapper
import org.springframework.stereotype.Component

@Component
class OrdersRecordMapper: RecordMapper<Record, Order> {
    override fun map(record: Record): Order {
        return Order(
            id = record.get(ORDERS.ID),
            consumerId = record.get(ORDERS.CONSUMER_ID),
            price = record.get(ORDERS.PRICE))
    }
}
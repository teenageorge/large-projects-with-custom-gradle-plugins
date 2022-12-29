package me.teenageorge.order.usecase

import me.teenageorge.order.data.OrdersRepository
import me.teenageorge.order.domain.OrderRequest
import org.springframework.stereotype.Service

@Service
class OrderUseCase(private val repository: OrdersRepository) {
    fun createOrder(orderRequest: OrderRequest) = repository.create(orderRequest)
    fun getAllOrders() = repository.get()
}
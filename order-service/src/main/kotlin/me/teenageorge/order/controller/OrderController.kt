package me.teenageorge.order.controller

import me.teenageorge.order.domain.Order
import me.teenageorge.order.domain.OrderRequest
import me.teenageorge.order.usecase.OrderUseCase
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class OrderController(private val useCase: OrderUseCase) {
    @PostMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createOrder(@RequestBody orderRequest: OrderRequest): Mono<ResponseEntity<Order>> =
        useCase.createOrder(orderRequest).map { ResponseEntity.ok(it) }

    @GetMapping (
        produces = [MediaType.APPLICATION_JSON_VALUE]
            )
    fun getAll() = useCase.getAllOrders().map { ResponseEntity.ok(it) }
}
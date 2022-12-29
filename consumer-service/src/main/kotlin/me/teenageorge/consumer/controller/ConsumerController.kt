package me.teenageorge.consumer.controller

import me.teenageorge.consumer.domain.Consumer
import me.teenageorge.consumer.domain.ConsumerRequest
import me.teenageorge.consumer.usecase.ConsumerUseCase
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class ConsumerController(val useCase: ConsumerUseCase) {
    @PostMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createConsumer(@RequestBody consumerRequest: ConsumerRequest): Mono<ResponseEntity<Consumer>> =
        useCase.createConsumer(consumerRequest).map { ResponseEntity.ok(it) }

    @GetMapping (
        produces = [MediaType.APPLICATION_JSON_VALUE]
            )
    fun getAll() = useCase.getAllConsumers().map { ResponseEntity.ok(it) }
}
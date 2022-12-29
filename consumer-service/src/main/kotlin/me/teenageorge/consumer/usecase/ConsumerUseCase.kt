package me.teenageorge.consumer.usecase

import me.teenageorge.consumer.data.ConsumerRepository
import me.teenageorge.consumer.domain.ConsumerRequest
import org.springframework.stereotype.Service

@Service
class ConsumerUseCase(val repository: ConsumerRepository) {
    fun createConsumer(consumerRequest: ConsumerRequest) = repository.create(consumerRequest)
    fun getAllConsumers() = repository.get()
}
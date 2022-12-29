package me.teenageorge.consumer.data

import me.teenageorge.consumer.domain.Consumer
import me.teenageorge.consumer.domain.ConsumerRequest
import me.teenageorge.tables.Consumer.CONSUMER
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class ConsumerRepository(
    private val dslContext: DSLContext,
    private val mapper: ConsumerRecordMapper) {

    fun create(consumerRequest: ConsumerRequest): Mono<Consumer> {
        return Mono.from(
            insert.values(values(consumerRequest))
                .returningResult(columns)
        )
            .map { mapper.map(it) }
    }

    fun get() =
        Flux.from(
            dslContext.select(columns)
                .from(CONSUMER)
                .orderBy(CONSUMER.CREATED_AT)
        ).map { mapper.map(it) }
            .collectList()

    private val insert = dslContext.insertInto(CONSUMER,
                                               CONSUMER.EMAIL,
                                               CONSUMER.FIRST_NAME,
                                               CONSUMER.LAST_NAME)

    private fun values(consumerRequest: ConsumerRequest) = listOf(
        consumerRequest.email,
        consumerRequest.firstName,
        consumerRequest.lastName
    )

    val columns = listOf(
        CONSUMER.ID,
        CONSUMER.EMAIL,
        CONSUMER.FIRST_NAME,
        CONSUMER.LAST_NAME
    )
}
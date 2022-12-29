package me.teenageorge.consumer.data

import me.teenageorge.consumer.domain.Consumer
import me.teenageorge.tables.Consumer.CONSUMER
import org.jooq.Record
import org.jooq.RecordMapper
import org.springframework.stereotype.Component

@Component
class ConsumerRecordMapper: RecordMapper<Record, Consumer> {
    override fun map(record: Record): Consumer {
        return Consumer(
            id = record.get(CONSUMER.ID),
            email = record.get(CONSUMER.EMAIL),
        firstName = record.get(CONSUMER.FIRST_NAME),
        lastName = record.get(CONSUMER.LAST_NAME))
    }
}
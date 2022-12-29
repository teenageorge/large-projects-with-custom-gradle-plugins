package me.teenageorge.common.exception

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ServerWebInputException
import reactor.core.publisher.Mono

@ControllerAdvice
class GlobalExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(Exception::class)
    fun genericSearchErrorHandler(ex: Exception): Mono<ResponseEntity<Any>> {
        return when (ex) {
            is ServerWebInputException ->
                Mono.just(ResponseEntity.badRequest().body(handleWebInputException(ex)))
            else -> {
                logger.error(ex) { "Unexpected error: " }
                Mono.just(ResponseEntity.internalServerError().body(ex.message))
            }
        }
    }

    private fun handleWebInputException(inputException: ServerWebInputException): String {
        val name: String
        return when (val mismatchedInputException = inputException.rootCause) {
            is MismatchedInputException -> {
                name = mismatchedInputException.path[0].fieldName
                "Value for required input for field '$name' is missing or invalid."
            }
            else -> throw inputException
        }
    }
}
package org.task_manager.controller.utils

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.task_manager.controller.response.ErrorResponse
import org.task_manager.service.exception.EmployeeNotFoundException


@ControllerAdvice
@Suppress("unused")
class ControllerExceptionHandler {
    @ExceptionHandler(value = [EmployeeNotFoundException::class])
    fun employeeNotFoundException(
        ex: EmployeeNotFoundException,
        request: WebRequest?
    ): ResponseEntity<ErrorResponse> {
        val message = ErrorResponse(ex.message)

        return ResponseEntity<ErrorResponse>(message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [Exception::class])
    fun exception(
        ex: Exception,
        request: WebRequest?
    ): ResponseEntity<ErrorResponse> {
        val message = ErrorResponse(ex.message)

        return ResponseEntity<ErrorResponse>(message, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
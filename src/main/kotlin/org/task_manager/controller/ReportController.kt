package org.task_manager.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.task_manager.service.ReportService
import org.task_manager.tools.LogMethods
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
@RequestMapping("report")
@LogMethods
@Suppress("unused")
class ReportController(
    private val service: ReportService
) {

    @GetMapping
    fun report(
        @RequestParam(name = "dateFrom") dateFrom: LocalDate,
        @RequestParam(name = "dateTo", required = false) dateTo: LocalDateTime = LocalDateTime.now(),
        @RequestParam(name = "employeeName", required = false) employeeName: String? = null
    ) = service.report(dateFrom, dateTo, employeeName)
}
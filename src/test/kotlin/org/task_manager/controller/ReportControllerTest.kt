package org.task_manager.controller

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.task_manager.service.ReportService
import org.task_manager.service.dto.ReportDto
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals

class ReportControllerTest {
    private val random = EasyRandom()
    @MockK(relaxed = true)
    private lateinit var service: ReportService
    @InjectMockKs
    private lateinit var subj: ReportController
    @BeforeEach
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun report() {
        val dateFrom = LocalDate.now()
        val dateTo = LocalDateTime.now()
        val employeeName = random.nextObject(String::class.java)
        val report = random.nextObject(ReportDto::class.java)

        every { service.report(dateFrom, dateTo, employeeName) } returns report

        val result = subj.report(dateFrom, dateTo, employeeName)

        assertEquals(report, result)
    }
}
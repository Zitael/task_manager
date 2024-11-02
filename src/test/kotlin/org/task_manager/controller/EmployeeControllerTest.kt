package org.task_manager.controller

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.task_manager.service.EmployeeService
import org.task_manager.service.dto.EmployeeDto

class EmployeeControllerTest {

    private val random = EasyRandom()
    @MockK(relaxed = true)
    private lateinit var service: EmployeeService
    @InjectMockKs
    private lateinit var subj: EmployeeController

    @BeforeEach
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun getAll() {
        val dtos = listOf(random.nextObject(EmployeeDto::class.java))

        every { service.getAll() } returns dtos

        val result = subj.getAll()

        assertEquals(dtos, result)
    }

    @Test
    fun save() {
        val dto = random.nextObject(EmployeeDto::class.java)

        subj.save(dto)

        verify { service.save(dto) }
    }

    @Test
    fun findByName() {
        val name = random.nextObject(String::class.java)
        val dto = random.nextObject(EmployeeDto::class.java)

        every { service.findByName(name) } returns dto

        val result = subj.findByName(name)

        assertEquals(dto, result)
    }
}
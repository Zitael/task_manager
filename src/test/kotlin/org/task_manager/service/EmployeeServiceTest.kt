package org.task_manager.service

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.task_manager.db.entity.Employee
import org.task_manager.db.repository.EmployeeRepository
import org.task_manager.service.dto.EmployeeDto
import org.task_manager.service.mapper.EmployeeMapper

class EmployeeServiceTest {
    private val random = EasyRandom()

    @MockK
    private lateinit var mapper: EmployeeMapper
    @MockK(relaxed = true)
    lateinit var repository: EmployeeRepository
    @InjectMockKs
    private lateinit var subj: EmployeeService

    @BeforeEach
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun getAll() {
        val entities = listOf(random.nextObject(Employee::class.java))
        val dtos = listOf(random.nextObject(EmployeeDto::class.java))

        every { repository.findAll() } returns entities
        every { mapper.entityListToDtoList(entities) } returns dtos

        val result = subj.getAll()

        assertEquals(dtos, result)
    }

    @Test
    fun findByName() {
        val name = random.nextObject(String::class.java)
        val entity = random.nextObject(Employee::class.java)
        val dto = random.nextObject(EmployeeDto::class.java)

        every { repository.findByName(name) } returns entity
        every { mapper.entityToDto(entity) } returns dto

        val result = subj.findByName(name)

        assertEquals(dto, result)
    }

    @Test
    fun save() {
        val entity = random.nextObject(Employee::class.java)
        val dto = random.nextObject(EmployeeDto::class.java)

        every { mapper.dtoToEntity(dto) } returns entity
        every { repository.save(entity) } returns entity

        subj.save(dto)

        verify(exactly = 1) { repository.save(entity) }
    }
}
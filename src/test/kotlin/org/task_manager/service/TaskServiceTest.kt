package org.task_manager.service

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.runs
import io.mockk.verify
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.task_manager.controller.request.AssignTaskRequest
import org.task_manager.controller.request.UpdateTaskStatusRequest
import org.task_manager.db.entity.Employee
import org.task_manager.db.entity.Task
import org.task_manager.db.repository.TaskRepository
import org.task_manager.service.dto.EmployeeDto
import org.task_manager.service.dto.TaskDto
import org.task_manager.service.mapper.TaskMapper
import kotlin.test.assertEquals

class TaskServiceTest {

    private val random = EasyRandom()
    @MockK
    private lateinit var employeeService: EmployeeService
    @MockK
    private lateinit var mapper: TaskMapper
    @MockK(relaxed = true)
    lateinit var repository: TaskRepository
    @InjectMockKs
    private lateinit var subj: TaskService

    @BeforeEach
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun getAll() {
        val entities = listOf(random.nextObject(Task::class.java))
        val dtos = listOf(random.nextObject(TaskDto::class.java))

        every { repository.findAll() } returns entities
        every { mapper.entityListToDtoList(entities) } returns dtos

        val result = subj.getAll()

        assertEquals(dtos, result)
    }

    @Test
    fun save() {
        val entity = random.nextObject(Task::class.java)
        val dto = random.nextObject(TaskDto::class.java)

        every { mapper.dtoToEntity(dto) } returns entity
        every { repository.save(entity) } returns entity

        subj.save(dto)

        verify(exactly = 1) { repository.save(entity) }
    }

    @Test
    fun updateStatus() {
        val request = random.nextObject(UpdateTaskStatusRequest::class.java)

        subj.updateStatus(request)

        verify(exactly = 1) { repository.updateStatus(request.taskId, request.status, any()) }

    }

    @Test
    fun assign() {
        val request = random.nextObject(AssignTaskRequest::class.java)
        val employee = random.nextObject(EmployeeDto::class.java)

        every { employeeService.findByName(request.assigneeName) } returns employee

        subj.assign(request)

        verify { repository.assign(request.taskId, employee.id!!) }
    }
}
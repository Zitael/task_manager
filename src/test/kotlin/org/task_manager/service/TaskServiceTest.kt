package org.task_manager.service

import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.task_manager.controller.request.AssignTaskRequest
import org.task_manager.controller.request.TaskSaveRequest
import org.task_manager.controller.request.UpdateTaskStatusRequest
import org.task_manager.db.entity.Employee
import org.task_manager.db.entity.Task
import org.task_manager.db.repository.TaskRepository
import org.task_manager.service.dto.EmployeeDto
import org.task_manager.service.dto.TaskDto
import org.task_manager.service.exception.EmployeeNotFoundException
import org.task_manager.service.mapper.TaskMapper
import java.time.LocalDate
import java.time.LocalDateTime
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
        val request = random.nextObject(TaskSaveRequest::class.java)

        every { mapper.requestToEntity(request) } returns entity
        every { repository.save(entity) } returns entity

        subj.save(request)

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

    @Test
    fun assignWhenEmployeeNotFound() {
        val request = random.nextObject(AssignTaskRequest::class.java)

        every { employeeService.findByName(request.assigneeName) } returns null

        assertThrows<EmployeeNotFoundException> { subj.assign(request) }
    }

    @Test
    fun findAllByDates() {
        val dateFrom = LocalDate.now().minusDays(5)
        val dateTo = LocalDateTime.now()
        val tasks = listOf(
            Task(id = 1)
        )
        val dtos = listOf(
            TaskDto(id = 1)
        )

        every { repository.findByCreatedAtBetween(dateFrom.atStartOfDay(), dateTo) } returns tasks
        every { mapper.entityListToDtoList(tasks) } returns dtos

        val result = subj.findAllBy(dateFrom, dateTo)

        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
    }

    @Test
    fun findAllByDatesAndEmployee() {
        val dateFrom = LocalDate.now().minusDays(5)
        val employeeName = "testEmployee"
        val dateTo = LocalDateTime.now()
        val task1 = Task(id = 1, assignee = Employee(name = random.nextObject(String::class.java)))
        val task2 = Task(id = 2, assignee = Employee(name = employeeName))
        val dtos = listOf(
            TaskDto(id = 2, assignee = Employee(name = employeeName))
        )

        every { repository.findByCreatedAtBetween(dateFrom.atStartOfDay(), dateTo) } returns listOf(task1,task2)
        every { mapper.entityListToDtoList(listOf(task2)) } returns dtos

        val result = subj.findAllBy(dateFrom, dateTo, employeeName)

        assertEquals(1, result.size)
        assertEquals(2, result[0].id)
    }
}
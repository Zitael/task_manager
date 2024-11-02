package org.task_manager.service

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach
import org.task_manager.db.entity.Employee
import org.task_manager.service.dto.TaskDto
import org.task_manager.service.dto.TaskStatus
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ReportServiceTest {

    @MockK
    lateinit var service: TaskService
    @InjectMockKs
    private lateinit var subj: ReportService

    @BeforeEach
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun report() {
        val dateFrom = LocalDate.now().minusDays(5)
        val dateTo = LocalDateTime.now()

        val tasks = prepareTasks()

        every { service.findAllBy(dateFrom, dateTo, null) } returns tasks

        val result = subj.report(dateFrom, dateTo)

        with(result){
            assertEquals(5, this.tasksCreated)
            assertEquals(2, this.tasksCompleted)
            with(tasksMissedDueDate) {
                assertEquals(3, this.count)
                assertTrue { this.tasks.map { it.id }.containsAll(listOf(1,2,3)) }
            }
            with(tasksByStatuses) {
                assertEquals(3, this.size)
                assertEquals(2, this[TaskStatus.DONE]!!.count)
                assertTrue(this[TaskStatus.DONE]!!.tasks.map { it.id }.containsAll(listOf(1,4)))
                assertEquals(1, this[TaskStatus.CREATED]!!.count)
                assertTrue(this[TaskStatus.CREATED]!!.tasks.map { it.id }.contains(2))
                assertEquals(2, this[TaskStatus.IN_PROGRESS]!!.count)
                assertTrue(this[TaskStatus.IN_PROGRESS]!!.tasks.map { it.id }.containsAll(listOf(3,5)))
            }
            with(tasksByEmployee) {
                assertEquals(1, this["testEmployee1"]!!.count)
                assertEquals(1, this["testEmployee3"]!!.count)
                assertEquals(1, this["testEmployee4"]!!.count)
                assertEquals(1, this["testEmployee5"]!!.count)
                assertEquals(1, this[null]!!.count)
            }
            assertEquals(tasks, this.tasks)
        }
    }

    private fun prepareTasks() = listOf(
        TaskDto(
            id = 1,
            description = "done but missed the deadline",
            assignee = Employee(name = "testEmployee1"),
            status = TaskStatus.DONE,
            createdAt = LocalDateTime.now().minusDays(5),
            dueDate = LocalDate.now().minusDays(4),
            updatedAt = LocalDateTime.now().minusDays(2)
        ),
        TaskDto(
            id = 2,
            description = "created, unassigned and missed the deadline",
            status = TaskStatus.CREATED,
            createdAt = LocalDateTime.now().minusDays(5),
            dueDate = LocalDate.now().minusDays(4),
            updatedAt = LocalDateTime.now().minusDays(2)
        ),
        TaskDto(
            id = 3,
            description = "in progress and missed the deadline",
            assignee = Employee(name = "testEmployee3"),
            status = TaskStatus.IN_PROGRESS,
            createdAt = LocalDateTime.now().minusDays(5),
            dueDate = LocalDate.now().minusDays(4),
            updatedAt = LocalDateTime.now().minusDays(2)
        ),
        TaskDto(
            id = 4,
            description = "done in time",
            assignee = Employee(name = "testEmployee4"),
            status = TaskStatus.DONE,
            createdAt = LocalDateTime.now().minusDays(5),
            dueDate = LocalDate.now().minusDays(4),
            updatedAt = LocalDateTime.now().minusDays(4)
        ),
        TaskDto(
            id = 5,
            description = "in progress but have time",
            assignee = Employee(name = "testEmployee5"),
            status = TaskStatus.IN_PROGRESS,
            createdAt = LocalDateTime.now().minusDays(5),
            dueDate = LocalDate.now().plusDays(1),
            updatedAt = LocalDateTime.now().minusDays(2)
        )
    )
}
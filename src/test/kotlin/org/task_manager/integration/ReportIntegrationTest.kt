package org.task_manager.integration

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.task_manager.db.entity.Employee
import org.task_manager.db.entity.Task
import org.task_manager.db.repository.EmployeeRepository
import org.task_manager.db.repository.TaskRepository
import org.task_manager.service.dto.ReportDto
import org.task_manager.service.dto.TaskStatus
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReportIntegrationTest {
    @Autowired
    private lateinit var om: ObjectMapper

    @Autowired
    private var mockMvc: MockMvc? = null

    @Autowired
    private lateinit var taskRepository: TaskRepository

    @Autowired
    private lateinit var employeeRepository: EmployeeRepository

    @AfterEach
    fun tearDown() {
        taskRepository.deleteAll()
        employeeRepository.deleteAll()
    }

    @Test
    fun report() {
        prepareData()

        val result = mockMvc!!.perform(MockMvcRequestBuilders.get("/report?dateFrom=${LocalDate.now().minusDays(6)}"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn()

        val report = om.readValue(result.response.contentAsString, ReportDto::class.java)
        assertNotNull(report)
        with(report) {
            assertEquals(6, tasksCreated)
            assertEquals(2, tasksCompleted)
            assertEquals(5, averageTaskDurationDays)

            assertEquals(3, tasksMissedDueDate.count)
            assertTrue(tasksMissedDueDate.tasks.map { it.description }
                .containsAll(
                    listOf(
                        "unassigned and expired",
                        "in progress on employee1 and expired",
                        "done by employee2 but expired"
                    )
                )
            )

            assertEquals(3, tasksByStatuses.size)
            assertEquals(2, tasksByStatuses[TaskStatus.CREATED]?.count)
            assertTrue(tasksByStatuses[TaskStatus.CREATED]!!.tasks.map { it.description }
                .containsAll(
                    listOf(
                        "unassigned but we have time",
                        "unassigned and expired"
                    )
                )
            )
            assertEquals(2, tasksByStatuses[TaskStatus.IN_PROGRESS]?.count)
            assertTrue(tasksByStatuses[TaskStatus.IN_PROGRESS]!!.tasks.map { it.description }
                .containsAll(
                    listOf(
                        "in progress on employee1 but we have time",
                        "in progress on employee1 and expired"
                    )
                )
            )
            assertEquals(2, tasksByStatuses[TaskStatus.DONE]?.count)
            assertTrue(tasksByStatuses[TaskStatus.DONE]!!.tasks.map { it.description }
                .containsAll(
                    listOf(
                        "done by employee2 in time",
                        "done by employee2 but expired"
                    )
                )
            )

            assertEquals(3, tasksByEmployee.size)
            with(tasksByEmployee["UNASSIGNED"]!!) {
                assertEquals(2, count)
                assertEquals(0, completed)
                assertEquals(5, averageTaskDurationDays)
                assertTrue(tasks.map { it.description }
                    .containsAll(
                        listOf(
                            "unassigned but we have time",
                            "unassigned and expired"
                        )
                    )
                )
            }
            with(tasksByEmployee["employeeName1"]!!) {
                assertEquals(2, count)
                assertEquals(0, completed)
                assertEquals(5, averageTaskDurationDays)
                assertTrue(tasks.map { it.description }
                    .containsAll(
                        listOf(
                            "in progress on employee1 but we have time",
                            "in progress on employee1 and expired"
                        )
                    )
                )
            }
            with(tasksByEmployee["employeeName2"]!!) {
                assertEquals(2, count)
                assertEquals(2, completed)
                assertEquals(5, averageTaskDurationDays)
                assertTrue(tasks.map { it.description }
                    .containsAll(
                        listOf(
                            "done by employee2 in time",
                            "done by employee2 but expired"
                        )
                    )
                )
            }

            assertEquals(6, tasks.size)
        }
    }

    private fun prepareData() {
        val employee1 = employeeRepository.save(Employee(name = "employeeName1"))
        val employee2 = employeeRepository.save(Employee(name = "employeeName2"))
        employeeRepository.flush()

        taskRepository.save(
            Task(
                description = "unassigned but we have time",
                status = TaskStatus.CREATED,
                dueDate = LocalDate.now().plusDays(1),
                createdAt = LocalDateTime.now().minusDays(5)
            )
        )
        taskRepository.save(
            Task(
                description = "unassigned and expired",
                status = TaskStatus.CREATED,
                dueDate = LocalDate.now().minusDays(1),
                createdAt = LocalDateTime.now().minusDays(5)
            )
        )
        taskRepository.save(
            Task(
                description = "in progress on employee1 but we have time",
                status = TaskStatus.IN_PROGRESS,
                dueDate = LocalDate.now().plusDays(1),
                createdAt = LocalDateTime.now().minusDays(5),
                assignee = employee1
            )
        )
        taskRepository.save(
            Task(
                description = "in progress on employee1 and expired",
                status = TaskStatus.IN_PROGRESS,
                dueDate = LocalDate.now().minusDays(1),
                createdAt = LocalDateTime.now().minusDays(5),
                assignee = employee1
            )
        )
        taskRepository.save(
            Task(
                description = "done by employee2 in time",
                status = TaskStatus.DONE,
                dueDate = LocalDate.now().plusDays(1),
                createdAt = LocalDateTime.now().minusDays(5),
                assignee = employee2
            )
        )
        taskRepository.save(
            Task(
                description = "done by employee2 but expired",
                status = TaskStatus.DONE,
                dueDate = LocalDate.now().minusDays(1),
                createdAt = LocalDateTime.now().minusDays(5),
                assignee = employee2
            )
        )

        taskRepository.flush()
    }
}
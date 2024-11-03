package org.task_manager.integration

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.hamcrest.Matchers
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.task_manager.controller.request.AssignTaskRequest
import org.task_manager.controller.request.TaskSaveRequest
import org.task_manager.controller.request.UpdateTaskStatusRequest
import org.task_manager.db.entity.Employee
import org.task_manager.db.entity.Task
import org.task_manager.db.repository.EmployeeRepository
import org.task_manager.db.repository.TaskRepository
import org.task_manager.service.dto.TaskDto
import org.task_manager.service.dto.TaskStatus
import kotlin.test.assertEquals

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskIntegrationTest {

    private val random = EasyRandom()

    @Autowired
    private lateinit var om: ObjectMapper

    @Autowired
    @Suppress("unused")
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
    fun getAll() {
        val employee = employeeRepository.saveAndFlush(Employee(name = "employeeName"))
        val task = taskRepository.saveAndFlush(Task(status = TaskStatus.CREATED, assignee = employee))

        mockMvc!!.perform(MockMvcRequestBuilders.get("/task/all"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(jsonPath("$[0].status", Matchers.equalTo(task.status.name)))
            .andExpect(jsonPath("$[0].assignee.name", Matchers.equalTo(employee.name)))
            .andReturn()
    }

    @Test
    fun save() {
        val request = random.nextObject(TaskSaveRequest::class.java)
        mockMvc!!.perform(
            MockMvcRequestBuilders
                .post("/task/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn()

        val tasks = taskRepository.findAll()
        assertEquals(1, tasks.size)
        assertEquals(request.title, tasks[0].title)
        assertEquals(request.status, tasks[0].status)
    }

    @Test
    fun updateStatus() {
        val task = taskRepository.saveAndFlush(Task(status = TaskStatus.CREATED))
        val request = UpdateTaskStatusRequest(taskId = task.id!!, status = TaskStatus.IN_PROGRESS)
        mockMvc!!.perform(
            MockMvcRequestBuilders
                .post("/task/update-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn()

        val tasks = taskRepository.findAll()
        assertEquals(1, tasks.size)
        assertEquals(task.id, tasks[0].id)
        assertEquals(request.status, tasks[0].status)
    }

    @Test
    fun assign() {
        val task = taskRepository.saveAndFlush(Task(status = TaskStatus.CREATED))
        val employee = employeeRepository.saveAndFlush(Employee(name = "employeeName"))
        val request = AssignTaskRequest(taskId = task.id!!, assigneeName = employee.name!!)
        mockMvc!!.perform(
            MockMvcRequestBuilders
                .post("/task/assign")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn()

        val tasks = taskRepository.findAll()
        assertEquals(1, tasks.size)
        assertEquals(task.id, tasks[0].id)
        assertEquals(request.assigneeName, tasks[0].assignee!!.name)
    }

    @Test
    fun assignWhenEmployeeNotFound() {
        val task = taskRepository.saveAndFlush(Task(status = TaskStatus.CREATED))
        val request = AssignTaskRequest(taskId = task.id!!, assigneeName = random.nextObject(String::class.java))
        mockMvc!!.perform(
            MockMvcRequestBuilders
                .post("/task/assign")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(jsonPath("$.message", Matchers.equalTo("Incorrect assignee name")))
            .andReturn()
    }
}
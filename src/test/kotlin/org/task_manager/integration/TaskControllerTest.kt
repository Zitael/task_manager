package org.task_manager.integration

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
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
import org.task_manager.controller.request.AssignTaskRequest
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
class TaskControllerTest {

    private val random = EasyRandom()
    private val om = ObjectMapper()
        .registerModule(JavaTimeModule())
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

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
        val expectedResponse = "[${om.writeValueAsString(task)}]"

        mockMvc!!.perform(MockMvcRequestBuilders.get("/task/all"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(MockMvcResultMatchers.content().string(expectedResponse))
            .andReturn()
    }

    @Test
    fun save() {
        val employee = employeeRepository.saveAndFlush(random.nextObject(Employee::class.java))
        val request = random.nextObject(TaskDto::class.java)
        request.assignee = employee
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
}
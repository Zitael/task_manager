package org.task_manager.integration

import com.fasterxml.jackson.databind.ObjectMapper
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
import org.task_manager.controller.request.EmployeeSaveRequest
import org.task_manager.db.entity.Employee
import org.task_manager.db.repository.EmployeeRepository
import org.task_manager.service.dto.EmployeeDto
import kotlin.test.assertEquals

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeIntegrationTest {

    private val random = EasyRandom()

    @Autowired
    private lateinit var om: ObjectMapper

    @Autowired
    private var mockMvc: MockMvc? = null

    @Autowired
    private lateinit var employeeRepository: EmployeeRepository

    @AfterEach
    fun tearDown() {
        employeeRepository.deleteAll()
    }

    @Test
    fun getAll() {
        val employee = employeeRepository.saveAndFlush(Employee(name = "employeeName"))
        val expectedResponse = "[${om.writeValueAsString(employee)}]"

        mockMvc!!.perform(MockMvcRequestBuilders.get("/employee/all"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(MockMvcResultMatchers.content().string(expectedResponse))
            .andReturn()
    }

    @Test
    fun findByName() {
        val employee = employeeRepository.saveAndFlush(Employee(name = "employeeName"))
        val expectedResponse = om.writeValueAsString(employee)

        mockMvc!!.perform(
            MockMvcRequestBuilders.get("/employee/find-by-name?name=employeeName"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andExpect(MockMvcResultMatchers.content().string(expectedResponse))
            .andReturn()
    }

    @Test
    fun save() {
        val request = random.nextObject(EmployeeSaveRequest::class.java)
        mockMvc!!.perform(
            MockMvcRequestBuilders
                .post("/employee/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
            .andReturn()

        val employees = employeeRepository.findAll()
        assertEquals(1, employees.size)
        assertEquals(request.name, employees[0].name)
    }
}
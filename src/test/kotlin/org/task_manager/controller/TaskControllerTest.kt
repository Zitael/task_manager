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
import org.task_manager.controller.request.AssignTaskRequest
import org.task_manager.controller.request.TaskSaveRequest
import org.task_manager.controller.request.UpdateTaskStatusRequest
import org.task_manager.service.TaskService
import org.task_manager.service.dto.TaskDto

class TaskControllerTest {

    private val random = EasyRandom()
    @MockK(relaxed = true)
    private lateinit var service: TaskService
    @InjectMockKs
    private lateinit var subj: TaskController

    @BeforeEach
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun getAll() {
        val dtos = listOf(random.nextObject(TaskDto::class.java))

        every { service.getAll() } returns dtos

        val result = subj.getAll()

        assertEquals(dtos, result)
    }

    @Test
    fun save() {
        val dto = random.nextObject(TaskSaveRequest::class.java)

        subj.save(dto)

        verify { service.save(dto) }
    }

    @Test
    fun updateStatus() {
        val request = random.nextObject(UpdateTaskStatusRequest::class.java)

        subj.updateStatus(request)

        verify { service.updateStatus(request) }
    }

    @Test
    fun assign() {
        val request = random.nextObject(AssignTaskRequest::class.java)

        subj.assign(request)

        verify { service.assign(request) }
    }
}
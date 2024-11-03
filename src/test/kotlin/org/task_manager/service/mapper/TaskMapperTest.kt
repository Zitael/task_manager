package org.task_manager.service.mapper

import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.task_manager.controller.request.TaskSaveRequest
import org.task_manager.db.entity.Task
import kotlin.test.assertNull

class TaskMapperTest {

    private val mapper = TaskMapperImpl()
    private val random = EasyRandom()

    @Test
    fun entityToDto() {
        val entity = random.nextObject(Task::class.java)

        val dto = mapper.entityToDto(entity)

        assertEquals(entity.id, dto.id)
        assertEquals(entity.title, dto.title)
        assertEquals(entity.description, dto.description)
        assertEquals(entity.dueDate, dto.dueDate)
        assertEquals(entity.priority, dto.priority)
        assertEquals(entity.assignee, dto.assignee)
        assertEquals(entity.status, dto.status)
    }

    @Test
    fun entityListToDtoList() {
        val entity1 = random.nextObject(Task::class.java)
        val entity2 = random.nextObject(Task::class.java)

        val dtos = mapper.entityListToDtoList(listOf(entity1, entity2))

        assertEquals(entity1.id, dtos[0].id)
        assertEquals(entity1.title, dtos[0].title)
        assertEquals(entity1.description, dtos[0].description)
        assertEquals(entity1.dueDate, dtos[0].dueDate)
        assertEquals(entity1.priority, dtos[0].priority)
        assertEquals(entity1.assignee, dtos[0].assignee)
        assertEquals(entity1.status, dtos[0].status)
        assertEquals(entity2.id, dtos[1].id)
        assertEquals(entity2.title, dtos[1].title)
        assertEquals(entity2.description, dtos[1].description)
        assertEquals(entity2.dueDate, dtos[1].dueDate)
        assertEquals(entity2.priority, dtos[1].priority)
        assertEquals(entity2.assignee, dtos[1].assignee)
        assertEquals(entity2.status, dtos[1].status)
    }

    @Test
    fun requestToEntity() {
        val request = random.nextObject(TaskSaveRequest::class.java)

        val entity = mapper.requestToEntity(request)

        assertNull(entity.id)
        assertEquals(request.title, entity.title)
        assertEquals(request.description, entity.description)
        assertEquals(request.dueDate, entity.dueDate)
        assertEquals(request.priority, entity.priority)
        assertEquals(request.status, entity.status)
    }
}
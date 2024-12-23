package org.task_manager.service.mapper

import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.Test
import org.task_manager.controller.request.EmployeeSaveRequest
import org.task_manager.db.entity.Employee
import kotlin.test.assertEquals
import kotlin.test.assertNull

class EmployeeMapperTest {

    private val mapper = EmployeeMapperImpl()
    private val random = EasyRandom()

    @Test
    fun entityToDto() {
        val entity = random.nextObject(Employee::class.java)

        val dto = mapper.entityToDto(entity)

        assertEquals(entity.id, dto?.id)
        assertEquals(entity.name, dto?.name)
    }

    @Test
    fun entityListToDtoList() {
        val entity1 = random.nextObject(Employee::class.java)
        val entity2 = random.nextObject(Employee::class.java)

        val dtos = mapper.entityListToDtoList(listOf(entity1, entity2))

        assertEquals(entity1.id, dtos[0].id)
        assertEquals(entity1.name, dtos[0].name)
        assertEquals(entity2.id, dtos[1].id)
        assertEquals(entity2.name, dtos[1].name)
    }

    @Test
    fun requestToEntity() {
        val request = random.nextObject(EmployeeSaveRequest::class.java)

        val entity = mapper.requestToEntity(request)

        assertNull(entity.id)
        assertEquals(request.name, entity.name)
    }
}
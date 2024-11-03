package org.task_manager.service

import org.springframework.stereotype.Service
import org.task_manager.controller.request.EmployeeSaveRequest
import org.task_manager.db.entity.Employee
import org.task_manager.db.repository.EmployeeRepository
import org.task_manager.service.dto.EmployeeDto
import org.task_manager.service.mapper.EmployeeMapper

@Service
class EmployeeService(
    private val repository: EmployeeRepository,
    private val mapper: EmployeeMapper
) {

    fun getAll(): List<EmployeeDto> = mapper.entityListToDtoList(repository.findAll())

    fun findByName(name: String) = repository.findByName(name)?.let { mapper.entityToDto(it) }

    fun save(request: EmployeeSaveRequest): Employee = repository.save(mapper.requestToEntity(request))
}
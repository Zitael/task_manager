package org.task_manager.service

import org.springframework.stereotype.Service
import org.task_manager.db.repository.EmployeeRepository
import org.task_manager.service.dto.EmployeeDto
import org.task_manager.service.mapper.EmployeeMapper

@Service
class EmployeeService(
    private val repository: EmployeeRepository,
    private val mapper: EmployeeMapper
) {

    fun getAll(): List<EmployeeDto> = mapper.entityListToDtoList(repository.findAll())

    fun findByName(name: String) = mapper.entityToDto(repository.findByName(name))

    fun save(dto: EmployeeDto) = repository.save(mapper.dtoToEntity(dto))
}
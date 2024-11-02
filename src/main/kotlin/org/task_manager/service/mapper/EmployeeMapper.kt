package org.task_manager.service.mapper

import org.mapstruct.Mapper
import org.task_manager.db.entity.Employee
import org.task_manager.service.dto.EmployeeDto

@Mapper(componentModel = "spring")
interface EmployeeMapper {

    fun entityToDto(task: Employee): EmployeeDto
    fun entityListToDtoList(tasks: List<Employee>): List<EmployeeDto>

    fun dtoToEntity(dto: EmployeeDto): Employee
}
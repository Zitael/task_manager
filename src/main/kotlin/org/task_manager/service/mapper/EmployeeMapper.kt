package org.task_manager.service.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.task_manager.controller.request.EmployeeSaveRequest
import org.task_manager.db.entity.Employee
import org.task_manager.service.dto.EmployeeDto

@Mapper(componentModel = "spring")
interface EmployeeMapper {

    fun entityToDto(task: Employee?): EmployeeDto?
    fun entityListToDtoList(tasks: List<Employee>): List<EmployeeDto>

    @Mapping(target = "id", ignore = true)
    fun requestToEntity(request: EmployeeSaveRequest): Employee
}
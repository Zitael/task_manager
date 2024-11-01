package org.task_manager.service.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EmployeeDto(
    val id: Long,
    val name: String?
)
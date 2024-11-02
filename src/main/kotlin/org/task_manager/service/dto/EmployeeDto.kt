package org.task_manager.service.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EmployeeDto(
    var id: Long? = null,
    var name: String? = null,
)
package org.task_manager.controller.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class EmployeeSaveRequest(
    var name: String? = null,
)
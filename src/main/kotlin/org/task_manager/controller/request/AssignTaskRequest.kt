package org.task_manager.controller.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class AssignTaskRequest(
    val taskId: Long,
    val assigneeName: String
)
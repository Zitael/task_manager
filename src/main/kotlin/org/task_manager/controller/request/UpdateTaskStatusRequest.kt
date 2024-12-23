package org.task_manager.controller.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.task_manager.service.dto.TaskStatus

@JsonIgnoreProperties(ignoreUnknown = true)
data class UpdateTaskStatusRequest(
    val taskId: Long,
    val status: TaskStatus
)
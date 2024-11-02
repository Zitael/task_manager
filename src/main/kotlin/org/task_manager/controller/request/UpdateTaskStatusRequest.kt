package org.task_manager.controller.request

import org.task_manager.service.dto.TaskStatus

data class UpdateTaskStatusRequest(
    val taskId: Long,
    val status: TaskStatus
)
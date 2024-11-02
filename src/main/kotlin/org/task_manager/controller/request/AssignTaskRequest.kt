package org.task_manager.controller.request

data class AssignTaskRequest(
    val taskId: Long,
    val assigneeName: String
)
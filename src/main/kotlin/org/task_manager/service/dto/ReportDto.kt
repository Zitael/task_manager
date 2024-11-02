package org.task_manager.service.dto

data class ReportDto(
    var tasksCreated: Int,
    var tasksCompleted: Int,
    var tasksMissedDueDate: ReportTasks,
    var tasksByStatuses: Map<TaskStatus?, ReportTasks>,
    var tasksByEmployee: Map<String?, ReportTasks>,
    var tasks: List<TaskDto>
)

data class ReportTasks(
    var count: Int,
    var completed: Int? = null,
    var tasks: List<TaskDto>
)
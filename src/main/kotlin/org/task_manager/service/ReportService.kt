package org.task_manager.service

import org.springframework.stereotype.Service
import org.task_manager.service.dto.ReportDto
import org.task_manager.service.dto.ReportTasks
import org.task_manager.service.dto.TaskDto
import org.task_manager.service.dto.TaskStatus
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class ReportService(
    private val taskService: TaskService
) {

    fun report(
        dateFrom: LocalDate,
        dateTo: LocalDateTime = LocalDateTime.now(),
        employeeName: String? = null
    ): ReportDto {
        val tasks = taskService.findAllBy(dateFrom, dateTo, employeeName)
        val tasksByStatuses = getTasksByStatuses(tasks)
        val tasksByEmployee = getTasksByEmployee(tasks)
        val tasksMissedDueDate = getTasksMissedDueDate(tasks)
        return ReportDto(
            tasksCreated = tasks.size,
            tasksCompleted = tasksByStatuses[TaskStatus.DONE]?.count ?: 0,
            tasksMissedDueDate = tasksMissedDueDate,
            tasksByStatuses = tasksByStatuses,
            tasksByEmployee = tasksByEmployee,
            tasks = tasks
        )
    }

    private fun getTasksMissedDueDate(tasks: List<TaskDto>): ReportTasks {
        val filtered = tasks
            .filter { it.dueDate != null }
            .filter { taskIsDoneButMissedDueDate(it) || taskIsInProgressAndMissedDueDate(it) }
        return ReportTasks(count = filtered.size, tasks = filtered)
    }

    private fun taskIsInProgressAndMissedDueDate(it: TaskDto) =
        it.status != TaskStatus.DONE && LocalDate.now().isAfter(it.dueDate)

    private fun taskIsDoneButMissedDueDate(it: TaskDto) =
        it.status == TaskStatus.DONE && it.updatedAt.toLocalDate().isAfter(it.dueDate)

    private fun getTasksByEmployee(tasks: List<TaskDto>) = tasks
        .groupBy { it.assignee?.name }
        .mapValues { (_, tasks) ->
            ReportTasks(
                count = tasks.size,
                completed = tasks.filter { it.status == TaskStatus.DONE }.size,
                tasks = tasks
            )
        }

    private fun getTasksByStatuses(tasks: List<TaskDto>) = tasks
        .groupBy { it.status }
        .mapValues { (_, tasks) ->
            ReportTasks(count = tasks.size, tasks = tasks)
        }
}
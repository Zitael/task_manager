package org.task_manager.service.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.task_manager.db.entity.Employee
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TaskDto(
    val id: Long,
    val title: String?,
    val description: String?,
    val dueDate: LocalDate?,
    val priority: Int?,
    val assignee: Employee?,
    val status: TaskStatus?
)
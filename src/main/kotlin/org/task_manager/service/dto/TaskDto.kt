package org.task_manager.service.dto

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.persistence.Column
import org.task_manager.db.entity.Employee
import java.time.LocalDate
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TaskDto(
    var id: Long? = null,
    var title: String? = null,
    var description: String? = null,
    var dueDate: LocalDate? = null,
    var priority: Int? = null,
    var assignee: Employee? = null,
    var status: TaskStatus? = null,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
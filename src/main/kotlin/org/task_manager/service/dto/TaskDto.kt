package org.task_manager.service.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.format.annotation.DateTimeFormat
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
    var status: TaskStatus = TaskStatus.CREATED,
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss.SSSSSSS")
    var createdAt: LocalDateTime = LocalDateTime.now(),
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss.SSSSSSS")
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
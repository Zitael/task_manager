package org.task_manager.db.entity

import jakarta.persistence.*
import org.task_manager.service.dto.TaskStatus
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "task")
data class Task(
    @Id @GeneratedValue
    val id: Long,
    val title: String?,
    val description: String?,
    @Column(name = "due_date")
    val dueDate: LocalDate?,
    val priority: Int?,
    @ManyToOne
    @JoinColumn(name = "assignee_id")
    val assignee: Employee?,
    @Enumerated(EnumType.STRING)
    val status: TaskStatus,
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
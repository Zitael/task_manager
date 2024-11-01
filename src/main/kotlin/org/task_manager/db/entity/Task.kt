package org.task_manager.db.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "task")
data class Task(
    @Id @GeneratedValue
    val id: Long,
    val title: String,
    val description: String,
    @Column(name = "due_date")
    val dueDate: LocalDate,
    val priority: Int,
    @ManyToOne
    @JoinColumn(name = "assignee_id")
    val assignee: Employee
)
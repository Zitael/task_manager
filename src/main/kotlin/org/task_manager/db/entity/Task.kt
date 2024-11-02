package org.task_manager.db.entity

import jakarta.persistence.*
import org.task_manager.service.dto.TaskStatus
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "task")
@SequenceGenerator(name = "task_seq", sequenceName = "task_seq", allocationSize = 1)
data class Task(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    var id: Long? = null,
    var title: String? = null,
    var description: String? = null,
    @Column(name = "due_date")
    var dueDate: LocalDate? = null,
    var priority: Int? = null,
    @ManyToOne
    @JoinColumn(name = "assignee_id")
    var assignee: Employee? = null,
    @Enumerated(EnumType.STRING)
    var status: TaskStatus = TaskStatus.CREATED,
    @Column(name = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
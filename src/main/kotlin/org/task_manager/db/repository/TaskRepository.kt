package org.task_manager.db.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import org.task_manager.db.entity.Task
import org.task_manager.service.dto.TaskStatus
import java.time.LocalDateTime


@Repository
@Transactional
interface TaskRepository: JpaRepository<Task, Long> {

    fun findByCreatedAtBetween(startDate: LocalDateTime, endDate: LocalDateTime): List<Task>

    @Modifying
    @Query("UPDATE Task t SET t.status = :status, t.updatedAt = :updated_at WHERE t.id = :task_id")
    fun updateStatus(
        @Param("task_id") id: Long,
        @Param("status") status: TaskStatus,
        @Param("updated_at") updatedAt: LocalDateTime = LocalDateTime.now()
    )

    @Modifying
    @Query(value = "UPDATE task SET assignee_id = :assignee_id WHERE id = :task_id", nativeQuery = true)
    fun assign(
        @Param("task_id") taskId: Long,
        @Param("assignee_id") assigneeId: Long
    )
}
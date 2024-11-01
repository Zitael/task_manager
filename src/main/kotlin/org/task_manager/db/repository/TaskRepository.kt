package org.task_manager.db.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.task_manager.db.entity.Task

@Repository
interface TaskRepository: JpaRepository<Task, Long>
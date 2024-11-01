package org.task_manager.service

import org.springframework.stereotype.Service
import org.task_manager.db.repository.TaskRepository

@Service
class TaskService(
    private val repository: TaskRepository
) {

    fun getAll() = repository.findAll()
}
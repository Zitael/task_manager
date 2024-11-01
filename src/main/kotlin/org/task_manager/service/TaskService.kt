package org.task_manager.service

import org.springframework.stereotype.Service
import org.task_manager.db.repository.TaskRepository
import org.task_manager.service.dto.TaskDto
import org.task_manager.service.mapper.TaskMapper

@Service
class TaskService(
    private val repository: TaskRepository,
    private val mapper: TaskMapper
) {

    fun getAll(): List<TaskDto> = mapper.tasksToTaskDtoList(repository.findAll())
}
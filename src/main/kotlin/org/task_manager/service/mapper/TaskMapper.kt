package org.task_manager.service.mapper

import org.mapstruct.Mapper
import org.task_manager.db.entity.Task
import org.task_manager.service.dto.TaskDto

@Mapper(componentModel = "spring")
interface TaskMapper {

    fun taskToTaskDto(task: Task): TaskDto
    fun tasksToTaskDtoList(tasks: List<Task>): List<TaskDto>
}
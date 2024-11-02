package org.task_manager.service.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.task_manager.db.entity.Task
import org.task_manager.service.dto.TaskDto

@Mapper(componentModel = "spring")
interface TaskMapper {

    fun entityToDto(task: Task): TaskDto
    fun entityListToDtoList(tasks: List<Task>): List<TaskDto>

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", ignore = true)
    fun dtoToEntity(dto: TaskDto): Task
}
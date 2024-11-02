package org.task_manager.service

import org.springframework.stereotype.Service
import org.task_manager.controller.request.AssignTaskRequest
import org.task_manager.controller.request.UpdateTaskStatusRequest
import org.task_manager.db.repository.TaskRepository
import org.task_manager.service.dto.TaskDto
import org.task_manager.service.mapper.TaskMapper
import org.task_manager.tools.LogMethods

@Service
@LogMethods
class TaskService(
    private val repository: TaskRepository,
    private val employeeService: EmployeeService,
    private val mapper: TaskMapper
) {

    fun getAll(): List<TaskDto> = mapper.entityListToDtoList(repository.findAll())

    fun save(task: TaskDto) = repository.save(mapper.dtoToEntity(task))

    fun updateStatus(request: UpdateTaskStatusRequest) = repository.updateStatus(request.taskId, request.status)

    fun assign(request: AssignTaskRequest) {
        employeeService.findByName(request.assigneeName)
            .id
            ?.let { employeeId -> repository.assign(request.taskId, employeeId) }
    }
}
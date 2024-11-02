package org.task_manager.service

import org.springframework.stereotype.Service
import org.task_manager.controller.request.AssignTaskRequest
import org.task_manager.controller.request.UpdateTaskStatusRequest
import org.task_manager.db.entity.Task
import org.task_manager.db.repository.TaskRepository
import org.task_manager.service.dto.TaskDto
import org.task_manager.service.mapper.TaskMapper
import org.task_manager.tools.LogMethods
import java.time.LocalDate
import java.time.LocalDateTime

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

    fun findAllBy(
        dateFrom: LocalDate,
        dateTo: LocalDateTime = LocalDateTime.now(),
        employeeName: String? = null
    ): List<TaskDto> {
        val tasks = repository.findByCreatedAtBetween(dateFrom.atStartOfDay(), dateTo)
        val filtered = if (employeeName == null) {
            tasks
        } else {
            tasks.filter { it.assignee?.name == employeeName }
        }
        return mapper.entityListToDtoList(filtered)
    }
}
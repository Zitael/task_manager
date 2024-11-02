package org.task_manager.controller

import org.springframework.web.bind.annotation.*
import org.task_manager.controller.request.AssignTaskRequest
import org.task_manager.controller.request.UpdateTaskStatusRequest
import org.task_manager.service.TaskService
import org.task_manager.service.dto.TaskDto
import org.task_manager.tools.LogMethods

@RestController
@RequestMapping("task")
@LogMethods
class TaskController(
    private val service: TaskService
) {

    @GetMapping("all")
    fun getAll() = service.getAll()

    @PostMapping("save")
    fun save(@RequestBody task: TaskDto) = service.save(task)

    @PostMapping("update-status")
    fun updateStatus(@RequestBody request: UpdateTaskStatusRequest) = service.updateStatus(request)

    @PostMapping("assign")
    fun assign(@RequestBody request: AssignTaskRequest) = service.assign(request)
}
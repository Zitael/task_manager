package org.task_manager.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.task_manager.service.TaskService
import org.task_manager.tools.LogMethods

@RestController(value = "/task")
@LogMethods
class TaskController(
    private val service: TaskService
) {

    @GetMapping("/all")
    fun getAll() = service.getAll()
}
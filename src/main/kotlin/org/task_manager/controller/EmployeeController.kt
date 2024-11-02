package org.task_manager.controller

import org.springframework.web.bind.annotation.*
import org.task_manager.service.EmployeeService
import org.task_manager.service.dto.EmployeeDto
import org.task_manager.tools.LogMethods

@RestController
@RequestMapping("employee")
@LogMethods
class EmployeeController(
    private val service: EmployeeService
) {

    @GetMapping("all")
    fun getAll() = service.getAll()

    @PostMapping("save")
    fun save(@RequestBody task: EmployeeDto) = service.save(task)

    @GetMapping("find-by-name")
    fun findByName(@RequestParam(name = "name") name: String) = service.findByName(name)
}
package org.task_manager.db.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import org.task_manager.db.entity.Employee


@Repository
@Transactional
interface EmployeeRepository: JpaRepository<Employee, Long> {

    fun findByName(name: String): Employee
}
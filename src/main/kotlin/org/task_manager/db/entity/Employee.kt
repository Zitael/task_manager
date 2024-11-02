package org.task_manager.db.entity

import jakarta.persistence.*

@Entity
@Table(name = "employee")
@SequenceGenerator(name = "employee_seq", sequenceName = "task_seq", allocationSize = 1)
data class Employee(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_seq")
    var id: Long? = null,
    var name: String?
)
package org.task_manager.db.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "employee")
data class Employee(
    @Id @GeneratedValue
    val id: Long,
    val name: String?
)
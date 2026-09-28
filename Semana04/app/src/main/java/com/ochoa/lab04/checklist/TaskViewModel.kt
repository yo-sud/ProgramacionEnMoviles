package com.ochoa.lab04.checklist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val pendingCount: Int = 0,
    val pendingTitles: List<String> = emptyList()
)

class TaskViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    fun addTask(title: String): Boolean {
        if (title.isBlank()) return false
        val newTask = Task(title = title.trim())
        _uiState.update { currentState ->
            val updatedTasks = currentState.tasks + newTask
            currentState.copy(
                tasks = updatedTasks,
                pendingCount = updatedTasks.count { !it.isCompleted },
                pendingTitles = updatedTasks.filter { !it.isCompleted }.map { it.title }
            )
        }
        return true
    }

    fun editTask(id: String, newTitle: String): Boolean {
        if (newTitle.isBlank()) return false
        _uiState.update { currentState ->
            val updatedTasks = currentState.tasks.map {
                if (it.id == id) it.copy(title = newTitle.trim()) else it
            }
            currentState.copy(
                tasks = updatedTasks,
                pendingCount = updatedTasks.count { !it.isCompleted },
                pendingTitles = updatedTasks.filter { !it.isCompleted }.map { it.title }
            )
        }
        return true
    }

    fun deleteTask(id: String) {
        _uiState.update { currentState ->
            val updatedTasks = currentState.tasks.filter { it.id != id }
            currentState.copy(
                tasks = updatedTasks,
                pendingCount = updatedTasks.count { !it.isCompleted },
                pendingTitles = updatedTasks.filter { !it.isCompleted }.map { it.title }
            )
        }
    }

    fun toggleTaskStatus(id: String) {
        _uiState.update { currentState ->
            val updatedTasks = currentState.tasks.map {
                if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it
            }
            currentState.copy(
                tasks = updatedTasks,
                pendingCount = updatedTasks.count { !it.isCompleted },
                pendingTitles = updatedTasks.filter { !it.isCompleted }.map { it.title }
            )
        }
    }
}

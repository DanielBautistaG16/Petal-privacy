package com.petal.handsfree.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * MainViewModel - Handles UI state for MainActivity
 * 
 * Manages:
 * - Service status
 * - Error messages
 * - UI state updates
 * 
 * @author Petal Team
 * @version 1.0.0
 */
class MainViewModel : ViewModel() {

    // Service status
    private val _serviceStatus = MutableLiveData<Boolean>(false)
    val serviceStatus: LiveData<Boolean> = _serviceStatus

    // Error handling
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // Loading states
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * Update service running status
     */
    fun updateServiceStatus(isRunning: Boolean) {
        viewModelScope.launch {
            _serviceStatus.value = isRunning
        }
    }

    /**
     * Show error message
     */
    fun showError(message: String) {
        viewModelScope.launch {
            _errorMessage.value = message
        }
    }

    /**
     * Clear current error
     */
    fun clearError() {
        _errorMessage.value = null
    }

    /**
     * Set loading state
     */
    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }
}
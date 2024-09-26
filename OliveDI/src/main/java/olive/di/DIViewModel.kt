package olive.di

import androidx.lifecycle.ViewModel

abstract class DIViewModel : ViewModel() {
    init {
        addViewModelScopeInstance()
    }

    private fun addViewModelScopeInstance() {
        viewModelInstances.forEach { (type, instanceProvider) ->
            val instance = instanceProvider.get()
            instances[type] = instance
        }
    }

    override fun onCleared() {
        super.onCleared()
        removeViewModelScopeInstance()
    }

    private fun removeViewModelScopeInstance() {
        viewModelInstances.forEach { (type, _) ->
            instances.remove(type)
        }
    }
}

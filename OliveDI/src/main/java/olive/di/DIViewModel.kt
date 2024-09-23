package olive.di

import androidx.lifecycle.ViewModel

abstract class DIViewModel : ViewModel() {
    init {
        viewModelInstances.forEach { (type, instanceProvider) ->
            val instance = instanceProvider.get()
            instances[type] = instance
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelInstances.forEach { (type, _) ->
            instances.remove(type)
        }
    }
}

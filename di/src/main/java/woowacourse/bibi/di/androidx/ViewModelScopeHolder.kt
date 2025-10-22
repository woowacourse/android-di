package woowacourse.bibi.di.androidx

import androidx.lifecycle.ViewModel
import woowacourse.bibi.di.core.Container

internal class ViewModelScopeHolder : ViewModel() {
    lateinit var container: Container

    fun getOrCreate(factory: () -> Container): Container {
        if (!this::container.isInitialized) {
            container = factory()
        }
        return container
    }

    override fun onCleared() {
        super.onCleared()
        if (this::container.isInitialized) container.clear()
    }
}

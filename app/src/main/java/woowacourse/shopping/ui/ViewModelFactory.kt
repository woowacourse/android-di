package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.AppContainer

class ViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = modelClass.constructors.first()
        val params =
            constructor.parameters
                .map { param -> appContainer.resolve(param.type.kotlin) }
                .toTypedArray()

        return constructor.newInstance(*params) as T
    }
}

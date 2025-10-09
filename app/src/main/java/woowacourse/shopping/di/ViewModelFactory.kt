package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = modelClass.constructors.first()

        val params =
            constructor.parameters
                .map { param ->
                    appContainer.resolve(param.type.kotlin)
                        ?: throw IllegalArgumentException("AppContainer에 ${param.type} 의존성이 없습니다")
                }.toTypedArray()

        return constructor.newInstance(*params) as T
    }
}

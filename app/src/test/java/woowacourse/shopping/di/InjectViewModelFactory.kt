package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.fixture.TestAppContainer

class InjectViewModelFactory(
    private val container: TestAppContainer,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = modelClass.constructors.first()
        val params =
            constructor.parameters
                .map { param ->
                    container.resolve(param.type.kotlin)
                }.toTypedArray()

        return constructor.newInstance(*params) as T
    }
}

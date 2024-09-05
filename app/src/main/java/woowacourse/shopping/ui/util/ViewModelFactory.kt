package woowacourse.shopping.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.DIContainer
import kotlin.reflect.KClass

class ViewModelFactory(
    private val viewModelClass: KClass<*>,
    private val diContainer: DIContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(viewModelClass.java)) {
            diContainer.instance(viewModelClass) as T
        } else {
            throw IllegalArgumentException("잘못된 ViewModel 클래스입니다.")
        }
    }
}

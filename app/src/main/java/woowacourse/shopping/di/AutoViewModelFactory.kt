package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.di.DependencyResolver

class AutoViewModelFactory(
    private val dependencyResolver: DependencyResolver,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        modelClass.cast(dependencyResolver.create(modelClass.kotlin))
            ?: error("ViewModel 생성 결과가 요청한 타입과 다릅니다: ${modelClass.name}")
}

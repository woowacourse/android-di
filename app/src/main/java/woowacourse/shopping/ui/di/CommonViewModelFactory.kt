package woowacourse.shopping.ui.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.RepositoryContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class CommonViewModelFactory(
    private val repositoryContainer: RepositoryContainer
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = modelClass.kotlin.primaryConstructor
            ?: error("주 생성자를 찾을 수 없음")

        val repositories =
            constructor.parameters.associateWith { parameter ->
                val parameterType = parameter.type.classifier as KClass<*>
                repositoryContainer.getInstance(parameterType)
            }

        return constructor.callBy(repositories)
    }
}

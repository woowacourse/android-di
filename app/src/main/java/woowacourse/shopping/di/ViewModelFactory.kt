package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class ViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor =
            modelClass.kotlin.primaryConstructor
                ?: throw IllegalArgumentException(
                    "${modelClass.simpleName}에 주 생성자가 없습니다",
                )

        val args =
            constructor.parameters.associateWith { param ->
                val clazz =
                    param.type.classifier as? KClass<*>
                        ?: throw IllegalArgumentException("${param.name} 타입 정보를 가져올 수 없습니다")
                appContainer.resolve(clazz)
            }

        return constructor.callBy(args)
    }
}

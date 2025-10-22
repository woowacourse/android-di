package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.fixture.TestAppContainer

class InjectViewModelFactory(
    private val container: TestAppContainer,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClazz = modelClass.kotlin

        val constructor =
            kClazz.primaryConstructor
                ?: throw IllegalArgumentException("${modelClass.simpleName}에 기본 생성자가 없습니다")

        val args =
            constructor.parameters.associateWith { param ->
                val clazz =
                    param.type.classifier as? KClass<*>
                        ?: throw IllegalArgumentException("${param.name} 타입 정보를 가져올 수 없습니다")
                container.resolve(clazz) // 여기서 null이면 예외 발생하도록 resolve 구현 권장
            }

        return constructor.callBy(args) as T
    }
}

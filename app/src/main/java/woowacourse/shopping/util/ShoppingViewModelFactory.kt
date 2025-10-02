package woowacourse.shopping.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.AppContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class ShoppingViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClass: KClass<out ViewModel> = modelClass.kotlin

        // 생성할 ViewModel 클래스의 주 생성자 정보
        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalArgumentException("ViewModel은 주 생성자를 가지고 있지 않습니다")

        // 생성자 파라미터 타입에 맞춰 AppContainer에서 가져오기
        val parameterTypes = constructor.parameters.map { it.type.classifier as KClass<*> }

        // appContainer에서 생성된 의존성 인스턴스 가져오기
        val dependencies = parameterTypes.map { appContainer.get(it) }
        val argumentMap = constructor.parameters.zip(dependencies).toMap()

        return constructor.callBy(argumentMap) as T
    }
}

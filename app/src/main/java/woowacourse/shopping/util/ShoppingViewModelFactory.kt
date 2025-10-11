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
        val viewModelKClass: KClass<out ViewModel> = modelClass.kotlin

        @Suppress("UNCHECKED_CAST")
        return createViewModel(viewModelKClass as KClass<T>)
    }

    private fun <T : ViewModel> createViewModel(viewModelKClass: KClass<T>): T {
        // 생성할 ViewModel 클래스의 주 생성자 정보
        val primaryConstructor =
            viewModelKClass.primaryConstructor
                ?: throw IllegalArgumentException("ViewModel은 주 생성자를 가지고 있지 않습니다")

        // 생성자 파라미터 타입에 맞춰 AppContainer에서 가져오기
        val parameterTypes = primaryConstructor.parameters.map { it.type.classifier as KClass<*> }

        // appContainer에서 생성된 의존성 인스턴스 가져오기
        val dependencyInstances = parameterTypes.map { appContainer.get(it) }
        val constructorArguments = primaryConstructor.parameters.zip(dependencyInstances).toMap()

        // 리플랙션을 이용해 ViewModel 인스턴스 생성 및 반환
        return primaryConstructor.callBy(constructorArguments)
    }
}

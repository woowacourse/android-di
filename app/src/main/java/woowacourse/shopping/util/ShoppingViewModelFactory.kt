package woowacourse.shopping.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.AppContainer
import woowacourse.shopping.di.MyInjector
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

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
        val viewModel = primaryConstructor.callBy(constructorArguments)

        // 필드 주입
        injectField(viewModel)

        return viewModel
    }

    private fun injectField(viewModel: ViewModel) {
        val viewModelKClass = viewModel::class

        // 해당 클래스에 있는 모든 프로퍼티를 가져옴
        viewModelKClass.memberProperties.forEach { property ->
            // KMutableProperty1: 프로퍼티의 값을 변경할 수 있는 프로퍼티
            if (property is KMutableProperty1 && property.hasAnnotation<MyInjector>()) {
                val dependencyClass =
                    property.returnType.classifier as? KClass<*>
                        ?: return@forEach
                val dependency = appContainer.get(dependencyClass)

                // isAccessible: 프로퍼티의 접근 지시자를 변경할 수 있는 속성
                property.isAccessible = true
                // setter.call: 프로퍼티의 setter를 호출하여 값을 설정
                property.setter.call(viewModel, dependency)
            }
        }
    }
}

package woowacourse.shopping.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.di.AppContainer
import com.example.di.annotation.Inject
import com.example.di.annotation.Qualifier
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

    private fun <T : ViewModel> createViewModel(kClass: KClass<T>): T {
        val primaryConstructor =
            kClass.primaryConstructor
                ?: throw IllegalArgumentException("ViewModel은 주 생성자가 있어야 합니다")

        // 생성자 파라미터별로 Qualifier 감지
        val constructorArgs =
            primaryConstructor.parameters.associateWith { param ->
                val paramType = param.type.classifier as KClass<*>

                val qualifierKClass =
                    param.annotations
                        .map { it.annotationClass }
                        .firstOrNull { it.hasAnnotation<Qualifier>() }

                if (qualifierKClass != null) {
                    appContainer.get(paramType, qualifierKClass)
                } else {
                    appContainer.get(paramType)
                }
            }

        val viewModel = primaryConstructor.callBy(constructorArgs)
        injectField(viewModel)

        return viewModel
    }

    private fun injectField(viewModel: ViewModel) {
        val viewModelKClass = viewModel::class

        // 해당 클래스에 있는 모든 프로퍼티를 가져옴
        viewModelKClass.memberProperties.forEach { property ->
            // KMutableProperty1: 프로퍼티의 값을 변경할 수 있는 프로퍼티
            if (property is KMutableProperty1 && property.hasAnnotation<Inject>()) {
                val dependencyClass =
                    property.returnType.classifier as? KClass<*>
                        ?: return@forEach
                val qualifierKClass =
                    property.annotations
                        .map { it.annotationClass }
                        .firstOrNull { it.hasAnnotation<Qualifier>() }

                val dependency =
                    if (qualifierKClass != null) {
                        appContainer.get(dependencyClass, qualifierKClass)
                    } else {
                        appContainer.get(dependencyClass)
                    }

                // isAccessible: 프로퍼티의 접근 지시자를 변경할 수 있는 속성
                property.isAccessible = true
                // setter.call: 프로퍼티의 setter를 호출하여 값을 설정
                property.setter.call(viewModel, dependency)
            }
        }
    }
}

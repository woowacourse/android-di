package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

class DependencyInjector(
    private val appContainer: AppContainer,
) {
    inline fun <reified VM : ViewModel> inject(): VM {
        val viewModelKClass: KClass<VM> = VM::class
        val primaryConstructor = viewModelKClass.primaryConstructor

        // 생성자 파라미터 → 의존성 주입
        return if (primaryConstructor != null) {
            val constructorParams: List<KParameter> = primaryConstructor.parameters
            val parameters: List<Any?> = constructorParams.map(::injectInstance)
            primaryConstructor.call(*parameters.toTypedArray())
        } else {
            // 기본 생성자가 있는 경우
            viewModelKClass.createInstance()
        }
    }

    // Parameter에 해당하는 Instance를 appContainer에서 찾아 반환
    fun injectInstance(kParameter: KParameter): Any? {
        val classifier: KClassifier = kParameter.type.classifier ?: return null
        val paramKClass = classifier as? KClass<*> ?: return null

        // appContainer의 모든 프로퍼티를 탐색
        val matchedProperty: KProperty1<out AppContainer, *>? =
            appContainer::class
                .memberProperties
                .firstOrNull { prop -> prop.returnType.classifier == paramKClass }

        // 매칭되는 프로퍼티의 값 반환
        return matchedProperty?.getter?.call(appContainer)
    }
}

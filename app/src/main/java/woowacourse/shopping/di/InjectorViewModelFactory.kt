package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.annotation.Inject
import java.lang.reflect.Field
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

class InjectorViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModelKClass: KClass<T> = modelClass.kotlin
        // ViewModel 인스턴스 생성
        val viewModelInstance: T = viewModelKClass.createInstance()

        // @Inject 어노테이션이 붙은 fields 탐색
        viewModelKClass.declaredMemberProperties.forEach { prop: KProperty1<T, *> ->
            val field: Field? = prop.javaField
            if (field?.isAnnotationPresent(Inject::class.java) == true) {
                // appContainer에서 실제 프로퍼티 인스턴스를 찾아 주입
                val dependencyInstance: Any? = injectInstance(field.type)
                dependencyInstance?.let {
                    field.isAccessible = true
                    field.set(viewModelInstance, dependencyInstance)
                }
            }
        }
        return viewModelInstance
    }

    // Class에 해당하는 Instance를 appContainer에서 찾아 반환
    private fun injectInstance(clazz: Class<*>): Any? {
        val matchedProperty: KProperty1<out AppContainer, *>? = getMatchedProperty(clazz)
        return matchedProperty?.getter?.call(appContainer)
    }

    // appContainer의 모든 프로퍼티를 탐색
    private fun getMatchedProperty(clazz: Class<*>): KProperty1<out AppContainer, *>? {
        val matchedProperty: KProperty1<out AppContainer, *>? =
            appContainer::class
                .memberProperties
                .firstOrNull { prop -> (prop.returnType.classifier as? KClass<*>)?.java == clazz }
        return matchedProperty
    }
}

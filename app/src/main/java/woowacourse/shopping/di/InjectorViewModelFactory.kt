package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.annotation.Inject
import woowacourse.shopping.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
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
            val field = prop.javaField ?: return@forEach
            val qualifier: Qualifier? = prop.findAnnotation<Qualifier>()
            if (field.isAnnotationPresent(Inject::class.java)) {
                val dependencyClass: KClass<out Any> = qualifier?.value // @Qualifier에 지정된 구현체 클래스
                    ?: field.type.kotlin // 기본적으로 인터페이스 타입 사용

                val dependencyInstance = appContainer.getInstance(dependencyClass)
                field.isAccessible = true
                field.set(viewModelInstance, dependencyInstance)
            }
        }
        return viewModelInstance
    }
}

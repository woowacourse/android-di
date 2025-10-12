package woowacourse.shopping.di

import android.app.Activity
import androidx.lifecycle.ViewModel
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.typeOf

class DependencyProvider(
    private val module: DependencyModule,
) {
    private val dependencyGetters: MutableMap<KType, () -> Any> = mutableMapOf()

    init {
        module::class.memberProperties.forEach { property: KProperty1<out DependencyModule, *> ->
            if (property.findAnnotation<Dependency>() == null) return@forEach
            dependencyGetters[property.returnType] = {
                property.getter.call(module) ?: error("${property}의 getter가 null을 반환했습니다.")
            }
        }
    }

    fun injectViewModels(activity: Activity) {
        activity::class.memberProperties.forEach { property: KProperty1<out Activity, *> ->
            if (property.findAnnotation<InjectableViewModel>() != null && property is KMutableProperty1) {
                if (!property.returnType.isSubtypeOf(typeOf<ViewModel>())) error("${property.returnType}은(는) ViewModel이 아닙니다.")
                val kClass: KClass<*> = property.returnType.classifier as KClass<*>
                val viewModel: Any = kClass.createInstance()
                inject(viewModel)
                property.setter.call(activity, viewModel)
            }
        }
    }

    private fun dependency(key: KType): Any = dependencyGetters[key]?.invoke() ?: error("${key}에 대한 의존성이 정의되지 않았습니다.")

    private fun inject(target: Any) {
        val kClass = target::class
        kClass.memberProperties.forEach { property: KProperty1<out Any, *> ->
            if (property.findAnnotation<Inject>() != null && property is KMutableProperty1) {
                property.setter.call(target, dependency(property.returnType))
            }
        }
    }
}

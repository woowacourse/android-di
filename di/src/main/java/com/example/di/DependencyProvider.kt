package com.example.di

import android.app.Activity
import androidx.lifecycle.ViewModel
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.typeOf

object DependencyProvider {
    private val dependencyGetters: MutableMap<Identifier, () -> Any> = mutableMapOf()

    fun initialize(vararg module: Module) {
        module.forEach(::initialize)
    }

    private fun initialize(module: Module) {
        module::class.memberProperties.forEach { property: KProperty1<out Module, *> ->
            if (property.findAnnotation<Dependency>() == null) return@forEach

            val identifier = Identifier.of(property)
            dependencyGetters[identifier] = {
                property.getter.call(module) ?: error("${property}의 getter가 null을 반환했습니다.")
            }
        }
    }

    fun dependency(identifier: Identifier): Any = dependencyGetters[identifier]?.invoke() ?: error("${identifier}에 대한 의존성이 정의되지 않았습니다.")

    fun injectViewModels(activity: Activity) {
        activity::class.memberProperties.forEach { property: KProperty1<out Activity, *> ->
            if (property.findAnnotation<InjectableViewModel>() == null) return@forEach
            if (!property.returnType.isSubtypeOf(typeOf<ViewModel>())) error("${property.returnType}은(는) ViewModel이 아닙니다.")

            val kClass: KClass<*> = property.returnType.classifier as KClass<*>
            val viewModel: Any = kClass.createInstance()
            injectFields(viewModel)

            val mutableProperty = property.toMutableProperty()
            mutableProperty.setter.call(activity, viewModel)
        }
    }

    fun injectFields(target: Any) {
        target::class.memberProperties.forEach { property: KProperty1<out Any, *> ->
            if (property.findAnnotation<Inject>() == null) return@forEach

            val identifier = Identifier.of(property)
            val mutableProperty = property.toMutableProperty()
            mutableProperty.setter.call(target, dependency(identifier))
        }
    }

    private fun <T, V> KProperty1<T, V>.toMutableProperty(): KMutableProperty1<T, V> =
        this as? KMutableProperty1<T, V> ?: error("${this}은(는) 가변 프로퍼티가 아닙니다.")
}

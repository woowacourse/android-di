package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

object AutoDIManager {
    val dependencies: MutableMap<KClass<*>, Any> = mutableMapOf()

    inline fun <reified T : Any> registerDependency(dependency: Any) {
        val clazz = T::class
        dependencies[clazz] = dependency
    }

    inline fun <reified VM : ViewModel> createViewModelFactory(): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                val viewModelInstance = createAutoDIInstance<VM>()
                injectField<VM>(viewModelInstance)
                viewModelInstance
            }
        }
    }

    inline fun <reified VM : ViewModel> injectField(viewModelInstance: VM) {
        val properties = VM::class.declaredMemberProperties
        val mutableProperties = properties.filterIsInstance<KMutableProperty<*>>()
        for (mutableProperty in mutableProperties) {
            if (mutableProperty.findAnnotation<FieldInject>() != null) {
                mutableProperty.isAccessible = true
                mutableProperty.setter.call(viewModelInstance, "New Value")
            }
        }
    }

    inline fun <reified T : Any> createAutoDIInstance(): T {
        val clazz = T::class
        val constructor =
            clazz.primaryConstructor ?: return clazz.createInstance()

        val args =
            constructor.parameters.associateWith { parameter ->
                dependencies[parameter.type.jvmErasure]
            }

        return constructor.callBy(args)
    }
}

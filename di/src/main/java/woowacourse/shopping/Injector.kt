package woowacourse.shopping

import woowacourse.shopping.annotation.Inject
import woowacourse.shopping.annotation.Qualifier
import woowacourse.shopping.container.DiContainer
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class Injector(private val container: DiContainer) {

    fun <T : Any> inject(clazz: KClass<T>, annotations: List<Annotation> = emptyList()): T {
        container.getSavedInstanceOf(annotations, clazz)?.let {
            return it
        }
        val instance = getInstanceOf(clazz, annotations) ?: createInstanceOf(clazz)
        injectFields(clazz, instance)

        container.saveInstance(annotations, clazz, instance)
        return instance
    }

    private fun <T : Any> getInstanceOf(clazz: KClass<out T>, annotations: List<Annotation>): T? {
        val qualifier = annotations.firstOrNull {
            it.annotationClass.hasAnnotation<Qualifier>()
        }
        return container.getProviderOf(clazz, qualifier)
    }

    private fun getArguments(func: KFunction<*>): Array<Any> {
        val args = func.parameters.map {
            inject(it.type.jvmErasure, it.annotations)
        }.toTypedArray()
        return args
    }

    private fun <T : Any> createInstanceOf(implementationClass: KClass<out T>): T {
        val constructor = implementationClass.primaryConstructor
            ?: throw NullPointerException("주입할 클래스의 주생성자가 존재하지 않습니다.")

        if (constructor.parameters.isEmpty()) return implementationClass.createInstance()

        val args = getArguments(constructor)
        return constructor.call(*args)
    }

    fun <T : Any> injectFields(clazz: KClass<out T>, instance: T) {
        clazz.declaredMemberProperties.forEach {
            if (it.hasAnnotation<Inject>() && it is KMutableProperty<*>) {
                it.isAccessible = true
                it.setter.call(instance, inject(it.returnType.jvmErasure, it.annotations))
            }
        }
    }

    fun addModule(module: Module) {
        container.registerModule(module)
    }

    fun releaseDependency(dependency: String) {
        container.releaseDependency(dependency)
    }
}

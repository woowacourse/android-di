package woowacourse.shopping

import woowacourse.shopping.annotation.Inject
import woowacourse.shopping.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class Injector(private val container: AppContainer) {

    fun <T : Any> inject(clazz: KClass<T>): T {
        container.getSavedInstanceOf(clazz)?.let { return it }

        val instance = getInstanceOf(clazz) ?: createInstanceOf(clazz)
        injectFields(clazz, instance)

        container.saveInstance(clazz, instance)
        return instance
    }

    private fun <T : Any> getInstanceOf(clazz: KClass<out T>): T? {
        val provider = container.getProviderOf(clazz)
        return provider?.let {
            val args = getArguments(it)
            it.call(*args)
        }
    }

    private fun getArguments(func: KFunction<*>): Array<Any> {
        val args = func.parameters.map {
            inject(it.getImplementationClass())
        }.toTypedArray()
        return args
    }

    private fun KParameter.getImplementationClass(): KClass<*> {
        val implementationClass = getImplementationClassFromAnnotations(annotations)
        return implementationClass ?: type.jvmErasure
    }

    private fun getImplementationClassFromAnnotations(annotations: List<Annotation>): KClass<*>? {
        val qualifier =
            annotations.firstOrNull { it.annotationClass == Qualifier::class } as? Qualifier
        return qualifier?.let {
            val packageName = it.name
            Class.forName(packageName).kotlin
        }
    }

    private fun <T : Any> createInstanceOf(implementationClass: KClass<out T>): T {
        val constructor = implementationClass.primaryConstructor
            ?: throw NullPointerException("주입할 클래스의 주생성자가 존재하지 않습니다.")

        if (constructor.parameters.isEmpty()) return implementationClass.createInstance()

        val args = getArguments(constructor)
        return constructor.call(*args)
    }

    private fun <T : Any> injectFields(clazz: KClass<out T>, instance: T) {
        clazz.declaredMemberProperties.forEach {
            if (it.hasAnnotation<Inject>() && it is KMutableProperty<*>) {
                it.isAccessible = true
                it.setter.call(instance, inject(it.getImplementationClass()))
            }
        }
    }

    private fun <T : Any> KProperty1<T, *>.getImplementationClass(): KClass<*> {
        val implementationClass = getImplementationClassFromAnnotations(annotations)
        return implementationClass ?: returnType.jvmErasure
    }
}

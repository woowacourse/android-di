package woowacourse.shopping.di.inject

import woowacourse.shopping.di.annotation.CustomInject
import woowacourse.shopping.di.annotation.Qualifier
import woowacourse.shopping.di.container.DependencyContainer
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class CustomInjector {

    fun <T : Any> inject(kClass: KClass<T>): T {
        return (
            DependencyContainer.getInstance(kClass) ?: createInstanceFromKClass(
                kClass,
            )
            ) as T
    }

    private fun <T : Any> createInstanceFromKClass(kClass: KClass<T>): T {
        val constructor = kClass.primaryConstructor
            ?: throw IllegalArgumentException("주 생성자를 찾을 수 없습니다.")

        val parameterValues =
            constructor.parameters.associateWith {
                findPropertyGetValue(
                    it.type.jvmErasure,
                    it.annotations,
                )
            }

        return constructor.callBy(parameterValues).apply { injectFields(this) }
    }

    private fun <T : Any> injectFields(instance: T) {
        instance::class.declaredMemberProperties
            .filter { it.hasAnnotation<CustomInject>() }
            .forEach { prop ->
                prop.isAccessible = true
                val propertyType = prop.returnType.jvmErasure
                val value = findPropertyGetValue(propertyType, prop.annotations)
                (prop as KMutableProperty<*>).setter.call(instance, value)
            }
    }

    private fun <T : Any> findPropertyGetValue(
        kClass: KClass<T>,
        annotations: List<Annotation>,
    ): Any {
        val annotationWithQualifier =
            annotations.filter { it.annotationClass.java.isAnnotationPresent(Qualifier::class.java) }
        return DependencyContainer.getInstance(kClass, annotationWithQualifier)
            ?: createInstanceFromKClass(
                kClass,
            )
    }
}

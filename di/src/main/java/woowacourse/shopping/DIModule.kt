package woowacourse.shopping

import woowacourse.shopping.annotation.Binds
import woowacourse.shopping.annotation.Inject
import woowacourse.shopping.annotation.Provides
import woowacourse.shopping.annotation.Qualifier2
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

open class DIModule(private val parentModule: DIModule?) {
    fun <T : Any> inject(clazz: KClass<T>, qualifier: Annotation? = null): T {
        val how = getHowToImplement(clazz, qualifier)

        var instance: T? = if (how == null) {
            parentModule?.inject(clazz, qualifier)
        } else {
            createInstanceBy(how, clazz)
        }

        if (instance == null) instance = createInstance(clazz)
        injectFields(clazz, instance)

        return instance
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> getHowToImplement(
        clazz: KClass<T>,
        qualifier: Annotation?,
    ): KCallable<T>? {
        var members = this::class.declaredMembers.filter {
            it.returnType.jvmErasure.isSubclassOf(clazz)
        }

        if (qualifier != null) {
            members = members.filter { it.annotations.contains(qualifier) }
        }

        return when (members.size) {
            0 -> null
            1 -> members.first() as KCallable<T>
            else -> throw IllegalStateException("주입을 위한 방법을 특정할 수 없습니다.")
        }
    }

    private fun <T : Any> createInstance(clazz: KClass<out T>): T {
        val constructor = clazz.primaryConstructor
            ?: throw NullPointerException("주입할 클래스의 주생성자가 존재하지 않습니다.")

        if (constructor.parameters.isEmpty()) return clazz.createInstance()

        val args = getArguments(constructor)
        return constructor.call(*args)
    }

    private fun getArguments(func: KFunction<*>): Array<Any> {
        val args = func.parameters.map { parameter ->
            if (parameter.kind == KParameter.Kind.INSTANCE) {
                this@DIModule
            } else {
                inject(
                    parameter.type.jvmErasure,
                    parameter.annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier2>() },
                )
            }
        }.toTypedArray()
        return args
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> createInstanceBy(
        how: KCallable<T>,
        clazz: KClass<T>,
    ): T? {
        var instance: T? = null
        if (how.hasAnnotation<Binds>()) {
            val implementationClazz = how.returnType.jvmErasure
            check(implementationClazz.isSubclassOf(clazz)) { "Binds의 반환 타입은 주입하고자 하는 타입의 하위 타입이어야 합니다." }
            instance = createInstance(implementationClazz) as T
        } else if (how.hasAnnotation<Provides>()) {
            how.isAccessible = true
            val args = getArguments(how as KFunction<T>)
            instance = how.call(*args)
        }
        return instance
    }

    fun <T : Any> injectFields(clazz: KClass<out T>, instance: T) {
        clazz.declaredMemberProperties.forEach { property ->
            if (property.hasAnnotation<Inject>() && property is KMutableProperty<*>) {
                property.isAccessible = true
                property.setter.call(
                    instance,
                    inject(
                        property.returnType.jvmErasure,
                        property.annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier2>() },
                    ),
                )
            }
        }
    }
}

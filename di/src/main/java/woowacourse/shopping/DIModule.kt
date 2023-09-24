package woowacourse.shopping

import woowacourse.shopping.annotation.Binds
import woowacourse.shopping.annotation.Inject
import woowacourse.shopping.annotation.Provides
import woowacourse.shopping.annotation.Qualifier2
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

open class DIModule {

    fun <T : Any> inject(clazz: KClass<T>, qualifier: Annotation? = null): T {
        val how = getHowToImplement(clazz, qualifier)

        val instance: T? = if (how == null) {
            createInstance(clazz)
        } else {
            createInstanceBy(how, clazz)
        }

        check(instance != null) { "주입할 인스턴스를 가져오는데 실패했습니다." }
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

    private fun <T : Any> createInstance(implementationClass: KClass<out T>): T {
        val constructor = implementationClass.primaryConstructor
            ?: throw NullPointerException("주입할 클래스의 주생성자가 존재하지 않습니다.")

        if (constructor.parameters.isEmpty()) return implementationClass.createInstance()

        val args = getArguments(constructor)
        return constructor.call(*args)
    }

    private fun getArguments(func: KFunction<*>): Array<Any> {
        val args = func.parameters.map { parameter ->
            inject(
                parameter.type.jvmErasure,
                parameter.annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier2>() },
            )
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

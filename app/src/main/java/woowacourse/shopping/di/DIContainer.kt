package woowacourse.shopping.di

import androidx.room.Dao
import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.module.DIModule
import javax.inject.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

object DIContainer {
    private val singletonInstances = mutableMapOf<QualifierKey<*>, Any>()
    private val interfaceMappings = mutableMapOf<QualifierKey<*>, KClass<*>>()

    fun loadModule(module: DIModule) {
        module.register(this)
    }

    fun <T : Any> registerInstance(
        interfaceClass: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        val key = QualifierKey(interfaceClass, qualifier)
        singletonInstances[key] = instance
    }

    fun <T : Any> registerInterfaceMapping(
        interfaceClass: KClass<T>,
        implementationClass: KClass<out T>,
        qualifier: KClass<out Annotation>? = null,
    ) {
        val key = QualifierKey(interfaceClass, qualifier)
        interfaceMappings[key] = implementationClass
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getInstance(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        val key = QualifierKey(type, qualifier)
        return singletonInstances[key] as? T
            ?: throw IllegalArgumentException("${type.simpleName}에 대한 인스턴스가 등록되지 않았습니다.")
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        val key = QualifierKey(type, qualifier)
        singletonInstances[key]?.let { return it as T }

        val implementationType = interfaceMappings[key] ?: type

        if (implementationType.java.isInterface && implementationType.hasAnnotation<Dao>()) {
            return singletonInstances[key] as? T
                ?: throw IllegalArgumentException("${type.simpleName} DAO 인스턴스를 찾을 수 없습니다. RoomDatabase가 초기화되지 않았습니다.")
        }

        val injectConstructor =
            implementationType.constructors.firstOrNull { constructor ->
                constructor.annotations.any { it is Inject }
            }

        val constructor =
            injectConstructor ?: implementationType.primaryConstructor
                ?: throw IllegalArgumentException("${implementationType.simpleName} 클래스의 인스턴스를 생성할 수 없습니다. 기본 생성자도 없습니다.")

        val parameters =
            constructor.parameters.map { parameter ->
                val parameterType = parameter.type.jvmErasure
                val qualifierAnnotation = parameter.annotations.filterIsInstance<Qualifier>().firstOrNull()
                val qualifierValue = qualifierAnnotation?.annotationClass
                resolve(parameterType, qualifierValue)
            }

        val instance = constructor.call(*parameters.toTypedArray()) as T
        registerInstance(type, instance, qualifier)

        return instance
    }
}

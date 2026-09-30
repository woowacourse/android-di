package woowacourse.shopping.di

import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.annotation.Qualifier
import kotlin.collections.forEach
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

object DIContainer {
    private val instances = mutableMapOf<DependencyKey, Any>()
    private val creating = mutableSetOf<DependencyKey>()
    private val bindings = mutableMapOf<DependencyKey, KClass<*>>()

    fun <T : Any> bind(
        type: KClass<T>,
        implementation: KClass<out T>,
        qualifier: KClass<out Annotation>? = null,
    ) {
        bindings[DependencyKey(type, qualifier)] = implementation
    }

    fun <T : Any> bindInstance(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        instances[DependencyKey(type, qualifier)] = instance
    }

    fun <T : Any> createInstance(
        modelClass: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        val key = DependencyKey(modelClass, qualifier)
        if (instances[key] != null) return instances[key] as T

        if (key in creating) {
            throw IllegalStateException("순환 의존성이 발생했어요: $modelClass")
        }

        creating += key

        try {
            val implementationClass =
                if (modelClass.java.isInterface) {
                    findImplementation(modelClass, qualifier)
                } else {
                    modelClass
                }
            val constructor = implementationClass.primaryConstructor ?: throw IllegalArgumentException("생성자를 찾을 수 없어요 : $modelClass")
            val dependencies = findDependencies(constructor)
            val instance = constructor.call(*dependencies.toTypedArray())
            instances[DependencyKey(modelClass, qualifier)] = instance
            if (modelClass != implementationClass) instances[DependencyKey(implementationClass, qualifier)] = instance
            return instance
        } finally {
            creating -= key
        }
    }

    fun clear() {
        instances.clear()
        creating.clear()
        bindings.clear()
    }

    fun <T : Any> injectFields(instance: T) {
        instance::class
            .memberProperties
            .filter { property ->
                property.annotations.any { it.annotationClass == Inject::class } &&
                    property is KMutableProperty1<*, *>
            }.forEach { property ->
                val mutableProperty = property as KMutableProperty1<*, *>
                val dependencyType = property.returnType.classifier as KClass<*>
                val qualifier =
                    property.annotations
                        .firstOrNull { annotation ->
                            annotation.annotationClass.annotations.any {
                                it.annotationClass == Qualifier::class
                            }
                        }?.annotationClass
                val dependency = createInstance(dependencyType, qualifier)
                mutableProperty.setter.call(instance, dependency)
            }
    }

    private fun <T : Any> findImplementation(
        modelClass: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): KClass<out T> {
        if (qualifier == null) {
            val candidates =
                bindings
                    .filterKeys { key -> key.type == modelClass }
                    .values

            if (candidates.size > 1) {
                throw IllegalArgumentException("같은 타입의 구현체가 둘 이상이므로 Qualifier가 필요해요: $modelClass")
            }
        }

        val key = DependencyKey(modelClass, qualifier)

        return bindings[key] as? KClass<out T>
            ?: throw IllegalArgumentException("등록되지 않은 Qualifier예요: type = $modelClass, qualifier = $qualifier")
    }

    private fun <T : Any> findDependencies(constructor: KFunction<T>): List<Any> {
        val types = constructor.parameters.map { it.type.classifier as KClass<*> }
        return getInstances(types)
    }

    private fun getInstances(types: List<KClass<*>>): List<Any> =
        types.map { type ->
            createInstance(type)
        }
}

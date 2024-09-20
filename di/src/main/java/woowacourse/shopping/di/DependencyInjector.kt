package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object DependencyInjector {
    private const val ERROR_DEPENDENCY_NOT_FOUND =
        "Couldn't find dependency from Dependency Container :"

    lateinit var dependencyContainer: DependencyContainer
        private set

    fun initDependencyContainer(container: DependencyContainer) {
        dependencyContainer = container
    }

    fun <T : Any> createInstanceFromConstructor(
        modelClass: Class<T>,
        qualifier: String? = null
    ): T {
        val kClass: KClass<T> = modelClass.kotlin
        val targetInstance: T = checkClassTypeThenGetInstance(kClass, qualifier)
        setDependencyOfProperties(kClass, targetInstance)
        dependencyContainer.setInstance(kClass, targetInstance, qualifier)
        return targetInstance
    }

    private fun <T : Any> checkClassTypeThenGetInstance(
        kClass: KClass<T>,
        qualifier: String?,
    ): T {
        val objectInstance: T? = kClass.objectInstance
        val primaryConstructor: KFunction<T>? = kClass.primaryConstructor
        return if (primaryConstructor == null) {
            getInstanceFromDependencyContainer(kClass, qualifier)
        } else if (objectInstance != null) {
            objectInstance
        } else {
            primaryConstructor.isAccessible = true
            getInstanceFromConstructor(primaryConstructor)
        }
    }

    private fun <T : Any> getInstanceFromDependencyContainer(
        kClass: KClass<T>,
        qualifier: String?,
    ): T {
        val instanceFromContainer = dependencyContainer.getInstance<T>(kClass, qualifier)
        return if (instanceFromContainer != null) {
            instanceFromContainer
        } else {
            val implementKClass =
                requireNotNull(dependencyContainer.getImplement<T>(kClass, qualifier)) {
                    "$ERROR_DEPENDENCY_NOT_FOUND $kClass to $qualifier"
                }
            createInstanceFromConstructor(implementKClass.java, qualifier)
        }
    }

    private fun <T : Any> getInstanceFromConstructor(primaryConstructor: KFunction<T>): T {
        val constructorParams = getDependencyOfParameters(primaryConstructor.parameters)
        return primaryConstructor.call(*constructorParams)
    }

    private fun getDependencyOfParameters(parameters: List<KParameter>): Array<Any> {
        return parameters.map { param ->
            val classifier = param.type.classifier as KClass<*>
            if (param.hasAnnotation<ParamInject>()) {
                createInstanceFromConstructor(classifier.java)
            }
        }.toTypedArray()
    }

    private fun <T : Any> setDependencyOfProperties(
        kClass: KClass<T>,
        targetInstance: T,
    ) {
        kClass.declaredMemberProperties.forEach { kProperty ->
            if (checkNeedsDependencyInject(kProperty)) {
                kProperty.isAccessible = true
                val classifier = kProperty.returnType.classifier as KClass<*>
                val qualifier: String? = kProperty.findAnnotation<Qualifier>()?.name
                val dependency: Any = createInstanceFromConstructor(classifier.java, qualifier)
                (kProperty as KMutableProperty<*>).setter.call(targetInstance, dependency)
            }
        }
    }

    private fun checkNeedsDependencyInject(kProperty: KProperty<*>): Boolean =
        kProperty.hasAnnotation<FieldInject>() && kProperty.isLateinit && kProperty is KMutableProperty
}

package woowacourse.shopping.ui.util

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object DependencyInjector {
    lateinit var dependencyContainer: DependencyContainer
        private set

    fun initDependencyContainer(container: DependencyContainer) {
        dependencyContainer = container
    }

    fun <T : Any> createInstanceFromConstructor(modelClass: Class<T>): T {
        val kClass: KClass<T> = modelClass.kotlin
        val targetInstance: T
        val primaryConstructor = kClass.primaryConstructor

        if (primaryConstructor == null) {
            val instanceFromContainer = dependencyContainer.getInstance<T>(kClass)
            if (instanceFromContainer != null) {
                targetInstance = instanceFromContainer
            } else {
                val implementKClass = requireNotNull(dependencyContainer.getImplement<T>(kClass))
                targetInstance = createInstanceFromConstructor(implementKClass.java)
            }
        } else {
            val constructorParams = getDependencyOfParameters(primaryConstructor.parameters)
            targetInstance = primaryConstructor.call(*constructorParams)

            setDependencyOfProperties(kClass, targetInstance)
        }

        dependencyContainer.setInstance(kClass, targetInstance)
        return targetInstance
    }

    private fun getDependencyOfParameters(parameters: List<KParameter>): Array<Any> {
        return parameters.map { param ->
            val classifier = param.type.classifier as KClass<*>
            createInstanceFromConstructor(classifier.java)
        }.toTypedArray()
    }

    private fun <T : Any> setDependencyOfProperties(kClass: KClass<T>, targetInstance: T) {
        kClass.declaredMemberProperties.forEach { kProperty ->
            if (checkNeedsDependencyInject(kProperty)) {
                val isAccessible = kProperty.isAccessible
                val classifier = kProperty.returnType.classifier as KClass<*>
                val dependency: Any = createInstanceFromConstructor(classifier.java)
                if (!isAccessible) {
                    kProperty.isAccessible = true
                }
                (kProperty as KMutableProperty<*>).setter.call(targetInstance, dependency)
            }
        }
    }

    private fun checkNeedsDependencyInject(kProperty: KProperty<*>): Boolean =
        kProperty.hasAnnotation<FieldInject>() && kProperty.isLateinit && kProperty is KMutableProperty
}

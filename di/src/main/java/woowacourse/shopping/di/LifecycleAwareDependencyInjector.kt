package woowacourse.shopping.di

import androidx.lifecycle.LifecycleOwner
import javax.inject.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class LifecycleAwareDependencyInjector {
    private var lifecycleOwner: LifecycleOwner? = null

    lateinit var dependencyContainer: LifecycleDependencyContainer
        private set

    fun initDependencyContainer(container: LifecycleDependencyContainer) {
        dependencyContainer = container
    }

    fun <T : Any> createInstanceWithLifecycle(
        modelClass: KClass<T>,
        lifecycleOwner: LifecycleOwner,
    ): T {
        this.lifecycleOwner = lifecycleOwner
        return createInstance(modelClass)
    }

    fun <T : Any> createInstance(
        modelClass: KClass<T>,
        qualifier: AnnotationQualifier = null,
        lifecycleAware: LifecycleAwareAnnotation = null,
    ): T {
        // 라이프사이클은 인스턴스화 된 안드로이드 컴포넌트로부터 획득할 수 있다!
        // 그러니 먼저 타겟 인스턴스를 받아야한다.
        val targetInstance: T = checkClassTypeThenGetInstance(modelClass, qualifier, lifecycleAware)
        setLifecycleOwner(modelClass, targetInstance)

        setDependencyOfProperties(modelClass, targetInstance)
        if (lifecycleAware == null) {
            dependencyContainer.setInstance(modelClass, targetInstance, qualifier)
        } else {
            dependencyContainer.setInstanceWithinLifecycle(
                modelClass,
                targetInstance,
                qualifier,
                lifecycleOwner,
                lifecycleAware
            )
        }
        return targetInstance
    }

    private fun <T : Any> setLifecycleOwner(modelClass: KClass<T>, targetInstance: T) {
        if (lifecycleOwner == null) {
            if (LifecycleOwner::class.java.isAssignableFrom(modelClass.java)) {
                lifecycleOwner = (targetInstance as LifecycleOwner)
            }
        }
    }

    private fun <T : Any> checkClassTypeThenGetInstance(
        modelClass: KClass<T>,
        qualifier: AnnotationQualifier,
        lifecycleAware: LifecycleAwareAnnotation,
    ): T {
        val objectInstance: T? = modelClass.objectInstance
        val primaryConstructor: KFunction<T>? = modelClass.primaryConstructor
        return if (primaryConstructor == null) {
            getInstanceFromDependencyContainer(modelClass, qualifier, lifecycleAware)
        } else if (objectInstance != null) {
            objectInstance
        } else {
            primaryConstructor.isAccessible = true
            getInstanceFromConstructor(primaryConstructor)
        }
    }

    private fun <T : Any> getInstanceFromDependencyContainer(
        modelClass: KClass<T>,
        qualifier: AnnotationQualifier,
        lifecycleAware: LifecycleAwareAnnotation,
    ): T {
        val instanceFromContainer = dependencyContainer.getInstance<T>(modelClass, qualifier)
        return if (instanceFromContainer != null) {
            instanceFromContainer
        } else {
            val implementKClass =
                requireNotNull(dependencyContainer.getImplement<T>(modelClass, qualifier)) {
                    "$ERROR_DEPENDENCY_NOT_FOUND $modelClass to $qualifier"
                }
            createInstance(implementKClass, qualifier, lifecycleAware)
        }
    }

    private fun <T : Any> getInstanceFromConstructor(primaryConstructor: KFunction<T>): T {
        val constructorParams = getDependencyOfParameters(primaryConstructor.parameters)
        return primaryConstructor.call(*constructorParams)
    }

    private fun getDependencyOfParameters(parameters: List<KParameter>): Array<Any> {
        return parameters.map { parameter ->
            val classifier = parameter.type.classifier as KClass<*>
            if (parameter.hasAnnotation<ParamInject>()) {
                createInstance(classifier)
            }
            if (parameter.hasAnnotation<ApplicationContext>()) {
                dependencyContainer.getContext()
            }
        }.toTypedArray()
    }

    private fun <T : Any> setDependencyOfProperties(
        modelClass: KClass<T>,
        targetInstance: T,
    ) {
        modelClass.declaredMemberProperties.forEach { property ->
            if (checkNeedsDependencyInject(property)) {
                property.isAccessible = true
                val classifier = property.returnType.classifier as KClass<*>
                val qualifier: Annotation? = property.checkAnnotation<Qualifier>()
                val lifecycleAware: Annotation? = property.checkAnnotation<LifecycleAware>()
                val dependency: DependencyInstance =
                    createInstance(
                        classifier,
                        qualifier?.annotationClass,
                        lifecycleAware?.annotationClass,
                    )
                (property as KMutableProperty<*>).setter.call(targetInstance, dependency)
            }
        }
    }

    private fun checkNeedsDependencyInject(kProperty: KProperty<*>): Boolean =
        kProperty.hasAnnotation<FieldInject>() && kProperty.isLateinit && kProperty is KMutableProperty

    private inline fun <reified T : Annotation> KProperty<*>.checkAnnotation(): Annotation? {
        return this.annotations.find { it.annotationClass.hasAnnotation<T>() }
    }

    companion object {
        private const val ERROR_DEPENDENCY_NOT_FOUND =
            "Couldn't find dependency from Dependency Container :"
    }
}

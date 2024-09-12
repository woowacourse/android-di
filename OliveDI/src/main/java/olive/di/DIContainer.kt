package olive.di

import android.app.Application
import olive.di.annotation.Inject
import olive.di.annotation.Qualifier
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

class DIContainer(
    applicationInstance: Any,
    applicationType: KClass<out Application>,
    diModules: List<KClass<out DIModule>>,
) {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()
    private val namedInstances: NamedInstances = NamedInstances()

    init {
        instances[applicationType] = applicationInstance
        diModules.filter { !it.isAbstract }.forEach { it.injectModule() }
        diModules.filter { it.isAbstract }.forEach { it.injectAbstractModule() }
    }

    private fun KClass<out DIModule>.injectModule() {
        val functions = this.declaredMemberFunctions
        functions.forEach { function ->
            val type = function.returnType.classifier as KClass<*>
            val parameters = function.parameters().filter { this != it }
            val objectInstance = createSingleton(this)
            val arguments = parameters.map { singletonInstance(it) }
            val instance = function.call(objectInstance, *arguments.toTypedArray())
            instances[type] = instance ?: throw IllegalArgumentException()
        }
    }

    private fun KClass<out DIModule>.injectAbstractModule() {
        val functions = this.declaredMemberFunctions
        functions.forEach { function ->
            val instanceType = function.parameters().first { it != this }
            val cacheType = function.returnType.classifier as KClass<*>

            if (function.annotations.isQualifierAnnotation()) {
                val nameAnnotation = function.annotations.qualifierNameAnnotation()
                namedInstances[cacheType] = NamedInstance(nameAnnotation, create(instanceType))
                return@forEach
            }
            createSingleton(instanceType, cacheType)
        }
    }

    private fun KFunction<*>.parameters(): List<KClass<*>> =
        parameters.map { it.type.classifier as KClass<*> }

    private fun List<Annotation>.isQualifierAnnotation(): Boolean {
        return any { annotation ->
            annotation.annotationClass.annotations.any { it.annotationClass == Qualifier::class }
        }
    }

    private fun List<Annotation>.qualifierNameAnnotation(): KClass<out Annotation> {
        val nameAnnotation =
            first { functionAnnotation ->
                functionAnnotation.annotationClass.annotations.any { it.annotationClass == Qualifier::class }
            }
        return nameAnnotation.annotationClass
    }

    fun <T : Any> instance(classType: KClass<T>): T {
        return create(classType)
    }

    fun <T : Any> singletonInstance(classType: KClass<T>): T {
        return instances[classType] as? T ?: createSingleton(classType)
    }

    private fun <T : Any> createSingleton(
        instanceType: KClass<T>,
        cacheType: KClass<*> = instanceType,
    ): T {
        return create(instanceType).apply {
            putSingletonInstance(cacheType)
        }
    }

    private fun <T : Any> create(classType: KClass<T>): T {
        if (instances[classType] != null) return instances[classType] as T
        val parameters = classType.constructors.first().parameters
        val arguments = parameters.map(::argumentInstance)
        val instance = classType.constructors.first().call(*arguments.toTypedArray())
        instance.injectFieldDependency()
        return instance
    }

    private fun argumentInstance(parameter: KParameter): Any {
        val parameterType = parameter.type.classifier as KClass<*>
        if (parameter.annotations.isQualifierAnnotation()) {
            return namedInstance(parameterType, parameter.annotations)
        }
        return instances[parameterType] ?: createSingleton(parameterType)
    }

    private fun Any.putSingletonInstance(type: KClass<*>) {
        if (instances.containsKey(type)) {
            throw IllegalArgumentException("이미 해당 클래스의 인스턴스가 존재합니다.")
        }
        instances[type] = this
    }

    private fun Any.injectFieldDependency() {
        val fieldsToInject = this::class.fieldsToInject()
        fieldsToInject.forEach { fieldToInject ->
            val property = fieldToInject as KMutableProperty1
            property.isAccessible = true
            val type = property.returnType.classifier as KClass<*>
            val instance =
                if (property.annotations.isQualifierAnnotation()) {
                    namedInstance(type, property.annotations)
                } else {
                    instances[type] ?: createSingleton(type)
                }
            fieldToInject.setter.call(this, instance)
        }
    }

    private fun KClass<*>.fieldsToInject(): List<KProperty1<out Any, *>> {
        return declaredMemberProperties
            .filter { it.isLateinit }
            .filter { it.hasAnnotation<Inject>() }
    }

    private fun namedInstance(type: KClass<*>, annotations: List<Annotation>): Any {
        val nameAnnotation: KClass<out Annotation> = annotations.qualifierNameAnnotation()
        return namedInstances.instanceByName(nameAnnotation, type)
            ?: throw IllegalArgumentException("${nameAnnotation.simpleName}로 지정된 인스턴스가 존재하지 않습니다.")
    }
}

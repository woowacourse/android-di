package olive.di

import android.app.Application
import olive.di.annotation.Inject
import olive.di.annotation.Qualifier
import olive.di.annotation.Singleton
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

    private fun KClass<out DIModule>.injectModule() { // 일반 class 모듈인 경우 함수를 직접 호출한 결과를 인스턴스로 저장
        val functions = this.declaredMemberFunctions
        functions.forEach { function ->
            val type = function.returnType.classifier as KClass<*>
            val parameters = function.parameters().filter { this != it }
            val objectInstance = createSingleton(this)
            val arguments = parameters.map { instance(it) }
            val instance = function.call(objectInstance, *arguments.toTypedArray())
            instances[type] = instance ?: throw IllegalArgumentException()
        }
    }

    private fun KClass<out DIModule>.injectAbstractModule() {
        // abstact class 모듈인 경우 함수의 return 타입을 key로, 함수의 파라미터 타입을 value로 저장
        // 파라미터 타입에 맞는 객체를 createSingleton로 직접 생성
        val functions = this.declaredMemberFunctions
        functions.forEach { function ->
            val instanceType = function.parameters().first { it != this }
            val cacheType = function.returnType.classifier as KClass<*>

            if (function.annotations.hasQualifierAnnotation()) { // Qualifier가 붙은 경우 namedInstances에 저장
                val nameAnnotation = function.annotations.qualifierNameAnnotation()
                namedInstances[cacheType] = NamedInstance(nameAnnotation, create(instanceType))
                return@forEach
            }
            createSingleton(instanceType, cacheType)
        }
    }

    private fun KFunction<*>.parameters(): List<KClass<*>> = parameters.map { it.type.classifier as KClass<*> }

    private fun List<Annotation>.hasQualifierAnnotation(): Boolean {
        return any { annotation -> annotation.isQualifierAnnotation() }
    }

    private fun List<Annotation>.qualifierNameAnnotation(): KClass<out Annotation> {
        val nameAnnotation = first { functionAnnotation -> functionAnnotation.isQualifierAnnotation() }
        return nameAnnotation.annotationClass
    }

    private fun Annotation.isQualifierAnnotation(): Boolean {
        return annotationClass.annotations.any { it.annotationClass == Qualifier::class }
    }

    fun <T : Any> instance(classType: KClass<T>): T {
        if (classType.hasAnnotation<Singleton>()) {
            return instances[classType] as? T ?: createSingleton(classType)
        }
        return create(classType)
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
        if (parameter.annotations.hasQualifierAnnotation()) {
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
            val instance = // Qualifier가 붙은 경우 namedInstances에서 찾고, 안 붙은 경우 instances에서 찾음
                if (property.annotations.hasQualifierAnnotation()) {
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

    private fun namedInstance(
        type: KClass<*>,
        annotations: List<Annotation>,
    ): Any {
        val nameAnnotation: KClass<out Annotation> = annotations.qualifierNameAnnotation()
        return namedInstances.instanceByName(nameAnnotation, type)
            ?: throw IllegalArgumentException("${nameAnnotation.simpleName}로 지정된 인스턴스가 존재하지 않습니다.")
    }
}

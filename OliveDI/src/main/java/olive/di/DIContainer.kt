package olive.di

import android.app.Application
import androidx.lifecycle.ViewModel
import olive.di.annotation.ActivityScope
import olive.di.annotation.Singleton
import olive.di.annotation.ViewModelScope
import olive.di.util.fieldsToInject
import olive.di.util.hasQualifierAnnotation
import olive.di.util.parameters
import olive.di.util.qualifierNameAnnotation
import olive.di.util.toReturnType
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.superclasses
import kotlin.reflect.jvm.isAccessible

object DIContainer {
    fun injectApplication(applicationInstance: Any, applicationType: KClass<out Application>) {
        instances[applicationType] = applicationInstance
    }

    fun injectModules(diModules: List<KClass<out DIModule>>) {
        diModules.filter { !it.isAbstract }.forEach { it.injectModule() }
        diModules.filter { it.isAbstract }.forEach { it.injectAbstractModule() }
    }

    private fun KClass<out DIModule>.injectModule() { // 일반 class 모듈인 경우 함수를 직접 호출한 결과를 인스턴스로 저장
        val functions = this.declaredMemberFunctions
        functions.forEach { function ->
            val type = function.toReturnType()
            val instanceProvider = InstanceProvider { function.calledInstance(this) }
            if (function.hasAnnotation<ActivityScope>()) {
                activityInstances[type] = instanceProvider
                return@forEach
            }
            if (function.hasAnnotation<ViewModelScope>()) {
                viewModelInstances[type] = instanceProvider
                return@forEach
            }
            instances[type] = instanceProvider.get()
        }
    }

    private fun KFunction<*>.calledInstance(classType: KClass<*>): Any {
        val parameters = parameters(classType)
        val objectInstance = instance(classType)
        val arguments = parameters.map { instance(it) }
        val instance = call(objectInstance, *arguments.toTypedArray())
        return instance ?: throw IllegalArgumentException()
    }

    private fun KClass<out DIModule>.injectAbstractModule() {
        // abstact class 모듈인 경우 함수의 return 타입을 key로, 함수의 파라미터 타입을 value로 저장
        // 파라미터 타입에 맞는 객체를 createSingleton로 직접 생성
        val functions = this.declaredMemberFunctions
        functions.forEach { function ->
            val cacheType = function.toReturnType()
            val instanceType = function.parameters(this).first()

            if (function.annotations.hasQualifierAnnotation()) { // Qualifier가 붙은 경우 namedInstances에 저장
                val nameAnnotation = function.annotations.qualifierNameAnnotation()
                namedInstances[cacheType] = NamedInstance(nameAnnotation, create(instanceType))
                return@forEach
            }
            if (function.hasAnnotation<ActivityScope>()) {
                activityInstances[cacheType] = InstanceProvider { create(instanceType) }
                return@forEach
            }
            if (function.hasAnnotation<ViewModelScope>()) {
                viewModelInstances[cacheType] = InstanceProvider { create(instanceType) }
                return@forEach
            }
            createSingleton(instanceType, cacheType)
        }
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
        injectFieldDependency(instance)
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

    internal fun injectFieldDependency(instance: Any) {
        val fieldsToInject = instance::class.fieldsToInject()
        fieldsToInject.forEach { property ->
            property.isAccessible = true
            val propertyInstance = property.propertyInstance()
            property.setter.call(instance, propertyInstance)
        }
    }

    private fun KMutableProperty1<out Any, *>.propertyInstance(): Any {
        val type = this.toReturnType()
        if (type.superclasses.contains(ViewModel::class)) {

        }

        // Qualifier가 붙은 경우 namedInstances에서 찾고, 안 붙은 경우 instances에서 찾음
        return if (annotations.hasQualifierAnnotation()) {
            namedInstance(type, annotations)
        } else {
            instances[type] ?: createSingleton(type)
        }
    }

    private fun namedInstance(
        type: KClass<*>,
        annotations: List<Annotation>,
    ): Any {
        val nameAnnotation = annotations.qualifierNameAnnotation()
        return namedInstances.instanceByName(nameAnnotation, type)
            ?: throw IllegalArgumentException("${nameAnnotation.simpleName}로 지정된 인스턴스가 존재하지 않습니다.")
    }
}

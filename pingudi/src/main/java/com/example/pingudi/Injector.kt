package com.example.pingudi

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

class Injector(private val container: Container) {

    fun <T : Any> inject(modelClass: KClass<T>): T {
        val result = getInstance(modelClass.createType()) ?: createInstance(modelClass)
        return result as T
    }

    private fun getInstance(type: KType): Any? =
        container.getInstance(type)

    fun createInstance(kClass: KClass<*>): Any {
        val constructor = kClass.primaryConstructor ?: throw IllegalStateException()
        val parameters = constructor.parameters
        val instance = createInstanceWithArgument(parameters, kClass, constructor)
        injectField(instance)

        return instance
    }

    private fun createInstanceWithArgument(
        parameters: List<KParameter>,
        kClass: KClass<*>,
        constructor: KFunction<Any>,
    ): Any = if (parameters.isEmpty()) {
        kClass.createInstance()
    } else {
        // parameter의 어노테이션과 get해온 argument의 어노테이션을 비교하여 골라준다
        val arguments = parameters.map {
            getInstancesWithAnnotation(it.type, it.annotations)
        }
        constructor.call(*arguments.toTypedArray())
    }

    private fun getInstancesWithAnnotation(type: KType, annotation: List<Annotation>): Any {
        if (annotation.isEmpty()) {
            // 어노테이션이 없다면 해당 타입에 해당하는 인스턴스를 내보낸다
            return container.getInstance(type)
                ?: throw java.lang.IllegalArgumentException()
        }
        val instances = container.getInstances(type)
        // 가져온 인스턴스들의 어노테이션들과 그 인스턴스가 들어가야하는 파라미터의 어노테이션들을 비교하여 일치하는 것이 하나라도 있는 것들을 고른다
        val result = instances.filter {
            it::class.annotations.any { instanceAnnotaion ->
                annotation.any { parameterAnnotation ->
                    instanceAnnotaion == parameterAnnotation
                }
            }
        }
        if (result.isEmpty()) {
            throw java.lang.IllegalStateException()
        } else {
            return result.first()
        }
    }

    private fun <T : Any> injectField(instance: T) {
        instance::class.memberProperties.forEach { property ->
            if (property.annotations.any { it is com.example.pingudi.annotation.InjectField }) {
                if (property is KMutableProperty<*>) {
                    property.setter.call(instance, getInstance(property.returnType))
                }
            }
        }
    }
}

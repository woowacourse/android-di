package com.medandro.di

import androidx.lifecycle.ViewModel
import androidx.room.RoomDatabase
import com.medandro.di.annotation.InMemory
import com.medandro.di.annotation.InjectField
import com.medandro.di.annotation.RoomDB
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class DIContainer(
    vararg registerClasses: KClass<*>,
) {
    private val instances = mutableMapOf<DependencyKey, Any>()
    private val interfaceMapping = mutableMapOf<DependencyKey, KClass<*>>()
    private val externalSingletons = mutableMapOf<KClass<*>, Any>()

    init {
        generateInterfaceMapping(registerClasses)
    }

    fun registerSingleton(instance: Any): DIContainer {
        externalSingletons[instance::class] = instance
        return this
    }

    fun getInstance(
        kClass: KClass<*>,
        qualifier: String? = null,
    ): Any {
        val dependencyKey = DependencyKey(kClass, qualifier)

        // ViewModel은 매번 새로 생성
        if (ViewModel::class.java.isAssignableFrom(kClass.java)) {
            return createNewInstance(kClass)
        }

        // DIContainer 내부에서 생성&관리되는 싱글턴 인스턴스에서 반환
        instances[dependencyKey]?.let { return it }

        // 외부에서 등록된 싱글턴 인스턴스에서 반환
        externalSingletons[kClass]?.let { return it }

        // Room Database에서 Dao 인스턴스 반환

        resolveFromDatabase(kClass, qualifier)?.let { dao ->
            instances[dependencyKey] = dao
            return dao
        }

        // 인터페이스일 경우 매핑된 클래스로 반환
        interfaceMapping[dependencyKey]?.let { implClass ->
            val instance = createNewInstance(implClass)
            instances[dependencyKey] = instance
            return instance
        }

        // 인스턴스가 존재하지 않을 경우 생성
        val createdInstance = createNewInstance(kClass)
        instances[dependencyKey] = createdInstance
        return createdInstance
    }

    fun injectFields(target: Any) {
        target::class
            .memberProperties
            .filterIsInstance<KMutableProperty1<Any, Any?>>()
            .filter { it.hasAnnotation<InjectField>() }
            .forEach { property ->
                injectSingleField(target, property)
            }
    }

    private fun generateInterfaceMapping(registerClasses: Array<out KClass<*>>) {
        registerClasses.forEach { implClass ->
            val qualifier = getQualifier(implClass)
            val interfaces =
                implClass.supertypes
                    .mapNotNull { it.classifier as? KClass<*> }
                    .filter { it.java.isInterface }

            interfaces.forEach { interfaceClass ->
                val key = DependencyKey(interfaceClass, qualifier)
                interfaceMapping[key] = implClass
            }
        }
    }

    private fun injectSingleField(
        target: Any,
        property: KMutableProperty1<Any, Any?>,
    ) {
        try {
            val fieldType = property.returnType.classifier as KClass<*>
            val qualifier = getFieldQualifier(property)
            val dependency = getInstance(fieldType, qualifier)

            property.isAccessible = true
            property.set(target, dependency)
        } catch (e: Exception) {
            throw IllegalStateException(
                "${target::class.simpleName}의 '${property.name}, ${getFieldQualifier(property)}필드 주입 실패 : ${e.message}",
                e,
            )
        }
    }

    private fun resolveFromDatabase(
        kClass: KClass<*>,
        qualifier: String?,
    ): Any? {
        if (qualifier != "RoomDB") {
            if (qualifier != null) {
                return null
            }
        }

        val database =
            externalSingletons.values.find { it is RoomDatabase } as? RoomDatabase
                ?: return null

        val dbClass: Class<RoomDatabase> = database.javaClass
        val method: Method =
            dbClass.methods.firstOrNull { m: Method ->
                m.parameterCount == 0 && kClass.java.isAssignableFrom(m.returnType)
            } ?: return null
        return method.invoke(database)
    }

    private fun createNewInstance(kClass: KClass<*>): Any {
        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalStateException("주 생성자가 존재하지 않아 객체를 생성할 수 없습니다")

        val parameterMap =
            constructor.parameters
                .filterNot { it.isOptional }
                .associateWith { param ->
                    val paramType = param.type.classifier as KClass<*>
                    externalSingletons[paramType] ?: getInstance(paramType)
                }
        return constructor.callBy(parameterMap)
    }

    private fun getQualifier(kClass: KClass<*>): String? =
        when {
            kClass.hasAnnotation<RoomDB>() -> "RoomDB"
            kClass.hasAnnotation<InMemory>() -> "InMemory"
            else -> null
        }

    private fun getFieldQualifier(property: KMutableProperty1<Any, Any?>): String? =
        when {
            property.hasAnnotation<RoomDB>() -> "RoomDB"
            property.hasAnnotation<InMemory>() -> "InMemory"
            else -> null
        }
}

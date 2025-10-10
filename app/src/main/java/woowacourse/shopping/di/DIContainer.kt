package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.room.RoomDatabase
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

class DIContainer(
    private val interfaceMapping: Map<KClass<*>, KClass<*>>,
    private val database: RoomDatabase,
) {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun injectFields(target: Any) {
        target::class
            .memberProperties
            .filterIsInstance<KMutableProperty1<Any, Any?>>()
            .filter { it.hasAnnotation<InjectField>() }
            .forEach { property ->
                injectSingleField(target, property)
            }
    }

    fun getInstance(kClass: KClass<*>): Any {
        if (ViewModel::class.java.isAssignableFrom(kClass.java)) {
            return createNewInstance(kClass)
        }

        instances[kClass]?.let { return it }

        resolveFromDatabase(kClass)?.let { dao ->
            instances[kClass] = dao
            return dao
        }

        interfaceMapping[kClass]?.let {
            return getInstance(it)
        }

        val createdInstance = createNewInstance(kClass)
        registerInstance(kClass, createdInstance)
        return createdInstance
    }

    private fun injectSingleField(
        target: Any,
        property: KMutableProperty1<Any, Any?>,
    ) {
        try {
            val fieldType = property.returnType.classifier as KClass<*>
            val dependency = getInstance(fieldType)

            property.isAccessible = true
            property.set(target, dependency)
        } catch (e: Exception) {
            throw IllegalStateException(
                "${target::class.simpleName}의 '${property.name}필드 주입 실패 : ${e.message}",
                e,
            )
        }
    }

    private fun resolveFromDatabase(kClass: KClass<*>): Any? {
        val dbClass: Class<RoomDatabase> = database.javaClass
        val method: Method =
            dbClass.methods.firstOrNull { m: Method ->
                m.parameterCount == 0 && kClass.java.isAssignableFrom(m.returnType)
            } ?: return null
        return method.invoke(database)
    }

    private fun createNewInstance(kClass: KClass<*>): Any {
        val constructor = kClass.primaryConstructor ?: throw IllegalStateException()

        val parameterMap =
            constructor.parameters
                .filterNot { it.isOptional }
                .associateWith { param ->
                    val paramType = param.type.classifier as KClass<*>
                    getInstance(paramType)
                }
        return constructor.callBy(parameterMap)
    }

    private fun registerInstance(
        kClass: KClass<*>,
        instance: Any,
    ) {
        instances[kClass] = instance
    }
}

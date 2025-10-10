package woowacourse.shopping.di

import androidx.room.RoomDatabase
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DIContainer(
    private val interfaceMapping: Map<KClass<*>, KClass<*>>,
    private val database: RoomDatabase,
) {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun getInstance(kClass: KClass<*>): Any {
        instances[kClass]?.let { return it }

        resolveFromDatabase(kClass)?.let { dao ->
            instances[kClass] = dao
            return dao
        }

        interfaceMapping[kClass]?.let {
            return getInstance(it)
        }

        val createdInstance = createNewInstance(kClass)
        registerRepository(kClass, createdInstance)
        return createdInstance
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

    private fun registerRepository(
        kClass: KClass<*>,
        instance: Any,
    ) {
        instances[kClass] = instance
    }
}

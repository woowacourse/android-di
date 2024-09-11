package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

object DIContainer {
    val instances = mutableMapOf<KClass<*>, Any>()

    fun setUpInstances(context: Context) {
        val appModule = DefaultAppModule(context)
        val cartProductDao = appModule.database.cartProductDao()
        setInstance(CartProductDao::class, cartProductDao)

        setInstance(
            ProductRepository::class,
            DefaultProductRepository(),
        )
        setInstance(
            CartRepository::class,
            DefaultCartRepository(cartProductDao),
        )
    }

    fun getInstance(type: KClass<*>): Any = instances[type] ?: throw NullPointerException("No Instance Found")

    fun setInstance(
        type: KClass<*>,
        instance: Any,
    ) {
        instances[type] = instance
    }

    inline fun <reified T : Any> inject(): T {
        if (instances.containsKey(T::class)) {
            return getInstance(T::class) as T
        }
        val constructor = T::class.primaryConstructor ?: throw IllegalArgumentException("${T::class} has no primary constructor")
        val parameters =
            constructor.parameters
                .map {
                    val clazz = it.type.classifier as KClass<*>
                    getInstance(clazz)
                }.toTypedArray()
        val instance = constructor.call(*parameters)
        setInstance(T::class, instance)
        return instance
    }

    fun injectFieldDependencies(target: Any) {
        val injectionProperties =
            target::class.declaredMemberProperties.filter {
                it.javaField?.getAnnotation(FieldInject::class.java) !=
                    null
            }

        injectionProperties.forEach { property ->
            if (property.javaField?.getAnnotation(FieldInject::class.java) != null) {
                property.isAccessible = true
                val value = instances[property.returnType.classifier as KClass<*>]
                property.javaField?.set(target, value)
            } else {
                println("ERROR: DIContainer, Property ${property.name} is not annotated with FieldInject")
            }
        }
    }
}

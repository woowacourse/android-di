package com.example.di2

import kotlin.reflect.KClass

data class DIKey(
    val kClass: KClass<*>,
    val qualifierClass: KClass<*>? = null,
)

class AppContainer(
    private val bindings: Map<DIKey, () -> Any>,
) {
    private val instances = mutableMapOf<DIKey, Any>()

//    init {
//        providers[DIKey(CartRepository::class, RoomDatabase::class)] =
//            { DefaultCartRepository(shoppingDatabase.cartProductDao()) }
//
//        providers[DIKey(ProductRepository::class, InMemory::class)] = { DefaultProductRepository() }
//    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(
        clazz: KClass<T>,
        qualifier: KClass<*>? = null,
    ): T {
        val key = DIKey(clazz, qualifier)
        instances[key]?.let { return it as T }

        val provider =
            bindings[key]
                ?: throw IllegalArgumentException("No binding for $clazz with qualifier ${qualifier?.simpleName}")
        val instance = provider() as T
        instances[key] = instance
        return instance
    }
}

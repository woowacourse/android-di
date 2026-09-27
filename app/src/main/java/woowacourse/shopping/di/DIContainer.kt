package woowacourse.shopping.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ShoppingDatabase
import kotlin.jvm.kotlin
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

object DIContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun initialize(context: Context) {
        val db =
            Room
                .databaseBuilder<ShoppingDatabase>(context, "shopping-database")
                .setDriver(AndroidSQLiteDriver())
                .build()
        val cartDao = db.cartProductDao()
        instances[CartProductDao::class] = cartDao
    }

    fun <T : Any> createInstance(modelClass: KClass<T>): T {
        val implementationClass =
            if (modelClass.java.isInterface) {
                findImplementation(modelClass)
            } else {
                modelClass
            }
        val constructor = implementationClass.primaryConstructor ?: throw IllegalArgumentException("생성자를 찾을 수 없어요 : $modelClass")
        val dependencies = findDependencies(constructor)
        return constructor.call(*dependencies.toTypedArray())
    }

    fun <T : Any> findImplementation(modelClass: KClass<T>): KClass<out T> {
        if (modelClass == CartRepository::class) {
            return CartRepositoryModule.provideCartRepository() as KClass<out T>
        }

        throw IllegalArgumentException("구현체를 찾을 수 없어요: $modelClass")
    }

    private fun <T : Any> findDependencies(constructor: KFunction<T>): List<Any> {
        val types = constructor.parameters.map { it.type.classifier as KClass<*> }
        return getInstances(types)
    }

    private fun getInstances(types: List<KClass<*>>): List<Any> =
        types.map { type ->
            instances.getOrPut(type) {
                createInstance(type)
            }
        }
}

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = DIContainer.createInstance(modelClass.kotlin)
}

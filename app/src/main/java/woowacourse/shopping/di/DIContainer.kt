package woowacourse.shopping.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.DIContainer.createInstance
import woowacourse.shopping.di.DIContainer.injectFields
import kotlin.collections.forEach
import kotlin.jvm.kotlin
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

object DIContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun initialize(context: Context) {
        val db =
            Room
                .databaseBuilder<ShoppingDatabase>(context, "shopping-database")
                .setDriver(AndroidSQLiteDriver())
                .build()
        ShoppingDatabase::class.declaredFunctions.forEach { function ->
            instances[(function.returnType.classifier as? KClass<*>)!!] = function.call(db) as Any
        }
    }

    fun <T : Any> createInstance(modelClass: KClass<T>): T {
        if (instances[modelClass] != null) return instances[modelClass] as T
        val implementationClass =
            if (modelClass.java.isInterface) {
                findImplementation(modelClass)
            } else {
                modelClass
            }
        val constructor = implementationClass.primaryConstructor ?: throw IllegalArgumentException("생성자를 찾을 수 없어요 : $modelClass")
        val dependencies = findDependencies(constructor)
        val instance = constructor.call(*dependencies.toTypedArray())
        instances[implementationClass] = instance
        if (modelClass != implementationClass) instances[modelClass] = instance
        return instance
    }

    fun <T : Any> injectFields(instance: T) {
        instance::class
            .memberProperties
            .filter { property ->
                property.annotations.any { it.annotationClass == Inject::class } &&
                    property is KMutableProperty1<*, *>
            }.forEach { property ->
                val mutableProperty = property as KMutableProperty1<*, *>
                val dependency = createInstance(property.returnType.classifier as KClass<*>)
                mutableProperty.setter.call(instance, dependency)
            }
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
            createInstance(type)
        }
}

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = modelClass.kotlin.primaryConstructor!!.call()
        injectFields(viewModel)
        return viewModel
    }
}

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject

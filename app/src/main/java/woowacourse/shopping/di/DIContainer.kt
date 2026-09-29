package woowacourse.shopping.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.di.annotation.Qualifier
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
    private val creating = mutableSetOf<KClass<*>>()
    private val bindings = mutableMapOf<DependencyKey, KClass<*>>()

    fun <T : Any> bind(
        type: KClass<T>,
        implementation: KClass<out T>,
        qualifier: KClass<out Annotation>? = null,
    ) {
        bindings[DependencyKey(type, qualifier)] = implementation
    }

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

    fun <T : Any> createInstance(
        modelClass: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        if (instances[modelClass] != null) return instances[modelClass] as T

        if (modelClass in creating) {
            throw IllegalStateException("순환 의존성이 발생했어요: $modelClass")
        }

        creating += modelClass

        try {
            val implementationClass =
                if (modelClass.java.isInterface) {
                    findImplementation(modelClass, qualifier)
                } else {
                    modelClass
                }
            val constructor = implementationClass.primaryConstructor ?: throw IllegalArgumentException("생성자를 찾을 수 없어요 : $modelClass")
            val dependencies = findDependencies(constructor)
            if (dependencies.isEmpty()) {
                val instance = constructor.call()
                injectFields(instance)
                return instance
            }
            val instance = constructor.call(*dependencies.toTypedArray())
            instances[implementationClass] = instance
            if (modelClass != implementationClass) instances[modelClass] = instance
            return instance
        } finally {
            creating -= modelClass
        }
    }

    private fun <T : Any> injectFields(instance: T) {
        instance::class
            .memberProperties
            .filter { property ->
                property.annotations.any { it.annotationClass == Inject::class } &&
                    property is KMutableProperty1<*, *>
            }.forEach { property ->
                val mutableProperty = property as KMutableProperty1<*, *>
                val dependencyType = property.returnType.classifier as KClass<*>
                val qualifier =
                    property.annotations
                        .firstOrNull { annotation ->
                            annotation.annotationClass.annotations.any {
                                it.annotationClass == Qualifier::class
                            }
                        }?.annotationClass
                val dependency = createInstance(dependencyType, qualifier)
                mutableProperty.setter.call(instance, dependency)
            }
    }

    fun <T : Any> findImplementation(
        modelClass: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): KClass<out T> {
        val key = DependencyKey(modelClass, qualifier)

        return bindings[key] as? KClass<out T>
            ?: throw IllegalArgumentException("구현체를 찾을 수 없어요: $modelClass, qualifier = $qualifier")
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
    override fun <T : ViewModel> create(modelClass: Class<T>): T = DIContainer.createInstance(modelClass.kotlin)
}

package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.di.Container
import kotlin.reflect.KClass

object HunnitFactory : ViewModelProvider.Factory {
    private val container = Container()

    fun <T : Any> register(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) = container.register(type, instance, qualifier)

    fun <T : Any> bind(
        type: KClass<T>,
        implementation: KClass<out T>,
        qualifier: KClass<out Annotation>? = null,
    ) = container.bind(type, implementation, qualifier)

    override fun <T : ViewModel> create(modelClass: Class<T>): T = container.create(modelClass.kotlin)

    fun <T : Any> createInstance(type: KClass<T>): T = container.create(type)

    fun getInstance(type: KClass<*>): Any = container.resolve(type)
}

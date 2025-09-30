package woowacourse.shopping.di

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.ui.MainApplication
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class AppContainer {
    private val cache: MutableMap<KClass<*>, Any> = mutableMapOf()

    operator fun get(clazz: KClass<*>): Any? = cache[clazz]

    fun instantiate(clazz: KClass<*>):Any? {
        if (cache.containsKey(clazz)) return this[clazz]
        return clazz.primaryConstructor?.let { constructor ->
            val arguments = constructor.parameters
                .filter { !it.isOptional }
                .associateWith { parameter ->
                    instantiate(parameter.type.jvmErasure)
                }
            cache[clazz] = constructor.callBy(arguments)
            cache[clazz]
        }?: clazz.createInstance()
    }
}
fun CreationExtras.containerProvider(clazz: KClass<*>): Any? {
    val store = (this[APPLICATION_KEY] as MainApplication).appContainer
    return store.instantiate(clazz)
}
package woowacourse.shopping.di

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.ui.MainApplication
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class AppContainer {
    private val cache: MutableMap<KClass<*>, Any> = mutableMapOf()

    operator fun get(clazz: KClass<*>): Any? = cache[clazz]

    fun instantiate(clazz: KClass<*>): Any? {
        val inProgress = mutableSetOf<KClass<*>>()
        return instantiate(clazz, inProgress)
    }

    private fun instantiate(
        clazz: KClass<*>,
        inProgress: MutableSet<KClass<*>>,
    ): Any? {
        if (cache.containsKey(clazz)) return this[clazz]

        if (inProgress.contains(clazz)) {
            throw IllegalArgumentException(
                "$ERR_CIRCULAR_DEPENDENCY_DETECTED : ${clazz.simpleName}",
            )
        }

        return clazz.primaryConstructor?.let { constructor ->
            inProgress.add(clazz)
            cache[clazz] = reflect(constructor, inProgress)
            inProgress.remove(clazz)
            cache[clazz]
        } ?: clazz.createInstance()
    }

    private fun reflect(
        constructor: KFunction<Any>,
        inProgress: MutableSet<KClass<*>> = mutableSetOf(),
    ): Any {
        val arguments =
            constructor.parameters
                .filter { !it.isOptional }
                .associateWith { parameter ->
                    val instance = instantiate(parameter.type.jvmErasure, inProgress)
                    instance
                }
        return constructor.callBy(arguments)
    }

    companion object {
        private const val ERR_CIRCULAR_DEPENDENCY_DETECTED = "순환 참조가 발견되었습니다"
    }
}

fun CreationExtras.containerProvider(clazz: KClass<*>): Any? {
    val store = (this[APPLICATION_KEY] as MainApplication).appContainer
    return store.instantiate(clazz)
}

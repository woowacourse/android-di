package woowacourse.shopping.di

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.ui.MainApplication
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.jvmErasure

class AppContainerDelegate<T>(
    private val appContainerStore: AppContainerStore
) {

    @Suppress("UNCHECKED_CAST")
    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>
    ): T {
        val instance = appContainerStore.cache[property.returnType.jvmErasure]
        return when(instance) {
            is Lazy<*> -> instance.value as? T ?: throw IllegalStateException(
                "$ERR_INSTANCE_NOT_FOUND : ${property.returnType}"
            )
            null -> throw IllegalStateException(
                "$ERR_INSTANCE_NOT_FOUND : ${property.returnType}"
            )
            else -> instance as? T ?: throw IllegalStateException(
                "$ERR_INSTANCE_NOT_FOUND : ${property.returnType}"
            )
        }
    }

    companion object {
        private const val ERR_INSTANCE_NOT_FOUND = "해당 인스턴스를 찾을 수 없습니다"
    }
}

inline fun <reified T> CreationExtras.containerProvider(): AppContainerDelegate<T> {
    val store = (this[APPLICATION_KEY] as MainApplication).appContainerStore
    return AppContainerDelegate(store)
}

fun CreationExtras.containerProvider(parameter: KParameter): Any? {
    val store = (this[APPLICATION_KEY] as MainApplication).appContainerStore
    val clazz = parameter.type.jvmErasure
    val instance = store.cache[clazz]
    return when(instance) {
        is Lazy<*> -> instance.value
        null -> null
        else -> instance
    }
}
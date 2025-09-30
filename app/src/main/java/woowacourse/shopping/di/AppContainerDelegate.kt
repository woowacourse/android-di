package woowacourse.shopping.di

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.ui.MainApplication
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KType

class AppContainerDelegate<T>(
    private val appContainerStore: AppContainerStore
) {

    @Suppress("UNCHECKED_CAST")
    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>
    ): T {
        val instance = appContainerStore.cache[property.returnType]
        return (instance as? Lazy<T>)?.value ?: (instance as? T) ?: throw IllegalStateException(
            "$ERR_INSTANCE_NOT_FOUND : ${property.returnType}"
        )
    }
}
private const val ERR_INSTANCE_NOT_FOUND = "해당 타입의 인스턴스를 찾을 수 없습니다"

inline fun <reified T> CreationExtras.containerProvider(): AppContainerDelegate<T> {
    val store = (this[APPLICATION_KEY] as MainApplication).appContainerStore
    return AppContainerDelegate(store)
}

fun CreationExtras.containerProvider(type: KType): Any? {
    val store = (this[APPLICATION_KEY] as MainApplication).appContainerStore
    return (store.cache[type] as? Lazy<*>)?.value ?: store.cache[type] ?: throw IllegalStateException(
        "$ERR_INSTANCE_NOT_FOUND : $type"
    )
}
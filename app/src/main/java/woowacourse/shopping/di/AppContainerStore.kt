package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class AppContainerStore(
    appContainer: AppContainer
) {
    val cache: MutableMap<KClass<*>, Any> = mutableMapOf()

    init {
        appContainer::class.memberProperties.forEach { property ->
            property.isAccessible = true
            val editableProperty = property as? KProperty1<AppContainer, *>
            cache[property.returnType.jvmErasure] =
                editableProperty?.getDelegate(appContainer) ?: editableProperty?.get(appContainer)
                        ?: throw IllegalStateException(
                    "$ERR_PROPERTY_NOT_INITIALIZED : ${property.returnType}"
                )
        }
    }

    companion object {
        private const val ERR_PROPERTY_NOT_INITIALIZED = "해당 프로퍼티가 초기화되지 않았습니다"
    }
}
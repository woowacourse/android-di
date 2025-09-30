package woowacourse.shopping.di

import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class AppContainerStore(
    appContainer: AppContainer
) {
    val cache: MutableMap<KType, Any> = mutableMapOf()

    init {
        appContainer::class.memberProperties.forEach { property ->
            property.isAccessible = true
            val editableProperty = property as? KProperty1<AppContainer, *>
            cache[property.returnType] =
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
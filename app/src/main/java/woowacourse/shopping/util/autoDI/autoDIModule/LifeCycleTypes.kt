package woowacourse.shopping.util.autoDI.autoDIModule

import woowacourse.shopping.util.autoDI.LifeCycleType
import kotlin.reflect.KType

@Suppress("UNCHECKED_CAST")
sealed class LifeCycleTypes {
    abstract val value: MutableList<out LifeCycleType<*>>
    internal fun <T> searchWithOutQualifier(kType: KType): T? {
        return value.find { it.type == kType }?.getInstance() as T?
    }

    internal fun <T> searchWithQualifier(kType: KType, qualifier: String): T? {
        return value.find { it.type == kType && it.qualifier == qualifier }?.getInstance() as T?
    }

    class Singletons(override val value: MutableList<LifeCycleType.Singleton<*>>) :
        LifeCycleTypes() {
        internal fun <T : Any> add(qualifier: String? = null, initializeMethod: () -> T) {
            value.add(LifeCycleType.Singleton(qualifier, initializeMethod))
        }
    }

    class Disposables(override val value: MutableList<LifeCycleType.Disposable<*>>) :
        LifeCycleTypes() {
        internal fun <T : Any> add(qualifier: String? = null, initializeMethod: () -> T) {
            value.add(LifeCycleType.Disposable(qualifier, initializeMethod))
        }
    }
//    class Activities(override val values: MutableList<LifeCycleType<*>>) : LifeCycleTypes()
//    class Fragments(override val values: MutableList<LifeCycleType<*>>) : LifeCycleTypes()
}

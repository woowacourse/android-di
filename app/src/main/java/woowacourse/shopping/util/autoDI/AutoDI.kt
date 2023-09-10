package woowacourse.shopping.util.autoDI

import woowacourse.shopping.util.autoDI.autoDIContainer.AutoDIModuleContainer
import kotlin.reflect.KType
import kotlin.reflect.typeOf

object AutoDI {
    operator fun invoke(init: AutoDI.() -> Unit) {
        this.init()
    }

    inline fun <reified T : Any> inject(qualifier: String? = null): T {
        val kType = typeOf<T>()
        return publishedSearchLifeCycleType(kType, qualifier)
    }

    @PublishedApi
    internal fun <T> publishedSearchLifeCycleType(kType: KType, qualifier: String?): T =
        AutoDIModuleContainer.searchLifeCycleType(kType, qualifier)
}

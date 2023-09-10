package woowacourse.shopping.util.autoDI.autoDIContainer

import woowacourse.shopping.util.autoDI.ViewModelBundle
import woowacourse.shopping.util.autoDI.autoDIModule.AutoDIModule
import woowacourse.shopping.util.autoDI.autoDIModule.autoDIModule
import kotlin.reflect.KType

class AutoDIModules(value: MutableList<AutoDIModule>) {
    private val _value: MutableList<AutoDIModule> = value
    val value: List<AutoDIModule> get() = _value.toList()

    internal fun addModule(autoDIModule: AutoDIModule) {
        _value.add(autoDIModule)
    }

    internal fun searchModule(qualifier: String): AutoDIModule? =
        value.find { autoDIModule -> autoDIModule.qualifier == qualifier }

    internal fun <T : Any> searchLifeCycleType(kType: KType, qualifier: String?): T? {
        value.forEach { autoDIModule ->
            val eachSearchResult: T? = autoDIModule.searchLifeCycleType(kType, qualifier)
            if (eachSearchResult != null) return eachSearchResult
        }
        return null
    }

    internal fun searchViewModelBundle(kType: KType): ViewModelBundle<*>? {
        value.forEach { autoDIModule ->
            val eachSearchResult: ViewModelBundle<*>? = autoDIModule.searchViewModelBundle(kType)
            if (eachSearchResult != null) return eachSearchResult
        }
        return null
    }
}

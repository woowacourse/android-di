package woowacourse.shopping.util.autoDI

import kotlin.reflect.KProperty

class Injector(val qualifier: String? = null) {
    inline operator fun <reified T : Any> getValue(thisRef: Any?, property: KProperty<*>): T {
        @Suppress("UNCHECKED_CAST")
        return AutoDI.inject(qualifier)
    }
}

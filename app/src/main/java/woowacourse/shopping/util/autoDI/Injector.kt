package woowacourse.shopping.util.autoDI

import kotlin.reflect.KProperty

class Injector<T>(qualifier: String? = null) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        // todo 로직 추가 및 return값 변경
        @Suppress("UNCHECKED_CAST")
        return thisRef as T
    }
}

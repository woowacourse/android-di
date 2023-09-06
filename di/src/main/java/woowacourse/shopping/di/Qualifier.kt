package woowacourse.shopping.di

import kotlin.reflect.KType
import kotlin.reflect.typeOf

class Qualifier {
    val constructors: HashMap<KType, KType> = hashMapOf()
    val providers: HashMap<KType, () -> Any> = hashMapOf()

    inline fun <reified T : Any> provider(noinline init: () -> T) {
        providers[typeOf<T>()] = init
    }

    inline fun <reified T> provider(type: KType) {
        constructors[typeOf<T>()] = type
    }
}

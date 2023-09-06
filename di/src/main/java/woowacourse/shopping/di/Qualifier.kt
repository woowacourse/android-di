package woowacourse.shopping.di

import kotlin.reflect.KType

data class Qualifier(
    val constructors: HashMap<KType, KType> = hashMapOf(),
    val providers: HashMap<KType, () -> Any> = hashMapOf()
)

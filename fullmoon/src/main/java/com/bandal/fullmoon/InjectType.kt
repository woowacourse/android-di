package com.bandal.fullmoon

import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

enum class InjectType {
    NEED_QUALIFY,
    NO_DISTINCTION,
    ;

    companion object {
        fun from(kClass: KClass<*>): InjectType = when (kClass.hasAnnotation<Qualifier>()) {
            true -> NEED_QUALIFY
            false -> NO_DISTINCTION
        }
    }
}

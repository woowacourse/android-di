package com.bandal.fullmoon

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.hasAnnotation

enum class InjectType {
    NEED_QUALIFY,
    NO_DISTINCTION,
    ;

    companion object {
        inline fun <reified T : KAnnotatedElement> from(element: T): InjectType {
            return when (element.hasAnnotation<Qualifier>()) {
                true -> NEED_QUALIFY
                false -> NO_DISTINCTION
            }
        }
    }
}

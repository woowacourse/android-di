package woowacourse.shopping.model

import kotlin.reflect.KClass

internal data class Key(
    val type: KClass<*>,
    val qualifier: KClass<out Annotation>? = null,
) {
    companion object {
        fun from(type: KClass<*>): Key = Key(type = type)

        fun of(
            type: KClass<*>,
            qualifier: KClass<out Annotation>?,
        ): Key = Key(type, qualifier)
    }
}

package woowacourse.shopping.core.di

@Target(
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.PROPERTY,
)
annotation class Qualifier(
    val name: String,
)

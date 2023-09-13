package woowacourse.shopping.di.annotation

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.PROPERTY
)
annotation class Qualifier(val implementation: String)

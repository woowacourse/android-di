package woowacourse.shopping.otterdi.annotation

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.PROPERTY
)
annotation class Qualifier(val implementationName: String)

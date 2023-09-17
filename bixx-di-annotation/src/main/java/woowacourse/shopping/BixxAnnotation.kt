package woowacourse.shopping

@Target(AnnotationTarget.PROPERTY)
annotation class Inject

@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.VALUE_PARAMETER,
)
annotation class Qualifier(val className: String)

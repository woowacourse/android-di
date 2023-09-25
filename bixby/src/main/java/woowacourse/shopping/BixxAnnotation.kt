package woowacourse.shopping

@Target(AnnotationTarget.PROPERTY)
annotation class Inject

@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FUNCTION,
)
annotation class Qualifier(val className: String)

@Target(AnnotationTarget.FUNCTION)
annotation class Singleton

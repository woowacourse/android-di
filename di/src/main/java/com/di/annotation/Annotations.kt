package woowacourse.shopping.data.di.annotation


@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.PROPERTY, AnnotationTarget.CONSTRUCTOR
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.CONSTRUCTOR
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier(val value: String)

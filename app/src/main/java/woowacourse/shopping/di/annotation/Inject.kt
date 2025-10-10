package woowacourse.shopping.di.annotation

@Target(
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.PROPERTY,
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject

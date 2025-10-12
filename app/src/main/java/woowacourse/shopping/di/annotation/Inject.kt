package woowacourse.shopping.di.annotation

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Inject(
    val name: String,
)

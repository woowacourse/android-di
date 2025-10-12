package woowacourse.shopping.di

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier(
    val name: String,
)

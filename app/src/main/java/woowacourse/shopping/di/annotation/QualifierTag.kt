package woowacourse.shopping.di.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class QualifierTag(
    val value: String,
)

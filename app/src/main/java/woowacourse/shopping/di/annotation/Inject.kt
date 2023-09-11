package woowacourse.shopping.di.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CONSTRUCTOR)
annotation class Inject

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class InjectField
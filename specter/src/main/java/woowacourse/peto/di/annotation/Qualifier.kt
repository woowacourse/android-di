package woowacourse.peto.di.annotation

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier(val name: String)

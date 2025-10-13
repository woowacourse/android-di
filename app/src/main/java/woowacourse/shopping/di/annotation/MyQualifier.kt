package woowacourse.shopping.di.annotation

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class MyQualifier(val type: String)

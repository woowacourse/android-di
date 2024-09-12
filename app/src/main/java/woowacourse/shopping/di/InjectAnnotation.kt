package woowacourse.shopping.di

@Retention(AnnotationRetention.RUNTIME)
annotation class Inject

@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier(val value: String)

package woowacourse.shopping.di

annotation class Inject

@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Database

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class InMemory

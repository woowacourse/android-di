package woowacourse.shopping.di

@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier

@Qualifier
annotation class InMemory

@Qualifier
annotation class Database

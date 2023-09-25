package woowacourse.shopping.di.module

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Database

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class InMemory

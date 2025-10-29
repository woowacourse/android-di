package woowacourse.shopping.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class InMemory

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Default
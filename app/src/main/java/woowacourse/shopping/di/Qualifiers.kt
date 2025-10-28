package woowacourse.shopping.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RoomDatabase

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class InMemory

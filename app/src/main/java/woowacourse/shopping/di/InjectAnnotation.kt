package woowacourse.shopping.di

@Retention(AnnotationRetention.RUNTIME)
annotation class Inject

annotation class Qualifier

@Qualifier
annotation class RoomDao

@Qualifier
annotation class InMemoryDao

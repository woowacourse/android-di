package woowacourse.shopping.di.annotation

import com.example.di.annotation.Qualifier

@Qualifier
annotation class RoomDb

@Qualifier
annotation class InMemory

@Qualifier
annotation class RoomDbCartRepository

@Qualifier
annotation class InMemoryCartRepository

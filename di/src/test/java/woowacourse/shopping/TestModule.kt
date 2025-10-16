package woowacourse.shopping

import woowacourse.shopping.annotation.Room
import woowacourse.shopping.annotation.Singleton

object TestModule {
    @Singleton
    fun provideRepository(): TestRepository = InMemoryTestRepository()

    @Room
    fun provideRoomRepository(): TestRepository = RoomTestRepository()
}

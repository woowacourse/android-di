package woowacourse.shopping

import woowacourse.shopping.annotation.Room

object TestModule {
    fun provideRepository(): TestRepository = InMemoryTestRepository()

    @Room
    fun provideRoomRepository(): TestRepository = RoomTestRepository()
}

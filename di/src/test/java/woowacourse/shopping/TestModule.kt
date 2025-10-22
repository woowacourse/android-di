package woowacourse.shopping

import woowacourse.shopping.annotation.Singleton

object TestModule {
    @Singleton
    fun provideRepository(): TestRepository = InMemoryTestRepository()

    @QualifierA
    fun provideRoomRepository(): TestRepository = RoomTestRepository()
}

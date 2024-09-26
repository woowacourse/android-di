package woowacourse.shopping.fake

object FakeModule {
    @FakeRoomDBRepository
    fun provideFakeRoomRepository(): FakeRepository {
        return DefaultRoomFakeRepository
    }

    @FakeInMemoryRepository
    fun provideFakeInMemoryRepository(): FakeRepository {
        return DefaultInMemoryFakeRepository
    }
}

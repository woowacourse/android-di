package woowacourse.shopping.fake

import org.library.haeum.Module

@Module
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
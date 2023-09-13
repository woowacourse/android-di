package woowacourse.shopping.data.di

import androidx.lifecycle.ViewModel

class FakeViewModel(@Inject @RoomDB val fakeRepository: FakeRepository): ViewModel()
package woowacourse.shopping.data.di

import androidx.lifecycle.ViewModel

class FakeViewModel(@Inject val fakeRepository: FakeRepository): ViewModel()
package org.aprilgom.androiddi.fake

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class FakeViewModel(
    @QDefaultFakeRepository
    @Inject
    val fakeRepository: FakeRepository,
) : ViewModel()

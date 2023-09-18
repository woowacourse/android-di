package io.hyemdooly.di

import io.hyemdooly.di.annotation.Inject

class FakeViewModel(val repository: FakeRepository) {
    @Inject
    lateinit var dao: FakeDao
}

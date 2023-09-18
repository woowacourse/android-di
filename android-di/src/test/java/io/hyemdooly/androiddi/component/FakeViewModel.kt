package io.hyemdooly.androiddi.component

import androidx.lifecycle.ViewModel
import io.hyemdooly.androiddi.element.FakeRepository
import io.hyemdooly.androiddi.element.FakeRepositoryInViewModel
import io.hyemdooly.di.annotation.Qualifier

class FakeViewModel(@Qualifier(FakeRepositoryInViewModel::class) repository: FakeRepository) : ViewModel()

package com.example.fake

import FakeApplicationModule
import FakeViewModel
import com.example.di.DIActivity
import com.example.di.DIModule
import com.example.di.viewmodel.provideViewModel

class FakeActivity : DIActivity() {
    val viewModel: FakeViewModel by provideViewModel(FakeApplicationModule::class)

    override val module: DIModule = FakeActivityModule()
}

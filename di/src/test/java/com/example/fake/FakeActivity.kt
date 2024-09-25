package com.example.fake

import FakeViewModel
import com.example.di.DIActivity
import com.example.di.DIModule
import com.example.di.viewmodel.provideViewModel

class FakeActivity : DIActivity() {
    val viewModel: FakeViewModel by provideViewModel(FakeActivityModule::class)

    override val module: DIModule = FakeActivityModule()
}

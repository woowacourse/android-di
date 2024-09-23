package com.example.fake

import FakeModule
import FakeViewModel
import com.example.di.DIActivity
import com.example.di.DIModule
import com.example.di.viewmodel.provideViewModel

class FakeActivity : DIActivity() {
    val viewModel: FakeViewModel by provideViewModel(FakeModule::class)

    override val module: DIModule = FakeModule()
}

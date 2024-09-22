package com.woowa.di.fixture.component

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class ComponentTestViewModel : ViewModel() {
    @Inject
    lateinit var singletonFake: TestSingletonComponent

    @Inject
    lateinit var viewModelFake: TestViewModelComponent
}

class ComponentTestViewModel2 : ViewModel() {

    @Inject
    lateinit var viewModelFake: TestViewModelComponent
}

class FailComponentTestViewModel : ViewModel() {
    lateinit var fake: TestSingletonComponent
}

package com.buna.di.injector.fakeClasses

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import woowacourse.shopping.ui.util.viewModel.viewModel

annotation class AConstructorDependencyQualifier

interface ConstructorDependency

@AConstructorDependencyQualifier
class AConstructorDependency : ConstructorDependency

class ConstructorTestActivity : AppCompatActivity() {
    val viewModel: ConstructorTestViewModel by viewModel()
}

class ConstructorTestViewModel(
    @AConstructorDependencyQualifier
    val constructorDependency: ConstructorDependency,
) : ViewModel()

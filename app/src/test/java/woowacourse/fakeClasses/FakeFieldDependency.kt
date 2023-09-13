package com.buna.di.injector.fakeClasses

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.woowacourse.bunadi.annotation.Inject
import woowacourse.shopping.ui.util.viewModel.viewModel

annotation class AFieldDependencyQualifier

interface FieldDependency

@AFieldDependencyQualifier
class AFieldDependency : FieldDependency

class FieldTestActivity : AppCompatActivity() {
    val viewModel: FieldTestViewModel by viewModel()
}

class FieldTestViewModel : ViewModel() {
    @com.woowacourse.bunadi.annotation.Inject
    @AFieldDependencyQualifier
    lateinit var fieldWithInjectAnnotation: FieldDependency
    private lateinit var fieldWithoutInjectAnnotation: FieldDependency

    fun isFieldWithoutInjectAnnotationInitialized() = ::fieldWithoutInjectAnnotation.isInitialized
}

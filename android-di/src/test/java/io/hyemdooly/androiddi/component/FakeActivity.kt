package io.hyemdooly.androiddi.component

import io.hyemdooly.androiddi.base.HyemdoolyActivity
import io.hyemdooly.androiddi.element.FakeFormatterInActivity
import io.hyemdooly.di.annotation.Inject

class FakeActivity : HyemdoolyActivity() {
    @Inject
    lateinit var formatter: FakeFormatterInActivity
}

package com.woowa.di.fixture.component

import androidx.activity.ComponentActivity
import com.woowa.di.DIActivity
import javax.inject.Inject

@DIActivity
class TestActivity : ComponentActivity() {

    @Inject
    lateinit var activityComponent: TestActivityComponent
}
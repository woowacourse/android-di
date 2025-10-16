package com.daedan.di.fixture

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.daedan.di.util.autoViewModels

class FakeActivity : ComponentActivity() {
    val viewModel by autoViewModels<TestViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel
    }
}

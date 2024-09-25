package com.kmlibs.supplin.fixtures.android.activity

import androidx.appcompat.app.AppCompatActivity
import com.kmlibs.supplin.annotations.SupplinActivity
import com.kmlibs.supplin.fixtures.android.module.FakeActivityUtilModule
import com.kmlibs.supplin.fixtures.android.util.FakeUtil1
import com.kmlibs.supplin.fixtures.android.viewmodel.FakeViewModel1
import com.kmlibs.supplin.fixtures.android.viewmodel.FakeViewModel2
import com.kmlibs.supplin.fixtures.android.viewmodel.FakeViewModel3
import com.kmlibs.supplin.fixtures.android.viewmodel.FakeViewModel4
import com.kmlibs.supplin.fixtures.android.viewmodel.FakeViewModel5
import com.kmlibs.supplin.fixtures.android.viewmodel.FakeViewModel8
import com.kmlibs.supplin.supplinInjection
import com.kmlibs.supplin.viewmodel.supplinViewModel

class FakeActivity1 : AppCompatActivity() {
    val viewModel: FakeViewModel1 by supplinViewModel()
}

class FakeActivity2 : AppCompatActivity() {
    val viewModel: FakeViewModel2 by supplinViewModel()
}

class FakeActivity3 : AppCompatActivity() {
    val viewModel: FakeViewModel3 by supplinViewModel()
}

class FakeActivity4 : AppCompatActivity() {
    val viewModel: FakeViewModel4 by supplinViewModel()
}

class FakeActivity5 : AppCompatActivity() {
    val viewModel: FakeViewModel5 by supplinViewModel()
}

@SupplinActivity([FakeActivityUtilModule::class])
class FakeActivity6 : AppCompatActivity() {
    val fakeUtil1 by supplinInjection<FakeUtil1>()
}

@SupplinActivity([FakeActivityUtilModule::class])
class FakeActivity7 : AppCompatActivity() {
    val fakeViewModel: FakeViewModel8 by supplinViewModel<FakeViewModel8>()
}

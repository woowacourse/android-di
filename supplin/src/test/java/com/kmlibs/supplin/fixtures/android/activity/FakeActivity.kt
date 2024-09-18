package com.kmlibs.supplin.fixtures.android.activity

import androidx.appcompat.app.AppCompatActivity
import com.kmlibs.supplin.fixtures.android.viewmodel.FakeViewModel1
import com.kmlibs.supplin.fixtures.android.viewmodel.FakeViewModel2
import com.kmlibs.supplin.fixtures.android.viewmodel.FakeViewModel3
import com.kmlibs.supplin.fixtures.android.viewmodel.FakeViewModel4
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

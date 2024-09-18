package com.kmlibs.supplin.fixtures.android.fragment

import androidx.fragment.app.Fragment
import com.kmlibs.supplin.fixtures.android.viewmodel.FakeViewModel1
import com.kmlibs.supplin.fixtures.android.viewmodel.FakeViewModel2
import com.kmlibs.supplin.viewmodel.supplinViewModel

class FakeFragment1 : Fragment() {
    val viewModel: FakeViewModel1 by supplinViewModel()
}

class FakeFragment2 : Fragment() {
    val viewModel: FakeViewModel2 by supplinViewModel()
}

package org.aprilgom.androiddi.fake

import androidx.activity.ComponentActivity
import org.aprilgom.androiddi.viewModel

class FakeActivity : ComponentActivity() {
    val fakeViewModel by viewModel<FakeViewModel>()
}

package org.aprilgom.androiddi.fake

import androidx.appcompat.app.AppCompatActivity
import org.aprilgom.androiddi.viewModel

class FakeActivity : AppCompatActivity() {
    val viewModel by viewModel<FakeViewModel>()
}

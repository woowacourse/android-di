package woowacourse.shopping.data.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.ViewModelFactory

class FakeFieldInjectActivity : AppCompatActivity() {
    val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.provide<FakeFieldInjectViewModel>()
        )[FakeFieldInjectViewModel::class.java]
    }
}
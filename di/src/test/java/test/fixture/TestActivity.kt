package test.fixture

import androidx.activity.ComponentActivity
import com.example.di.DependencyInjector
import com.example.di.RequireInjection
import com.example.di.autoViewModel
import com.example.di.scope.ActivityScope

class TestActivity : ComponentActivity() {
    init {
        DependencyInjector.injectField(this, this)
    }

    val viewModel: FakeCartViewModel by autoViewModel()

    @RequireInjection(impl = TestDateFormatter::class, scope = ActivityScope::class)
    lateinit var dateFormatter: TestDateFormatter
}

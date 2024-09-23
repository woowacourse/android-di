package com.example.sh1mj1.stub

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.sh1mj1.annotation.Inject
import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.annotation.ViewModelScope
import com.example.sh1mj1.component.activityscope.injectActivityScopeComponent
import com.example.sh1mj1.injectedSh1mj1ViewModel
import woowacourse.shopping.ui.cart.DateFormatter

class StubActivity : ComponentActivity() {
    val viewModel by injectedSh1mj1ViewModel<StubViewModel>()
    val dateFormatter by injectActivityScopeComponent<DateFormatter>()

    fun useViewModelFunction() {
        viewModel.printAll()
    }
}

class StubDateFormatter(context: Context) : LifecycleEventObserver, DateFormatter {
    override fun onStateChanged(
        source: LifecycleOwner,
        event: Lifecycle.Event,
    ) {
    }

    override fun formatDate(timestamp: Long): String = STUB_FORMAT

    companion object {
        private const val STUB_FORMAT = "Stub"
    }
}

class StubViewModel : ViewModel() {
    @Inject
    @Qualifier("singleton")
    lateinit var repo1: StubRepo

    @Inject
    @Qualifier("viewModelScope")
    @ViewModelScope
    lateinit var repo2: StubRepo

    fun printAll() {
        repo1.all().forEach(::println)
        repo2.all().forEach(::println)
    }
}

interface StubRepo {
    fun all(): List<String>
}

class DefaultStubRepo : StubRepo {
    override fun all(): List<String> = listOf("11", "22")
}

class ViewModelScopeStubRepo : StubRepo {
    override fun all(): List<String> = listOf("33", "44")
}

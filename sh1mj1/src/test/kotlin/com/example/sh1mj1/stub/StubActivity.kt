package com.example.sh1mj1.stub

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.sh1mj1.annotation.Inject
import com.example.sh1mj1.annotation.ViewModelScope
import com.example.sh1mj1.component.activityscope.injectActivityScopeComponent
import com.example.sh1mj1.injectedSh1mj1ViewModel
import woowacourse.shopping.ui.cart.DateFormatter

class StubActivity : ComponentActivity() {
    // TODO: StubActivity 가 Activity 를 상속받을 때는 injectedSh1mj1ViewModel 를 사용할 수 없지만, AppCompatActivity 상속받으니 ㄱㅊ
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

// TODO: [ ] ProductRepository 는 ViewModel LifeCycle 동안 유지되도록 구현한다.
class StubViewModel : ViewModel() {
    @Inject
    @ViewModelScope
    lateinit var repo: StubRepo

    fun printAll() {
        repo.all().forEach(::println)
    }
}

interface StubRepo {
    fun all(): List<String>
}

class DefaultStubRepo : StubRepo {
    override fun all(): List<String> = listOf("11", "22")
}
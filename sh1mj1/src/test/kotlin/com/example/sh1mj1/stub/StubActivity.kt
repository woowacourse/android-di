package com.example.sh1mj1.stub

import android.app.Activity
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.sh1mj1.component.activityscope.injectActivityScopeComponent
import woowacourse.shopping.ui.cart.DateFormatter

class StubActivity : Activity() {
    val dateFormatter by injectActivityScopeComponent<DateFormatter>()
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

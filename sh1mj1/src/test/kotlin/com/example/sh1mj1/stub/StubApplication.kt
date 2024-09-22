package com.example.sh1mj1.stub

import com.example.sh1mj1.DiApplication
import com.example.sh1mj1.component.activityscope.activityScopeComponent
import woowacourse.shopping.ui.cart.DateFormatter

class StubApplication : DiApplication() {
    override fun onCreate() {
        super.onCreate()

        activityContainer.add(
            activityScopeComponent<DateFormatter>(::StubDateFormatter),
        )
    }
}
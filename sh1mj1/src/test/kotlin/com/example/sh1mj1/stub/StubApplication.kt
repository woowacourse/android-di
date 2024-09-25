package com.example.sh1mj1.stub

import com.example.sh1mj1.DiApplication
import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.component.activityscope.activityScopeComponent
import com.example.sh1mj1.component.singleton.singletonComponent
import com.example.sh1mj1.component.viewmodelscope.viewModelScopeComponent
import woowacourse.shopping.ui.cart.DateFormatter

class StubApplication : DiApplication() {
    override fun onCreate() {
        super.onCreate()

        container.add(
            singletonComponent<StubRepo>(DefaultStubRepo(), Qualifier("singleton")),
        )
        container.add(
            viewModelScopeComponent<StubRepo>(ViewModelScopeStubRepo(), Qualifier("viewModelScope")),
        )

        activityContainer.add(
            activityScopeComponent<DateFormatter>({ context ->
                StubDateFormatter(context) }
            ),
        )
    }
}

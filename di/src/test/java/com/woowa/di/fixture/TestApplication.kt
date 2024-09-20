package com.woowa.di.fixture

import android.app.Application
import com.woowa.di.component.injectDI
import com.woowa.di.fixture.component.SingletonComponentTestBinder
import com.woowa.di.fixture.component.ViewModelComponentTestBinder
import com.woowa.di.fixture.qualifier.QualifierTestBinder

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        injectDI(this) {
            binder(ViewModelComponentTestBinder::class)
            binder(SingletonComponentTestBinder::class)
            binder(QualifierTestBinder::class)
        }
    }
}

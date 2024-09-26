package com.zzang.di.testfixture

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.zzang.di.DIContainer
import com.zzang.di.DependencyInjector
import com.zzang.di.annotation.Inject
import com.zzang.di.provideViewModel

class FakeActivity : ComponentActivity() {
    @Inject
    lateinit var fakeService: FakeService
    val viewModel: FakeViewModel by provideViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        DependencyInjector.injectDependencies(this, this)
        fakeService.doSomething()
    }

    override fun onDestroy() {
        super.onDestroy()
        DIContainer.clearActivityScopedInstances(this)
    }
}

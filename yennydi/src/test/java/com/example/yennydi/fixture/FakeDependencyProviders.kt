package com.example.yennydi.fixture

import com.example.yennydi.di.DependencyContainer
import com.example.yennydi.di.DependencyProvider

class FakeApplicationDependencyProvider : DependencyProvider {
    override fun register(container: DependencyContainer) {
        container.addDeferredDependency(ApplicationScopeComponent::class to ApplicationScopeComponentImpl::class)
    }
}

class FakeActivityDependencyProvider : DependencyProvider {
    override fun register(container: DependencyContainer) {
        container.addDeferredDependency(ActivityScopeComponent::class to ActivityScopeComponent::class)
    }
}

class FakeViewModelDependencyProvider : DependencyProvider {
    override fun register(container: DependencyContainer) {
        container.addDeferredDependency(ViewModelScopeComponent::class to ViewModelScopeComponent::class)
    }
}

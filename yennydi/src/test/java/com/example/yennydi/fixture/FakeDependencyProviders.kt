package com.example.yennydi.fixture

import com.example.yennydi.di.DependencyContainer
import com.example.yennydi.di.DependencyProvider

class FakeApplicationDependencyProvider : DependencyProvider {
    override fun register(container: DependencyContainer) {
        container.addDeferredDependency(FakeRepository::class to FakeRepositoryImpl::class)
    }
}

class FakeDependencyProvider : DependencyProvider {
    override fun register(container: DependencyContainer) {
        container.addDeferredDependency(FakeInjectedComponent::class to FakeInjectedComponent::class)
    }
}

package com.example.yennydi.di.fixture

import com.example.yennydi.di.Injected
import com.example.yennydi.di.Qualifier

@Qualifier
annotation class A

@Qualifier
annotation class B

interface SingleDependency

class SingleDependencyImpl : SingleDependency

class InjectedClass(
    @Injected val dependency: SingleDependency,
)

interface MultipleDependency

@A
class MultipleDependencyImplA : MultipleDependency

@B
class MultipleDependencyImplB : MultipleDependency

class NonQualifierInjectedClass(
    @Injected val dependency: MultipleDependency,
)

class QualifierInjectedClass(
    @Injected @A val dependencyA: MultipleDependency,
)

class MultiQualifierInjectedClass(
    @Injected @A val dependencyA: MultipleDependency,
    @Injected @B val dependencyB: MultipleDependency,
)

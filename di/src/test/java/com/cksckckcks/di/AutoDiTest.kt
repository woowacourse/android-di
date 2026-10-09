package com.cksckckcks.di

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertSame

class AutoDiTest {
    @Test
    fun `생성자와 필드의 Qualifier로 구현체를 선택한다`() {
        val container = createContainer()
        val autoDi = AutoDi(container)

        val constructorTarget = autoDi.createInstance(ConstructorTarget::class)
        val propertyTarget = autoDi.createInstance(PropertyTarget::class)

        assertIs<LocalStorage>(constructorTarget.storage)
        assertIs<MemoryStorage>(propertyTarget.storage)
        assertSame(container.getInstance(Storage::class, Local::class), constructorTarget.storage)
        assertSame(container.getInstance(Storage::class, Memory::class), propertyTarget.storage)
    }

    @Test
    fun `Qualifier 없이 두 구현체를 직접 조회하면 후보를 알리는 오류를 낸다`() {
        val container = createContainer()

        val error = assertFailsWith<IllegalArgumentException> { container.getInstance(Storage::class) }

        assertContains(error.message.orEmpty(), "Storage")
        assertContains(error.message.orEmpty(), "Local")
        assertContains(error.message.orEmpty(), "Memory")
    }

    @Test
    fun `Qualifier 없이 생성자에 주입하면 모호성 오류를 낸다`() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                AutoDi(createContainer()).createInstance(UnqualifiedTarget::class)
            }

        assertContains(error.message.orEmpty(), "Storage")
        assertContains(error.message.orEmpty(), "Qualifier")
    }

    @Test
    fun `한 주입 지점에 Qualifier가 둘이면 오류를 낸다`() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                AutoDi(createContainer()).createInstance(MultipleQualifiersTarget::class)
            }

        assertContains(error.message.orEmpty(), "Qualifier는 하나만")
    }

    @Test
    fun `생성자 직접 순환 의존성을 경로를 포함한 오류로 알린다`() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                AutoDi(DiContainer()).createInstance(DirectCycleA::class)
            }

        assertContains(error.message.orEmpty(), "DirectCycleA → DirectCycleB → DirectCycleA")
    }

    @Test
    fun `생성자 간접 순환 의존성을 경로를 포함한 오류로 알린다`() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                AutoDi(DiContainer()).createInstance(IndirectCycleA::class)
            }

        assertContains(error.message.orEmpty(), "IndirectCycleA → IndirectCycleB → IndirectCycleC → IndirectCycleA")
    }

    @Test
    fun `필드 주입 순환 의존성을 경로를 포함한 오류로 알린다`() {
        val error =
            assertFailsWith<IllegalArgumentException> {
                AutoDi(DiContainer()).createInstance(FieldCycleA::class)
            }

        assertContains(error.message.orEmpty(), "FieldCycleA → FieldCycleB → FieldCycleA")
    }

    @Test
    fun `서로 다른 의존성이 같은 객체를 공유해도 순환으로 판단하지 않는다`() {
        val instance = AutoDi(DiContainer()).createInstance(SharedDependencyRoot::class)

        assertSame(instance.left.shared, instance.right.shared)
    }

    private fun createContainer() =
        DiContainer().apply {
            register(Storage::class, Local::class) { LocalStorage() }
            register(Storage::class, Memory::class) { MemoryStorage() }
        }

    interface Storage

    class LocalStorage : Storage

    class MemoryStorage : Storage

    @Qualifier
    @Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Local

    @Qualifier
    @Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Memory

    class ConstructorTarget(
        @Local val storage: Storage,
    )

    class PropertyTarget {
        @InjectProperty
        @Memory
        lateinit var storage: Storage
    }

    class UnqualifiedTarget(
        val storage: Storage,
    )

    class MultipleQualifiersTarget(
        @Local @Memory val storage: Storage,
    )

    class DirectCycleA(
        val dependency: DirectCycleB,
    )

    class DirectCycleB(
        val dependency: DirectCycleA,
    )

    class IndirectCycleA(
        val dependency: IndirectCycleB,
    )

    class IndirectCycleB(
        val dependency: IndirectCycleC,
    )

    class IndirectCycleC(
        val dependency: IndirectCycleA,
    )

    class FieldCycleA {
        @InjectProperty
        lateinit var dependency: FieldCycleB
    }

    class FieldCycleB {
        @InjectProperty
        lateinit var dependency: FieldCycleA
    }

    class SharedDependencyRoot(
        val left: SharedDependencyLeft,
        val right: SharedDependencyRight,
    )

    class SharedDependencyLeft(
        val shared: SharedDependency,
    )

    class SharedDependencyRight(
        val shared: SharedDependency,
    )

    class SharedDependency
}

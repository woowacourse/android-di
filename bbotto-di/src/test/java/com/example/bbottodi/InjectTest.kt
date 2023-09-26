package com.example.bbottodi

import com.example.bbottodi.di.Container
import com.example.bbottodi.di.inject.AutoDependencyInjector
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class InjectTest {
    private lateinit var applicationContainer: Container
    private lateinit var activityContainer: Container

    @Before
    fun setUp() {
        applicationContainer = Container(null, FakeApplicationModule())
        activityContainer = Container(applicationContainer, FakeActivityModule())
    }

    @Test
    fun `inject 어노테이션 있을 때 의존성 주입 후 인스턴스 생성 성공`() {
        // when
        val viewModel = AutoDependencyInjector.inject<FakeViewModelWithInjectOnSuccess>(
            activityContainer,
            FakeViewModelWithInjectOnSuccess::class,
        )

        // then
        val expect = FakeViewModelWithInjectOnSuccess::class
        val actual = viewModel::class
        assertEquals(expect, actual)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `inject 어노테이션 없을 때 의존성 주입 후 인스턴스 생성 실패`() {
        // when
        val viewModel = AutoDependencyInjector.inject<FakeViewModelWithInjectOnFailure>(
            activityContainer,
            FakeViewModelWithInjectOnFailure::class,
        )

        // then
        val expect = FakeViewModelWithInjectOnFailure::class
        val actual = viewModel::class
        assertEquals(expect, actual)
    }

    @Test
    fun `@inject 가 붙은 필드의 의존성 주입 성공`() {
        // when
        val viewModel = AutoDependencyInjector.inject<FakeViewModelWithFieldInjectOnSuccess>(
            activityContainer,
            FakeViewModelWithFieldInjectOnSuccess::class,
        )

        // then
        val expect = FakeViewModelWithFieldInjectOnSuccess::class
        val actual = viewModel::class
        assertEquals(expect, actual)
    }

    @Test
    fun `재귀 의존성 주입 성공`() {
        // when
        val viewModel = AutoDependencyInjector.inject<FakeViewModelWithRecursiveInject>(
            activityContainer,
            FakeViewModelWithRecursiveInject::class,
        )

        // then
        val expect = FakeViewModelWithRecursiveInject::class
        val actual = viewModel::class
        assertEquals(expect, actual)
    }

    @Test
    fun `Qualifier로 @InDisk 구분하여 의존성 주입 성공`() {
        // when
        val viewModel = AutoDependencyInjector.inject<FakeViewModelWithQualifier>(
            activityContainer,
            FakeViewModelWithQualifier::class,
        )

        // then
        val expect = FakeViewModelWithQualifier::class
        val actual = viewModel::class
        assertEquals(expect, actual)
    }

    @Test
    fun `Qualifier로 @InDisk 구분하여 필드에 의존성 주입 성공`() {
        // when
        val viewModel = AutoDependencyInjector.inject<FakeViewModelWithQualifierFieldInject>(
            activityContainer,
            FakeViewModelWithQualifierFieldInject::class,
        )

        // then
        val expect = FakeViewModelWithQualifierFieldInject::class
        val actual = viewModel::class
        assertEquals(expect, actual)
    }

    @Test
    fun `Qualifier로 @InDisk @InMemory 구분해 주입해주기`() {
        // when
        val viewModel = AutoDependencyInjector.inject<FakeViewModelWithInDiskAndInMemory>(
            activityContainer,
            FakeViewModelWithInDiskAndInMemory::class,
        )

        // then
        val expect = FakeViewModelWithInDiskAndInMemory::class
        val actual = viewModel::class
        assertEquals(expect, actual)
    }
}

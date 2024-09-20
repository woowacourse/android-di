package com.example.alsonglibrary2

import com.example.alsonglibrary2.di.AutoDIManager
import com.example.alsonglibrary2.fixtures.QualifiedDependencyProvider
import com.example.alsonglibrary2.fixtures.activity.FakeActivity1
import com.example.alsonglibrary2.fixtures.activity.FakeActivity2
import com.example.alsonglibrary2.fixtures.activity.FakeActivity3
import com.example.alsonglibrary2.fixtures.activity.FakeActivity4
import com.example.alsonglibrary2.fixtures.activity.FakeActivity5
import com.example.alsonglibrary2.fixtures.activity.FakeActivity6
import com.example.alsonglibrary2.fixtures.activity.FakeActivity7
import com.example.alsonglibrary2.fixtures.instance.defaultFakeRepository1
import com.example.alsonglibrary2.fixtures.instance.defaultFakeRepository2
import com.example.alsonglibrary2.fixtures.repository.FakeRepository
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AutoDIManagerTest {
    @Before
    fun setUp() {
        AutoDIManager.clearDependencies()
    }

    @Test
    fun `생성자에 등록된 의존성을 주입할 수 있다`() {
        // given
        AutoDIManager.registerDependency<FakeRepository>(defaultFakeRepository1)
        val activity = Robolectric.buildActivity(FakeActivity1::class.java).create().get()

        // then
        assertThat(activity.viewModel).isNotNull()
        assertThat(activity.viewModel.fakeRepository).isEqualTo(defaultFakeRepository1)
    }

    @Test(expected = Exception::class)
    fun `등록된 의존성이 존재하지 않으면 의존성 주입에 실패한다`() {
        // given - Qualifier가 없는 생성자 주입
        val activity = Robolectric.buildActivity(FakeActivity1::class.java).create().get()

        // then
        activity.viewModel
    }

    @Test
    fun `생성자에 Qualifier로 지정된 의존성을 주입할 수 있다`() {
        // given
        AutoDIManager.provider = QualifiedDependencyProvider
        val activity = Robolectric.buildActivity(FakeActivity2::class.java).create().get()

        // then
        assertThat(activity.viewModel).isNotNull()
        assertThat(activity.viewModel.fakeRepository).isEqualTo(defaultFakeRepository1)
    }

    @Test
    fun `필드에 등록된 의존성을 주입할 수 있다`() {
        // given
        AutoDIManager.registerDependency<FakeRepository>(defaultFakeRepository1)
        val activity = Robolectric.buildActivity(FakeActivity3::class.java).create().get()


        // then
        assertThat(activity.viewModel).isNotNull()
        assertThat(activity.viewModel.fakeRepository).isEqualTo(defaultFakeRepository1)
    }

    @Test
    fun `필드에 Qualifier로 지정된 의존성을 주입할 수 있다`() {
        // given
        AutoDIManager.provider = QualifiedDependencyProvider
        val activity = Robolectric.buildActivity(FakeActivity4::class.java).create().get()

        // then
        assertThat(activity.viewModel).isNotNull()
        assertThat(activity.viewModel.fakeRepository).isEqualTo(defaultFakeRepository1)
    }

    @Test(expected = Exception::class)
    fun `의존성이 등록되어있지 않고 Qualifier로 지정된 의존성도 없으면 의존성 주입이 실패한다`() {
        // given
        AutoDIManager.provider = QualifiedDependencyProvider
        val activity = Robolectric.buildActivity(FakeActivity5::class.java).create().get()

        // then
        activity.viewModel
    }

    @Test
    fun `필드에 같은 타입의 의존성이 여러개 있을 때 Qualifier로 인스턴스를 다르게 주입할 수 있다`() {
        // given
        AutoDIManager.provider = QualifiedDependencyProvider
        val activity = Robolectric.buildActivity(FakeActivity6::class.java).create().get()

        // then
        assertThat(activity.viewModel).isNotNull()
        assertThat(activity.viewModel.fakeRepository1).isEqualTo(defaultFakeRepository1)
        assertThat(activity.viewModel.fakeRepository2).isEqualTo(defaultFakeRepository2)
    }

    @Test
    fun `생성자에 같은 타입의 의존성이 여러개 있을 때 Qualifier로 인스턴스를 다르게 주입할 수 있다`() {
        // given
        AutoDIManager.provider = QualifiedDependencyProvider
        val activity = Robolectric.buildActivity(FakeActivity7::class.java).create().get()

        // then
        assertThat(activity.viewModel).isNotNull()
        assertThat(activity.viewModel.fakeRepository1).isEqualTo(defaultFakeRepository1)
        assertThat(activity.viewModel.fakeRepository2).isEqualTo(defaultFakeRepository2)
    }
}

package com.example.alsonglibrary2

import androidx.appcompat.app.AppCompatActivity
import com.example.alsonglibrary2.di.AutoDIManager
import com.example.alsonglibrary2.di.createAutoDIViewModel
import com.example.alsonglibrary2.fixtures.FakeDependencyProvider
import com.example.alsonglibrary2.fixtures.dao.FakeDao
import com.example.alsonglibrary2.fixtures.instance.defaultFakeRepository1
import com.example.alsonglibrary2.fixtures.instance.defaultFakeRepository2
import com.example.alsonglibrary2.fixtures.instance.defaultFakeRepository3
import com.example.alsonglibrary2.fixtures.instance.fakeDao
import com.example.alsonglibrary2.fixtures.repository.FakeRepository
import com.example.alsonglibrary2.fixtures.viewmodel.ConstructorInjectedWithQualifierFakeViewModel
import com.example.alsonglibrary2.fixtures.viewmodel.ConstructorInjectedWithTwoQualifierFakeViewModel
import com.example.alsonglibrary2.fixtures.viewmodel.ConstructorInjectedWithoutQualifierFakeViewModel
import com.example.alsonglibrary2.fixtures.viewmodel.ConstructorRecursiveInjectedFakeViewModel
import com.example.alsonglibrary2.fixtures.viewmodel.FieldInjectedOnlyAnnotationFakeViewModel
import com.example.alsonglibrary2.fixtures.viewmodel.FieldInjectedWithQualifierAndNoProviderFakeViewModel
import com.example.alsonglibrary2.fixtures.viewmodel.FieldInjectedWithQualifierFakeViewModel
import com.example.alsonglibrary2.fixtures.viewmodel.FieldInjectedWithTwoQualifierFakeViewModel
import com.example.alsonglibrary2.fixtures.viewmodel.FieldInjectedWithoutQualifierFakeViewModel
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

        class FakeActivity1 : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<ConstructorInjectedWithoutQualifierFakeViewModel>()
        }
        val activity = Robolectric.buildActivity(FakeActivity1::class.java).create().get()

        // then
        assertThat(activity.viewModel).isNotNull()
        assertThat(activity.viewModel.fakeRepository).isEqualTo(defaultFakeRepository1)
    }

    @Test(expected = Exception::class)
    fun `등록된 의존성이 존재하지 않으면 의존성 주입에 실패한다`() {
        // given
        class FakeActivity1 : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<ConstructorInjectedWithoutQualifierFakeViewModel>()
        }
        val activity = Robolectric.buildActivity(FakeActivity1::class.java).create().get()

        // then
        activity.viewModel
    }

    @Test
    fun `생성자에 Qualifier로 지정된 의존성을 주입할 수 있다`() {
        // given
        AutoDIManager.provider = FakeDependencyProvider

        class FakeActivity2 : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<ConstructorInjectedWithQualifierFakeViewModel>()
        }
        val activity = Robolectric.buildActivity(FakeActivity2::class.java).create().get()

        // then
        assertThat(activity.viewModel).isNotNull()
        assertThat(activity.viewModel.fakeRepository).isEqualTo(defaultFakeRepository1)
    }

    @Test
    fun `필드에 등록된 의존성을 주입할 수 있다`() {
        // given
        AutoDIManager.registerDependency<FakeRepository>(defaultFakeRepository1)

        class FakeActivity3 : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<FieldInjectedWithoutQualifierFakeViewModel>()
        }
        val activity = Robolectric.buildActivity(FakeActivity3::class.java).create().get()

        // then
        assertThat(activity.viewModel).isNotNull()
        assertThat(activity.viewModel.fakeRepository).isEqualTo(defaultFakeRepository1)
    }

    @Test
    fun `필드에 Qualifier로 지정된 의존성을 주입할 수 있다`() {
        // given
        AutoDIManager.provider = FakeDependencyProvider

        class FakeActivity4 : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<FieldInjectedWithQualifierFakeViewModel>()
        }
        val activity = Robolectric.buildActivity(FakeActivity4::class.java).create().get()

        // then
        assertThat(activity.viewModel).isNotNull()
        assertThat(activity.viewModel.fakeRepository).isEqualTo(defaultFakeRepository1)
    }

    @Test(expected = Exception::class)
    fun `의존성이 등록되어있지 않고 Qualifier로 지정된 의존성도 없으면 의존성 주입이 실패한다`() {
        // given
        AutoDIManager.provider = FakeDependencyProvider

        class FakeActivity5 : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<FieldInjectedWithQualifierAndNoProviderFakeViewModel>()
        }
        val activity = Robolectric.buildActivity(FakeActivity5::class.java).create().get()

        // then
        activity.viewModel
    }

    @Test
    fun `필드에 같은 타입의 의존성이 여러개 있을 때 Qualifier로 인스턴스를 다르게 주입할 수 있다`() {
        // given
        AutoDIManager.provider = FakeDependencyProvider

        class FakeActivity6 : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<FieldInjectedWithTwoQualifierFakeViewModel>()
        }
        val activity = Robolectric.buildActivity(FakeActivity6::class.java).create().get()

        // then
        assertThat(activity.viewModel).isNotNull()
        assertThat(activity.viewModel.fakeRepository1).isEqualTo(defaultFakeRepository1)
        assertThat(activity.viewModel.fakeRepository2).isEqualTo(defaultFakeRepository2)
    }

    @Test
    fun `생성자에 같은 타입의 의존성이 여러개 있을 때 Qualifier로 인스턴스를 다르게 주입할 수 있다`() {
        // given
        AutoDIManager.provider = FakeDependencyProvider

        class FakeActivity7 : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<ConstructorInjectedWithTwoQualifierFakeViewModel>()
        }
        val activity = Robolectric.buildActivity(FakeActivity7::class.java).create().get()

        // then
        assertThat(activity.viewModel).isNotNull()
        assertThat(activity.viewModel.fakeRepository1).isEqualTo(defaultFakeRepository1)
        assertThat(activity.viewModel.fakeRepository2).isEqualTo(defaultFakeRepository2)
    }

    @Test(expected = Exception::class)
    fun `필드에 FieldInject어노테이션이 붙은 프로퍼티에만 주입한다`() {
        // given
        AutoDIManager.registerDependency<FakeRepository>(defaultFakeRepository1)

        class FakeActivity8 : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<FieldInjectedOnlyAnnotationFakeViewModel>()
        }
        val activity = Robolectric.buildActivity(FakeActivity8::class.java).create().get()

        // then
        assertThat(activity.viewModel).isNotNull()
        activity.viewModel.fakeRepository1
        assertThat(activity.viewModel.fakeRepository2).isEqualTo(defaultFakeRepository2)
    }

    @Test
    fun `Recursive 자동주입을 할 수 있다`() {
        // given
        AutoDIManager.registerDependency<FakeDao>(fakeDao)
        AutoDIManager.registerDependency<FakeRepository>(defaultFakeRepository3)
        class FakeActivity9 : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<ConstructorRecursiveInjectedFakeViewModel>()
        }
        val activity = Robolectric.buildActivity(FakeActivity9::class.java).create().get()

        // then
        assertThat(activity.viewModel).isNotNull()
        assertThat(activity.viewModel.fakeRepository).isEqualTo(defaultFakeRepository3)
    }
}

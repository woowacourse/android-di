package com.example.alsonglibrary2

import androidx.appcompat.app.AppCompatActivity
import com.example.alsonglibrary2.di.AutoDIManager
import com.example.alsonglibrary2.di.createAutoDIViewModel
import com.example.alsonglibrary2.fixtures.FakeQualifierDependencyProvider
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
import com.example.alsonglibrary2.fixtures.viewmodel.LifecycleFakeViewModel
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

        class FakeActivity : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<ConstructorInjectedWithoutQualifierFakeViewModel>()
        }

        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        // then
        assertThat(activity.viewModel).isNotNull()
        assertThat(activity.viewModel.fakeRepository).isEqualTo(defaultFakeRepository1)
    }

    @Test(expected = Exception::class)
    fun `등록된 의존성이 존재하지 않으면 의존성 주입에 실패한다`() {
        // given
        class FakeActivity : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<ConstructorInjectedWithoutQualifierFakeViewModel>()
        }

        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        // then
        activity.viewModel
    }

    @Test
    fun `생성자에 Qualifier로 지정된 의존성을 주입할 수 있다`() {
        // given
        AutoDIManager.qualifierDependencyProvider = FakeQualifierDependencyProvider

        class FakeActivity : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<ConstructorInjectedWithQualifierFakeViewModel>()
        }

        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        // then
        assertThat(activity.viewModel).isNotNull()
        assertThat(activity.viewModel.fakeRepository).isEqualTo(defaultFakeRepository1)
    }

    @Test
    fun `필드에 등록된 의존성을 주입할 수 있다`() {
        // given
        AutoDIManager.registerDependency<FakeRepository>(defaultFakeRepository1)

        class FakeActivity : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<FieldInjectedWithoutQualifierFakeViewModel>()
        }

        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        // then
        assertThat(activity.viewModel).isNotNull()
        assertThat(activity.viewModel.fakeRepository).isEqualTo(defaultFakeRepository1)
    }

    @Test
    fun `필드에 Qualifier로 지정된 의존성을 주입할 수 있다`() {
        // given
        AutoDIManager.qualifierDependencyProvider = FakeQualifierDependencyProvider

        class FakeActivity : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<FieldInjectedWithQualifierFakeViewModel>()
        }

        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        // then
        assertThat(activity.viewModel).isNotNull()
        assertThat(activity.viewModel.fakeRepository).isEqualTo(defaultFakeRepository1)
    }

    @Test(expected = Exception::class)
    fun `의존성이 등록되어있지 않고 Qualifier로 지정된 의존성도 없으면 의존성 주입이 실패한다`() {
        // given
        AutoDIManager.qualifierDependencyProvider = FakeQualifierDependencyProvider

        class FakeActivity : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<FieldInjectedWithQualifierAndNoProviderFakeViewModel>()
        }

        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        // then
        activity.viewModel
    }

    @Test
    fun `필드에 같은 타입의 의존성이 여러개 있을 때 Qualifier로 인스턴스를 다르게 주입할 수 있다`() {
        // given
        AutoDIManager.qualifierDependencyProvider = FakeQualifierDependencyProvider

        class FakeActivity : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<FieldInjectedWithTwoQualifierFakeViewModel>()
        }

        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        // then
        assertThat(activity.viewModel).isNotNull()
        assertThat(activity.viewModel.fakeRepository1).isEqualTo(defaultFakeRepository1)
        assertThat(activity.viewModel.fakeRepository2).isEqualTo(defaultFakeRepository2)
    }

    @Test
    fun `생성자에 같은 타입의 의존성이 여러개 있을 때 Qualifier로 인스턴스를 다르게 주입할 수 있다`() {
        // given
        AutoDIManager.qualifierDependencyProvider = FakeQualifierDependencyProvider

        class FakeActivity : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<ConstructorInjectedWithTwoQualifierFakeViewModel>()
        }

        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        // then
        assertThat(activity.viewModel).isNotNull()
        assertThat(activity.viewModel.fakeRepository1).isEqualTo(defaultFakeRepository1)
        assertThat(activity.viewModel.fakeRepository2).isEqualTo(defaultFakeRepository2)
    }

    @Test(expected = Exception::class)
    fun `필드에 FieldInject어노테이션이 붙은 프로퍼티에만 주입한다`() {
        // given
        AutoDIManager.registerDependency<FakeRepository>(defaultFakeRepository1)

        class FakeActivity : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<FieldInjectedOnlyAnnotationFakeViewModel>()
        }

        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

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

        class FakeActivity : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<ConstructorRecursiveInjectedFakeViewModel>()
        }

        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        // then
        assertThat(activity.viewModel).isNotNull()
        assertThat(activity.viewModel.fakeRepository).isEqualTo(defaultFakeRepository3)
    }

    @Test
    fun `ViewModelScope 어노테이션이 붙은 프로퍼티는 뷰모델 clear시 인스턴스가 사라진다`() {
        // given
        AutoDIManager.registerDependency<FakeRepository>(defaultFakeRepository1)

        class FakeActivity : AppCompatActivity() {
            val viewModel by createAutoDIViewModel<LifecycleFakeViewModel>()
        }

        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        // when
        activity.viewModel.alsongClear()

        // then
        assertThat(AutoDIManager.dependencies[FakeRepository::class]).isNull()
    }
}

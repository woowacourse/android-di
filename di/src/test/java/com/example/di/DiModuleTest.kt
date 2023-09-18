package com.example.di

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import androidx.test.core.app.ApplicationProvider
import com.example.di.activity.DiEntryPointActivity
import com.example.di.annotation.FieldInject
import com.example.di.application.DiApplication
import com.example.di.module.ActivityRetainedModule
import com.example.di.module.ApplicationModule
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeApplication::class)
internal class DiModuleTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var application: Context
    private lateinit var applicationModule: ApplicationModule

    @Before
    fun setup() {
        application = ApplicationProvider.getApplicationContext<DiApplication>()
        applicationModule = FakeApplicationModule(application)
    }

    @Test
    fun `가시성이 공개된 필드인 productRepository를 필드 주입 받을 수 있다`() {
        // given
        class FakeViewModel : ViewModel() {
            @FieldInject
            lateinit var productRepository: FakeProductRepository
        }

        class FakeActivityRetainedModule(activityContext: Context, applicationModule: ApplicationModule) :
            ActivityRetainedModule(activityContext, applicationModule) {
            fun getFakeDefaultProductRepository(): FakeProductRepository {
                return FakeDefaultProductRepository()
            }
        }

        class FakeActivity : DiEntryPointActivity(FakeActivityRetainedModule::class.java) {
            val viewModel by viewModel<FakeViewModel>()
        }

        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()
        val activityModule = FakeActivityRetainedModule(activity, applicationModule)

        // when
        val viewModel = activityModule.provideInstance(FakeViewModel::class.java)

        // then
        assertThat(viewModel).isInstanceOf(FakeViewModel::class.java)
        assertDoesNotThrow { viewModel.productRepository }
    }

    @Test
    fun `가시성이 공개되지 않은 필드인 productRepository는 필드주입 받을 수 없다`() {
        // given
        class FakeViewModel : ViewModel() {
            @FieldInject
            private lateinit var productRepository: FakeProductRepository
        }

        class FakeActivityRetainedModule(activityContext: Context, applicationModule: ApplicationModule) :
            ActivityRetainedModule(activityContext, applicationModule) {
            fun getFakeDefaultProductRepository(): FakeProductRepository {
                return FakeDefaultProductRepository()
            }
        }

        class FakeActivity : DiEntryPointActivity(FakeActivityRetainedModule::class.java) {
            val viewModel by viewModel<FakeViewModel>()
        }

        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()
        val activityModule = FakeActivityRetainedModule(activity, applicationModule)

        // then
        assertThatThrownBy { activityModule.provideInstance(FakeViewModel::class.java) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("필드 주입을 받으려는 productRepository의 가시성이 공개되어 있지 않습니다.")
    }

    @Test
    fun `두 가지 이상 모듈 내 함수와 매칭되는데, 퀄리파이어가 지정되어 있지 않다면, 주입받을 수 없다`() {
        // given
        class FakeViewModel(
            private val productRepository: FakeProductRepository,
        ) : ViewModel()

        class FakeActivityRetainedModule(activityContext: Context, applicationModule: ApplicationModule) :
            ActivityRetainedModule(activityContext, applicationModule) {
            fun getProductRepository1(): FakeProductRepository {
                return FakeDefaultProductRepository()
            }

            fun getProductRepository2(): FakeProductRepository {
                return FakeDefaultProductRepository()
            }
        }

        class FakeActivity : DiEntryPointActivity(FakeActivityRetainedModule::class.java) {
            val viewModel by viewModel<FakeViewModel>()
        }

        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()
        val activityModule = FakeActivityRetainedModule(activity, applicationModule)

        // then
        assertThatThrownBy { activityModule.provideInstance(FakeViewModel::class.java) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("실행할 함수를 선택할 수 없습니다.")
    }

    @Test
    fun `두 가지 이상 모듈 내 함수와 매칭되더라도,퀄리파이어 어노테이션이 붙어있다면 주입받을 수 있다`() {
        // given
        class FakeViewModel(
            @FakeInMemoryCartRepositoryQualiefier val cartRepository: FakeCartRepository,
        ) : ViewModel()

        class FakeActivityRetainedModule(activityContext: Context, applicationModule: ApplicationModule) :
            ActivityRetainedModule(activityContext, applicationModule)

        class FakeActivity : DiEntryPointActivity(FakeActivityRetainedModule::class.java) {
            val viewModel by viewModel<FakeViewModel>()
        }

        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()
        val activityModule = FakeActivityRetainedModule(activity, applicationModule)

        // when
        val viewModel = activityModule.provideInstance(FakeViewModel::class.java)

        // then
        assertThat(viewModel).isInstanceOf(FakeViewModel::class.java)
    }

    @Test
    fun `주입 받으려는 객체 안에 있는 객체도 재귀적으로 주입받아서 FakeCartRepository객체를 제공해줄 수 있다`() {
        // given
        class FakeViewModel(
            @FakeInMemoryCartRepositoryQualiefier val cartRepository: FakeCartRepository,
        ) : ViewModel()

        class FakeActivityRetainedModule(activityContext: Context, applicationModule: ApplicationModule) :
            ActivityRetainedModule(activityContext, applicationModule)

        class FakeActivity : DiEntryPointActivity(FakeActivityRetainedModule::class.java) {
            val viewModel by viewModel<FakeViewModel>()
        }

        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()
        val activityModule = FakeActivityRetainedModule(activity, applicationModule)

        // then
        val viewModel = activityModule.provideInstance(FakeViewModel::class.java)
        val cartRepository =
            assertDoesNotThrow { viewModel.cartRepository as? FakeImMemoryCartRepository }
        cartRepository?.let { cartRepository ->
            assertDoesNotThrow { cartRepository.localDataSource }
            assertThat(cartRepository.localDataSource).isInstanceOf(FakeInMemoryLocalDataSource::class.java)
        }
    }
}

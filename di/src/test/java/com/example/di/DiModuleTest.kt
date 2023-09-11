package com.example.di

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import androidx.test.core.app.ApplicationProvider
import com.example.di.activity.DiEntryPointActivity
import com.example.di.annotation.FieldInject
import com.example.di.application.DiApplication
import com.example.di.module.ActivityModule
import com.example.di.module.ApplicationModule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.reflect.full.isSubclassOf

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
        class FakeViewModel(
            @FakeRoomDbCartRepository private val cartRepository: FakeCartRepository,
        ) : ViewModel() {
            @FieldInject
            lateinit var productRepository: FakeProductRepository
        }

        class FakeActivityModule(activityContext: Context, applicationModule: ApplicationModule) :
            ActivityModule(activityContext, applicationModule) {
            fun getFakeDefaultProductRepository(): FakeProductRepository {
                return getOrCreateInstance { FakeDefaultProductRepository() }
            }
        }

        class FakeActivity : DiEntryPointActivity(FakeActivityModule::class.java) {
            val viewModel by viewModel<FakeViewModel>()
        }

        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()
        val activityModule = FakeActivityModule(activity, applicationModule)

        // when
        val instance = activityModule.provideInstance(FakeViewModel::class.java)

        // then
        assertEquals(true, instance::class.isSubclassOf(FakeViewModel::class))
        assertNotNull(instance.productRepository)
    }

    @Test
    fun `가시성이 공개되지 않은 필드는 주입받을 수 없다`() {
        // given
        class FakeViewModel(
            @FakeRoomDbCartRepository private val cartRepository: FakeCartRepository,
        ) : ViewModel() {
            @FieldInject
            private lateinit var productRepository: FakeProductRepository
        }

        class FakeActivityModule(activityContext: Context, applicationModule: ApplicationModule) :
            ActivityModule(activityContext, applicationModule) {
            fun getFakeDefaultProductRepository(): FakeProductRepository {
                return getOrCreateInstance { FakeDefaultProductRepository() }
            }
        }

        class FakeActivity : DiEntryPointActivity(FakeActivityModule::class.java) {
            val viewModel by viewModel<FakeViewModel>()
        }

        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()
        val activityModule = FakeActivityModule(activity, applicationModule)

        // then
        assertThrows(IllegalStateException::class.java) {
            activityModule.provideInstance(FakeViewModel::class.java)
        }
    }

    @Test
    fun `두 가지 이상 모듈 내 함수와 매칭되면, 주입받을 수 없다`() {
        // given
        class FakeViewModel(
            private val cartRepository: FakeCartRepository, // 이거 때문에 에러 발생.
        ) : ViewModel() {
            @FieldInject
            lateinit var productRepository: FakeProductRepository
        }

        class FakeActivityModule(activityContext: Context, applicationModule: ApplicationModule) :
            ActivityModule(activityContext, applicationModule) {
            fun getFakeDefaultProductRepository(): FakeProductRepository {
                return getOrCreateInstance { FakeDefaultProductRepository() }
            }
        }

        class FakeActivity : DiEntryPointActivity(FakeActivityModule::class.java) {
            val viewModel by viewModel<FakeViewModel>()
        }

        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()
        val activityModule = FakeActivityModule(activity, applicationModule)

        // then
        assertThrows(IllegalStateException::class.java) {
            activityModule.provideInstance(FakeViewModel::class.java)
        }
    }
}

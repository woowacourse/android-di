package com.re4rk.arkdiAndroid

import com.re4rk.arkdi.ArkModule
import com.re4rk.arkdiAndroid.arkModules.ArkModules
import com.re4rk.arkdiAndroid.arkModules.arkModules
import com.re4rk.arkdiAndroid.fakeClasses.FakeApplication
import com.re4rk.arkdiAndroid.fakeClasses.fakeModules.FakeActivityModule
import com.re4rk.arkdiAndroid.fakeClasses.fakeModules.FakeApplicationModule
import com.re4rk.arkdiAndroid.fakeClasses.fakeModules.FakeRetainedActivityModule
import com.re4rk.arkdiAndroid.fakeClasses.fakeModules.FakeServiceModule
import com.re4rk.arkdiAndroid.fakeClasses.fakeModules.FakeViewModelModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE, application = FakeApplication::class)
@RunWith(RobolectricTestRunner::class)
class ArkModuleFactoryTest {

    private lateinit var arkModules: ArkModules

    private lateinit var arkModuleFactory: ArkModuleFactory

    @Before
    fun setUp() {
        // given
        arkModules = arkModules {
            applicationModule = ::FakeApplicationModule
            retainedActivityModule = ::FakeRetainedActivityModule
            activityModule = ::FakeActivityModule
            viewModelModule = ::FakeViewModelModule
            serviceModule = ::FakeServiceModule
        }

        arkModuleFactory = ArkModuleFactory(arkModules)
    }

    @Test
    fun `모듈 팩토리가 어플리케이션 모듈을 만든다`() {
        // when
        val module = arkModuleFactory.createApplicationModule(FakeApplication())

        // then
        assertThat(module).isInstanceOf(FakeApplicationModule::class.java)
    }

    @Test
    fun `모듈 팩토리가 액티비티 모듈을 만든다`() {
        // given
        val fakeActivity =
            Robolectric.buildActivity(ArkAppCompatActivity::class.java).create().get()

        // when
        val viewModel = arkModuleFactory.createActivityModule(ArkModule(), fakeActivity)

        // then
        assertThat(viewModel).isInstanceOf(ArkViewModel::class.java)

        // and
        viewModel.apply {
            assertThat(ownerRetainedModule).isInstanceOf(FakeRetainedActivityModule::class.java)
            assertThat(ownerModule).isInstanceOf(FakeActivityModule::class.java)
            assertThat(viewModelModule).isInstanceOf(FakeViewModelModule::class.java)
        }
    }

    @Test
    fun `액티비티의 구성변경으로 생명주기는 ownerModule만 변한다`() {
        // given
        val fakeActivity =
            Robolectric.buildActivity(ArkAppCompatActivity::class.java).create().get()

        val expected = arkModuleFactory.createActivityModule(ArkModule(), fakeActivity).let {
            ArkViewModel().apply {
                ownerRetainedModule = it.ownerRetainedModule
                ownerModule = it.ownerModule
                viewModelModule = it.viewModelModule
            }
        }

        // when
        val actual = arkModuleFactory.createActivityModule(ArkModule(), fakeActivity)

        // then
        assertThat(actual.ownerRetainedModule).isEqualTo(expected.ownerRetainedModule)
        assertThat(actual.ownerModule).isNotEqualTo(expected.ownerModule)
        assertThat(actual.viewModelModule).isEqualTo(expected.viewModelModule)
    }

    @Test
    fun `모듈 팩토리가 서비스 모듈을 만든다`() {
        // given & when
        val module = arkModuleFactory.createServiceModule(
            arkModuleFactory.createApplicationModule(FakeApplication()),
        )

        // then
        assertThat(module).isInstanceOf(FakeServiceModule::class.java)
    }
}

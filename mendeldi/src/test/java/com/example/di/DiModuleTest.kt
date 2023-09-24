package com.example.di

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.di.activity.DiEntryPointActivity
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
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

    @Test
    fun `주입 받으려는 객체 안에 있는 객체도 재귀적으로 주입받아서 FakeCartRepository객체를 제공해줄 수 있다`() {
        // given
        class FakeActivity : DiEntryPointActivity() {
            val viewModel: FakeViewModel by viewModel()
        }

        // when
        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        // then
        val viewModel = activity.viewModel
        val cartRepository =
            assertDoesNotThrow { viewModel.cartRepository as? FakeImMemoryCartRepository }
        cartRepository?.let { cartRepository ->
            assertDoesNotThrow { cartRepository.localDataSource }
            assertThat(cartRepository.localDataSource).isInstanceOf(FakeInMemoryLocalDataSource::class.java)
        }
    }

    @Test
    fun `가시성이 공개된 필드인 productRepository를 필드 주입 받을 수 있다`() {
        // given
        class FakeActivity : DiEntryPointActivity() {
            val viewModel: FakeFieldInjectSuccessViewModel by viewModel()
        }

        // when
        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        // then
        val viewModel = activity.viewModel
        assertThat(viewModel).isInstanceOf(FakeFieldInjectSuccessViewModel::class.java)
        assertDoesNotThrow { viewModel.productRepository }
    }

    @Test
    fun `가시성이 공개되지 않은 필드인 productRepository는 필드주입 받을 수 없다`() {
        // given
        class FakeActivity : DiEntryPointActivity() {
            val viewModel: FakeFieldInjectFailedViewModel by viewModel()
        }

        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        // when - then
        assertThatThrownBy { activity.viewModel }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("필드 주입을 받으려는 productRepository의 가시성이 공개되어 있지 않습니다.")
    }

    @Test
    fun `두 가지 이상 모듈 내 함수와 매칭되는데, 퀄리파이어가 지정되어 있지 않다면, 주입받을 수 없다`() {
        // given
        class FakeActivity : DiEntryPointActivity() {
            val viewModel: FakeQualifierFailedViewModel by viewModel()
        }

        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        // when - then
        assertThatThrownBy { activity.viewModel }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("실행할 함수를 선택할 수 없습니다.")
    }

    @Test
    fun `두 가지 이상 모듈 내 함수와 매칭되더라도,퀄리파이어 어노테이션이 붙어있다면 주입받을 수 있다`() {
        // given
        class FakeActivity : DiEntryPointActivity() {
            val viewModel: FakeQualifierSuccessViewModel by viewModel()
        }

        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        // when
        val viewModel = activity.viewModel

        // then
        assertThat(viewModel).isInstanceOf(FakeQualifierSuccessViewModel::class.java)
    }
}

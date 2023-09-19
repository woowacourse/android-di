package com.example.di

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.di.activity.DiEntryPointActivity
import com.example.di.annotation.FieldInject
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeApplication::class)
internal class ActivityModuleTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `액티비티 모듈에 정의된 메소드로 만들어진 객체는 액티비티 별로 다른 객체를 받는다`() {
        // given
        class FakeFirstActivity : DiEntryPointActivity() {
            @FieldInject
            lateinit var counter: NumberCounter
        }

        class FakeSecondActivity : DiEntryPointActivity() {
            @FieldInject
            lateinit var counter: NumberCounter
        }

        // when
        val firstActivity = Robolectric
            .buildActivity(FakeFirstActivity::class.java)
            .create()
            .get()

        val secondActivity = Robolectric
            .buildActivity(FakeSecondActivity::class.java)
            .create()
            .get()

        // then
        assertThat(firstActivity.counter === secondActivity.counter).isFalse
    }
}

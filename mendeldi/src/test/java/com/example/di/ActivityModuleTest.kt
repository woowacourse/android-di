package com.example.di

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.di.activity.DiEntryPointActivity
import com.example.di.annotation.FieldInject
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeApplication::class)
internal class ActivityModuleTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `액티비티 모듈에 정의된 메소드로 만들어진 객체는 구성변경시 유지되지 않는다`() {
        // given
        class FakeFirstActivity : DiEntryPointActivity() {
            @FieldInject
            lateinit var counter: NumberCounter

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setTheme(androidx.appcompat.R.style.Theme_AppCompat_DayNight_NoActionBar)
            }
        }

        val controller = buildActivity(FakeFirstActivity::class.java).setup() // resume까지
        val firstCounter = controller.get().counter
        controller.recreate() // 구성 변경 후 재시작
        val secondCounter = controller.get().counter
        assertThat(firstCounter === secondCounter).isFalse
    }
}

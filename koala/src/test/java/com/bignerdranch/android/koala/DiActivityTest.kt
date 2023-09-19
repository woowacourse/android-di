package com.bignerdranch.android.koala

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeDiApplication::class)
class DiActivityTest {

    @Test
    fun `viewmodel() 을 통해 viewmodel을 주입한다`() {
        // given
        class FakeDiActivity : DiActivity(FakeActivityModule()) {
            val viewModel: FakeViewModel by viewmodel()
        }

        // when
        val activity = Robolectric
            .buildActivity(FakeDiActivity::class.java)
            .create()
            .get()
        val viewModel = activity?.viewModel

        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `필드 주입이 필요한 경우 인스턴스를 생성하여 주입한다`() {
        // given
        class FakeDiActivity : DiActivity(FakeActivityModule()) {
            @KoalaFieldInject
            lateinit var fieldObject: FakeFieldObject
        }

        // when
        val activity = Robolectric
            .buildActivity(FakeDiActivity::class.java)
            .create()
            .get()

        // then
        assertThat(activity.fieldObject).isNotNull()
        assertThat(activity.fieldObject::class).isEqualTo(FakeFieldObject::class)
    }

    @Test
    fun `필드 주입이 필요하지 않은 경우 인스턴스를 주입하지 않는다`() {
        // given
        class FakeDiActivity : DiActivity(FakeActivityModule()) {
            @KoalaFieldInject
            lateinit var fieldObject: FakeFieldObject

            lateinit var fieldObject2: FakeFieldObject
        }

        // when
        val activity = Robolectric
            .buildActivity(FakeDiActivity::class.java)
            .create()
            .get()

        // then
        assertThat(activity.fieldObject).isNotNull()
        assertThrows(UninitializedPropertyAccessException::class.java) {
            activity.fieldObject2
        }
    }
}

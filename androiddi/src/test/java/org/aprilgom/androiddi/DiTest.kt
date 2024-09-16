package org.aprilgom.androiddi

import org.aprilgom.androiddi.fake.DefaultFakeRepository
import org.aprilgom.androiddi.fake.FakeActivity
import org.aprilgom.androiddi.fake.FakeRecursive1
import org.aprilgom.androiddi.fake.FakeRecursive2
import org.aprilgom.androiddi.fake.FakeRecursive3
import org.aprilgom.androiddi.fake.FakeRecursive4
import org.aprilgom.androiddi.fake.FakeViewModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DiTest {
    @After
    fun clear() {
        GlobalContext.clear()
    }

    @Test
    fun `적절한 객체 인스턴스를 찾아 ViewModel 의존성을 주입한다`() {
        // given
        diContainer {
            modules(
                module {
                    factory(named = "QDefaultFakeRepository") { DefaultFakeRepository() }
                    viewModel { FakeViewModel(get(named = "QDefaultFakeRepository")) }
                },
            )
        }

        val activity =
            Robolectric
                .buildActivity(FakeActivity::class.java)
                .create()
                .get()

        // then
        val viewModel = activity.viewModel
        assertNotNull(viewModel)
        assertEquals(viewModel.fakeRepository.javaClass.kotlin, DefaultFakeRepository::class)
    }

    @Test
    fun `재귀 주입 테스트`() {
        // given & when
        val expected = 1
        diContainer {
            modules(
                module {
                    factory { FakeRecursive1(expected) }
                    factory { FakeRecursive2(get()) }
                    factory { FakeRecursive3(get()) }
                    factory { FakeRecursive4(get()) }
                },
            )
        }

        // then
        val recursive4 = GlobalContext.provide(FakeRecursive4::class) as FakeRecursive4
        val actual = recursive4.recursive.recursive.recursive.intValue
        assertEquals(actual, expected)
    }
}

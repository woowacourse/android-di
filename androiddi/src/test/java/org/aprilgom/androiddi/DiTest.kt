package org.aprilgom.androiddi

import org.aprilgom.androiddi.fake.DefaultFakeRepository
import org.aprilgom.androiddi.fake.FakeActivity
import org.aprilgom.androiddi.fake.FakeConstructorFieldClass
import org.aprilgom.androiddi.fake.FakeField
import org.aprilgom.androiddi.fake.FakeFieldInjectClass
import org.aprilgom.androiddi.fake.FakeMixedInjectClass
import org.aprilgom.androiddi.fake.FakeQualifierClass1
import org.aprilgom.androiddi.fake.FakeQualifierClass2
import org.aprilgom.androiddi.fake.FakeQualifierInjectClass
import org.aprilgom.androiddi.fake.FakeRecursive1
import org.aprilgom.androiddi.fake.FakeRecursive2
import org.aprilgom.androiddi.fake.FakeRecursive3
import org.aprilgom.androiddi.fake.FakeRecursive4
import org.aprilgom.androiddi.fake.FakeScopeActivity
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
        val viewModel = activity.fakeViewModel
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

    @Test
    fun `필드 주입 테스트`() {
        // given & when
        val expected = 1
        diContainer {
            modules(
                module {
                    factory { FakeFieldInjectClass() }
                    factory { FakeField(expected) }
                },
            )
        }

        // then
        val fakeFieldInjectClass = GlobalContext.provide(FakeFieldInjectClass::class) as FakeFieldInjectClass
        assertNotNull(fakeFieldInjectClass.fakeField)
        assertEquals(fakeFieldInjectClass.fakeField.intValue, expected)
    }

    @Test
    fun `생성자와 필드 혼합 주입 테스트`() {
        val constructorExpected = 1
        val fieldExpected = 2
        diContainer {
            modules(
                module {
                    factory { FakeConstructorFieldClass(constructorExpected) }
                    factory { FakeField(fieldExpected) }
                    factory { FakeMixedInjectClass(get()) }
                },
            )
        }

        // then
        val fakeMixedInjectClass = GlobalContext.provide(FakeMixedInjectClass::class) as FakeMixedInjectClass
        assertNotNull(fakeMixedInjectClass.fakeField)
        assertNotNull(fakeMixedInjectClass.fakeConstructorFieldClass)

        assertEquals(fakeMixedInjectClass.fakeField.intValue, fieldExpected)
        assertEquals(fakeMixedInjectClass.fakeConstructorFieldClass.intValue, constructorExpected)
    }

    @Test
    fun `Qualifier 구분 테스트`() {
        val expected1 = 1
        val expected2 = 2
        diContainer {
            modules(
                module {
                    factory(named = "FakeQualifier1") { FakeQualifierClass1(1) }
                    factory(named = "FakeQualifier2") { FakeQualifierClass2(2) }
                    factory { FakeQualifierInjectClass() }
                },
            )
        }

        val fakeQualifierInjectClass = GlobalContext.provide(FakeQualifierInjectClass::class) as FakeQualifierInjectClass

        val fakeQualifier1 = fakeQualifierInjectClass.fakeQualifier1
        val fakeQualifier2 = fakeQualifierInjectClass.fakeQualifier2

        assertEquals(fakeQualifier1.getValue(), expected1)
        assertEquals(fakeQualifier2.getValue(), expected2)
    }

    @Test
    fun `Activity가 stop되었을 때에도 Repository가 유지된다`() {
        diContainer {
            modules(
                module {
                    factory(named = "QDefaultFakeRepository") { DefaultFakeRepository() }
                    viewModel { FakeViewModel(get(named = "QDefaultFakeRepository")) }
                },
            )
        }
        val controller = Robolectric.buildActivity(FakeActivity::class.java).setup()
        val activity = controller.get()
        controller.stop()

        val repository = activity.fakeViewModel.fakeRepository
        assertNotNull(repository)
    }

    @Test
    fun `Activity가 살아있을 경우 activity scope의 필드에 접근할 수 있다`() {
        diContainer {
            modules(
                module {
                    scope(named = "FakeScope") {
                        scoped(named = "FakeField") { FakeField(1) }
                    }
                },
            )
        }
        val controller =
            Robolectric.buildActivity(FakeScopeActivity::class.java)
                .setup()

        val activity = controller.get()
        val expected = activity.scope.get<FakeField>("FakeField")
        assertEquals(FakeField(1), expected)
    }

    @Test(expected = NullPointerException::class)
    fun `Activity가 destroy되었을 경우 activity scope의 필드에 접근하지 못한다`() {
        diContainer {
            modules(
                module {
                    scope(named = "FakeScope") {
                        scoped(named = "FakeField") { FakeField(1) }
                    }
                },
            )
        }
        val controller =
            Robolectric.buildActivity(FakeScopeActivity::class.java)
                .setup()
                .destroy()

        val activity = controller.get()
        activity.scope.get<FakeField>("FakeField")
    }
}

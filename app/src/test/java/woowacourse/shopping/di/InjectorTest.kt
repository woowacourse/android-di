package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import org.junit.Assert.assertEquals
import org.junit.Test
import woowacourse.shopping.Fake

class InjectorTest {

    interface FakeViewModel2
    interface FakeRepository2
    class DefaultFakeRepository2 : FakeRepository2
    class FakeViewModel3(val repository: FakeRepository2) : ViewModel()

    @Test
    fun `주어진 클래스 타입에 맞게 인스턴스를 생성해서 반환한다`() {
        // when
        Container.addInstance(Fake.DefaultFakeRepository::class, null)
        val injector = Injector(Container)
        val actual = injector.createInstance(Fake.FakeViewModel::class)

        // then
        assertEquals(Fake.FakeViewModel::class, actual::class)
    }

//    @Test(expected = IllegalStateException::class)
//    fun `주어진 클래스 타입의 주생성자를 가져올 수 없다면 오류가 발생한다`() {
//        val injector = Injector(Container)
//        injector.createInstance(FakeViewModel3::class)
//    }
//
//    @Test(expected = IllegalArgumentException::class)
//    fun `container에 없는 타입을 요구할 경우 오류가 발생한다`() {
//        // given
//        Container.addInstance(Fake.DefaultFakeRepository::class, null)
//        val injector = Injector(Container)
//
//        // when
//        injector.createInstance(FakeViewModel3::class)
//    }
}

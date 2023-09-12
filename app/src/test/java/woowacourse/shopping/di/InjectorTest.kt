package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

interface FakeInterface

class FakeFirst

class FakeImplOfInterface : FakeInterface

class FakeOnceConstructor(fakeInterface: FakeInterface)

class FakeHaveTwiceConstructor(
    fakeFirst: FakeFirst,
    fakeOnceConstructor: FakeOnceConstructor,
)

class FakeHaveFieldAndConstructor(
    fakeOnceConstructor: FakeOnceConstructor,
) {
    @Inject
    lateinit var fakeFirst: FakeFirst
}

class FakeModule : Module {
    fun provideFakeFirst(): FakeFirst = FakeFirst()

    fun provideFakeImplOfInterface(): FakeInterface = FakeImplOfInterface()

    fun provideFakeOnceConstructor(
        fakeInterface: FakeInterface,
    ): FakeOnceConstructor = FakeOnceConstructor(fakeInterface)

    fun provideFakeHaveTwiceConstructor(
        fakeFirst: FakeFirst,
        fakeOnceConstructor: FakeOnceConstructor,
    ): FakeHaveTwiceConstructor = FakeHaveTwiceConstructor(fakeFirst, fakeOnceConstructor)

    fun provideFakeHaveFieldAndConstructor(
        fakeOnceConstructor: FakeOnceConstructor,
    ): FakeHaveFieldAndConstructor = FakeHaveFieldAndConstructor(fakeOnceConstructor)
}

class InjectorTest {
    private lateinit var injector: Injector

    @Before
    fun setup() {
        injector = Injector(FakeModule())
    }

    @Test
    fun `주 생성자가 없으면 NullPointException이 발생한다`() {
        assertThrows(NullPointerException::class.java) {
            injector.inject(FakeInterface::class.java)
        }.also {
            assertEquals("[ERROR] 주 생성자가 없습니다.", it.message)
        }
    }

    @Test
    fun `추상화된 주 생성자 하나가 있는 클래스의 의존성을 주입한다`() {
        val instance = injector.inject(FakeOnceConstructor::class.java)
        assertThat(instance).isInstanceOf(FakeOnceConstructor::class.java)
    }

    @Test
    fun `생성자가 있는 클래스와 디폴트 생성자만 존재하는 클래스를 주 생성자로 갖는 클래스의 의존성을 주입한다`() {
        val instance = injector.inject(FakeHaveTwiceConstructor::class.java)
        assertThat(instance).isInstanceOf(FakeHaveTwiceConstructor::class.java)
    }

    @Test
    fun `생성자가 있는 클래스와 @Inject 어노테이션이 붙은 필드를 갖는 클래스의 의존성을 주입한다`() {
        val instance = injector.inject(FakeHaveFieldAndConstructor::class.java)
        assertThat(instance).isInstanceOf(FakeHaveFieldAndConstructor::class.java)
    }
}

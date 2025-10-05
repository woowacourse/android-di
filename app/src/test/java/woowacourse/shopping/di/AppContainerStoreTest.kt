package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import woowacourse.fixture.Child1
import woowacourse.fixture.Child2
import woowacourse.fixture.CircularDependency1
import woowacourse.fixture.NestedDependency
import woowacourse.fixture.NoConstructorObject
import woowacourse.fixture.Parent
import woowacourse.fixture.UnableReflectObject

class AppContainerStoreTest {
    @Test
    fun `instantiate는 등록된 팩토리를 통해 객체를 생성해야 한다`() {
        // given
        val appContainerStore =
            AppContainerStore(
                listOf(
                    DependencyFactory(Child1::class) {
                        Child1()
                    },
                    DependencyFactory(Child2::class) {
                        Child2()
                    },
                ),
            )
        // when
        val actual = appContainerStore.instantiate(Parent::class)

        assertThat(actual).isInstanceOf(Parent::class.java)
    }

    @Test
    fun `팩토리가 없는 클래스는 리플렉션을 통해 자동 주입되어야 한다`() {
        // given
        val appContainerStore =
            AppContainerStore(
                listOf(),
            )

        // when
        val actual = appContainerStore.instantiate(Parent::class)

        // then
        assertThat(actual).isInstanceOf(Parent::class.java)
    }

    @Test
    fun `중첩 의존성 체인을 성공적으로 해결하고 주입해야 한다`() {
        // given
        val appContainerStore = AppContainerStore(listOf())

        // when
        val actual = appContainerStore.instantiate(NestedDependency::class)

        // when
        assertThat(actual).isInstanceOf(NestedDependency::class.java)
    }

    @Test
    fun `saveToCache=true일 때 동일 인스턴스를 반환한다`() {
        // given
        val appContainerStore = AppContainerStore(listOf())

        // when
        val actual = appContainerStore.instantiate(Parent::class, true)
        val expected = appContainerStore[Parent::class]

        // then
        assertThat(actual).isSameAs(expected)
    }

    @Test
    fun `saveToCache=false일 때 매번 새로운 인스턴스를 반환한다`() {
        // given
        val appContainerStore = AppContainerStore(listOf())

        // when
        val actual = appContainerStore.instantiate(Parent::class, false)
        val expected = appContainerStore[Parent::class]

        // then
        assertThat(actual).isNotSameAs(expected)
    }

    @Test
    fun `순환 참조가 발생하면 예외를 던진다`() {
        // given
        val appContainerStore = AppContainerStore(listOf())

        // when - then
        assertThatThrownBy {
            appContainerStore.instantiate(CircularDependency1::class)
        }.message().contains("순환 참조가 발견되었습니다")
    }

    @Test
    fun `주 생성자가 없거나 등록되지 않은 인터페이스 요청 시 예외를 던진다`() {
        // given
        val appContainerStore = AppContainerStore(listOf())

        // when - then
        assertThatThrownBy {
            appContainerStore.instantiate(NoConstructorObject::class)
        }.message().contains("주 생성자를 찾을 수 없습니다")
    }

    @Test
    fun `필수 의존성을 해결할 수 없으면 예외를 던진다`() {
        // given
        val appContainerStore = AppContainerStore(listOf())

        // when = then
        assertThatThrownBy {
            appContainerStore.instantiate(UnableReflectObject::class)
        }.message().contains("주 생성자를 찾을 수 없습니다")
    }
}

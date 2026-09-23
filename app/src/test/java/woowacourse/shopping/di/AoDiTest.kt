package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AoDiTest {
    @Test
    fun `생성자 의존성을 재귀적으로 생성한다`() {
        val root = AoDi.instantiate(Root::class)

        assertThat(root.branch.leaf).isInstanceOf(Leaf::class.java)
    }

    @Test
    fun `같은 타입의 의존성을 재사용한다`() {
        val first = AoDi.instantiate(FirstConsumer::class)
        val second = AoDi.instantiate(SecondConsumer::class)

        assertThat(first.dependency).isSameAs(second.dependency)
    }

    @Test
    fun `의존성 타입을 정의하고 그 타입에 맞는 객체를 DI에 입력한다`() {
        val depen = SharedDependency()

        AoDi.register(SharedDependency::class, depen)
        val first = AoDi.instantiate(FirstConsumer::class)

        assertThat(first.dependency).isSameAs(depen)
    }

    class Root(
        val branch: Branch,
    )

    class Branch(
        val leaf: Leaf,
    )

    class Leaf

    class FirstConsumer(
        val dependency: SharedDependency,
    )

    class SecondConsumer(
        val dependency: SharedDependency,
    )

    class SharedDependency
}

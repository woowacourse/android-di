package com.example.sh1mj1

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

class DefaultInjectedSingletonContainerTest {
    interface StubRepo

    class Default1StubRepo : StubRepo

    class Default2StubRepo : StubRepo

    @Test
    fun `같은 타입이며 같은 Qualfier 인 컴포넌트를 컨테이너에 넣을 때 예외를 던진다`() {
        // given
        val component1 =
            InjectedComponent.InjectedSingletonComponent(
                StubRepo::class,
                Default1StubRepo(),
                Qualifier("1"),
            )

        val component2 =
            InjectedComponent.InjectedSingletonComponent(
                StubRepo::class,
                Default2StubRepo(),
                Qualifier("1"),
            )

        // when
        DefaultInjectedSingletonContainer.add(component1)

        // then
        shouldThrow<IllegalStateException> {
            DefaultInjectedSingletonContainer.add(component2)
        }
    }

    @Test
    fun `하나의 타입 컴포넌트를 컨테이너에 넣고 찾는다`() {
        // given
        val component =
            InjectedComponent.InjectedSingletonComponent(
                StubRepo::class,
                Default1StubRepo(),
            )

        // when
        DefaultInjectedSingletonContainer.add(component)

        // then
        val stubRepo =
            DefaultInjectedSingletonContainer.findWithKey(
                ComponentKey(
                    StubRepo::class,
                    null,
                ),
            )
        stubRepo.shouldBeInstanceOf<Default1StubRepo>()
    }

    @Test
    fun `같은 타입이고 하나에만 Qualifier 가 붙어있는 컴포넌트를 컨테이너에 넣고 클래스로만 찾을 때 예외를 던진다`() {
        // given
        val component1 =
            InjectedComponent.InjectedSingletonComponent(
                StubRepo::class,
                Default1StubRepo(),
            )

        val component2 =
            InjectedComponent.InjectedSingletonComponent(
                StubRepo::class,
                Default2StubRepo(),
                Qualifier("1"),
            )

        // when
        DefaultInjectedSingletonContainer.add(component1)
        DefaultInjectedSingletonContainer.add(component2)

        // then
        val foundComponent1 =
            DefaultInjectedSingletonContainer.findWithKey(
                ComponentKey(
                    StubRepo::class,
                    null,
                ),
            )
        foundComponent1.shouldBeInstanceOf<Default1StubRepo>()

        val foundComponent2 =
            DefaultInjectedSingletonContainer.findWithKey(
                ComponentKey(
                    StubRepo::class,
                    Qualifier("1"),
                ),
            )
        foundComponent2.shouldBeInstanceOf<Default2StubRepo>()
    }
}

package com.example.sh1mj1

import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.component.singleton.ComponentKey
import com.example.sh1mj1.component.singleton.InjectedSingletonComponent
import com.example.sh1mj1.container.singleton.InjectedSingletonContainer
import com.example.sh1mj1.container.singleton.DefaultInjectedSingletonContainer
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultInjectedSingletonContainerTest {
    private lateinit var container: InjectedSingletonContainer

    @BeforeEach
    fun setUp() {
        container = DefaultInjectedSingletonContainer.instance
    }

    @AfterEach
    fun tearDown() {
        container.clear()
    }

    interface StubRepo

    class Default1StubRepo : StubRepo

    class Default2StubRepo : StubRepo

    @Test
    fun `하나의 타입 컴포넌트를 컨테이너에 넣고 찾는다`() {
        // given
        val component =
            InjectedSingletonComponent(
                StubRepo::class,
                Default1StubRepo(),
            )

        // when
        container.add(component)

        // then
        val stubRepo =
            container.find(
                ComponentKey.of(
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
            InjectedSingletonComponent(
                StubRepo::class,
                Default1StubRepo(),
            )

        val component2 =
            InjectedSingletonComponent(
                StubRepo::class,
                Default2StubRepo(),
                Qualifier("1"),
            )

        // when
        container.add(component1)
        container.add(component2)

        // then
        val foundComponent1 =
            container.find(
                ComponentKey.of(
                    StubRepo::class,
                    null,
                ),
            )
        foundComponent1.shouldBeInstanceOf<Default1StubRepo>()

        val foundComponent2 =
            container.find(
                ComponentKey.of(
                    StubRepo::class,
                    Qualifier("1"),
                ),
            )
        foundComponent2.shouldBeInstanceOf<Default2StubRepo>()
    }
}

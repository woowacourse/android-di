package com.bandal.fullmoon

import junit.framework.TestCase.assertNotSame
import junit.framework.TestCase.assertSame
import org.junit.Test

class SingletonTest {
    @Test
    fun `모듈의 함수가 싱글톤으로 지정되어있을 경우 새로운 인스턴스를 생성하지 않는다`() {
        // given
        val testModule = object : Module {
            @Singleton
            fun createInMemoryInstance(): FakeRepository {
                return FakeInMemoryRepository()
            }
        }

        class TestClass(
            @FullMoonInject
            val fakeRepository1: FakeRepository,
        ) {
            @FullMoonInject
            lateinit var fakeRepository2: FakeRepository
        }

        val injector = FullMoonInjector(AppContainer(testModule))

        // when
        val injectedClass = injector.inject(TestClass::class)

        // then
        assertSame(injectedClass.fakeRepository1, injectedClass.fakeRepository2)
        println("fakeRepository1은 싱글톤 인스턴스 " + injectedClass.fakeRepository1)
        println("fakeRepository2은 싱글톤 인스턴스 " + injectedClass.fakeRepository2)
    }

    @Test
    fun `모듈의 함수가 싱글톤으로 지정되지 않았다면 새로운 인스턴스를 생성한다`() {
        // given
        val testModule = object : Module {
            fun createInMemoryInstance(): FakeRepository {
                return FakeInMemoryRepository()
            }
        }

        class TestClass(
            @FullMoonInject
            val fakeRepository1: FakeRepository,
        ) {
            @FullMoonInject
            lateinit var fakeRepository2: FakeRepository
        }

        val injector = FullMoonInjector(AppContainer(testModule))

        // when
        val injectedClass = injector.inject(TestClass::class)

        // then
        assertNotSame(injectedClass.fakeRepository1, injectedClass.fakeRepository2)
        println("fakeRepository1 싱글톤이 아닌 인스턴스 " + injectedClass.fakeRepository1)
        println("fakeRepository2 싱글톤이 아닌 인스턴스 " + injectedClass.fakeRepository2)
    }

    @Test
    fun `모듈의 함수가 싱글톤과 Qualifier로 지정되어있다면 두번째 주입부터 새로운 인스턴스를 생성하지 않는다`() {
        // given
        val testModule = object : Module {
            @Singleton
            @Qualifier("InMemory")
            fun createInMemoryInstance(): FakeRepository {
                return FakeInMemoryRepository()
            }
        }

        class TestClass(
            @Qualifier("InMemory")
            val fakeRepository1: FakeRepository,
        ) {
            @Qualifier("InMemory")
            lateinit var fakeRepository2: FakeRepository
        }

        val injector = FullMoonInjector(AppContainer(testModule))

        // when
        val injectedClass = injector.inject(TestClass::class)

        // then
        assertSame(injectedClass.fakeRepository1, injectedClass.fakeRepository2)
        println("fakeRepository1은 싱글톤 인스턴스 " + injectedClass.fakeRepository1)
        println("fakeRepository2은 싱글톤 인스턴스 " + injectedClass.fakeRepository2)
    }

    @Test
    fun `모듈의 함수가 Qualifier로 지정되어있으나 Singleton으로 지정되어있지 않다면 새로운 인스턴스를 생성한다`() {
        // given
        val testModule = object : Module {
            @Qualifier("InMemory")
            fun createInMemoryInstance(): FakeRepository {
                return FakeInMemoryRepository()
            }
        }

        class TestClass(
            @Qualifier("InMemory")
            val fakeRepository1: FakeRepository,
        ) {
            @Qualifier("InMemory")
            lateinit var fakeRepository2: FakeRepository
        }

        val injector = FullMoonInjector(AppContainer(testModule))

        // when
        val injectedClass = injector.inject(TestClass::class)

        // then
        assertNotSame(injectedClass.fakeRepository1, injectedClass.fakeRepository2)
        println("fakeRepository1은 싱글톤이 아닌 인스턴스 " + injectedClass.fakeRepository1)
        println("fakeRepository2은 싱글톤이 아닌 인스턴스 " + injectedClass.fakeRepository2)
    }
}

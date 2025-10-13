package com.example.di

import com.example.di.annotation.Inject
import com.example.di.annotation.Provides
import com.example.di.annotation.Singleton
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class AppContainerTest {
    private lateinit var container: AppContainer

    @Before
    fun setUp() {
        container = AppContainer()
    }

    @Test
    fun `provide로 바인딩한 인스턴스를 resolve가 반환한다`() {
        // given
        val testInstance = Any()

        // when
        container.provide(Any::class, testInstance)

        // then
        val resolvedInstance = container.resolve(Any::class)
        assertThat(resolvedInstance).isSameAs(testInstance)
    }

    @Test
    fun `모듈을 등록하면 @Provides 반환 타입을 resolve할 수 있다`() {
        // when
        container.provideModule(TestModule::class)

        // then
        val resolvedInstance = container.resolve(TestModule.ProvidesAnnotationTest::class)
        assertThat(resolvedInstance).isNotNull
    }

    @Test
    fun `@Singleton이 없는 @Provides는 호출마다 새 인스턴스를 제공한다`() {
        // given
        container.provideModule(TestModule::class)

        // when
        val resolvedInstance1 = container.resolve(TestModule.ProvidesAnnotationTest::class)
        val resolvedInstance2 = container.resolve(TestModule.ProvidesAnnotationTest::class)

        // then
        assertThat(resolvedInstance1).isNotEqualTo(resolvedInstance2)
    }

    @Test
    fun `@Singleton이 붙은 @Provides는 항상 동일 인스턴스를 제공한다`() {
        // given
        container.provideModule(TestModule::class)

        // when
        val resolvedInstance1 = container.resolve(TestModule.SingletonAnnotationTest::class)
        val resolvedInstance2 = container.resolve(TestModule.SingletonAnnotationTest::class)

        // then
        assertThat(resolvedInstance1).isSameAs(resolvedInstance2)
    }

    @Test
    fun `@Inject가 붙은 생성자 파라미터가 주입된다`() {
        // given
        val testInstance = Any()
        container.provide(Any::class, testInstance)

        // when
        val resolvedInstance = container.resolve(ConstructorInjectionTest::class)

        // then
        assertThat(resolvedInstance.testInstance).isSameAs(testInstance)
    }

    @Test
    fun `@Inject가 붙은 프로퍼티가 주입된다`() {
        // given
        val testInstance = Any()
        container.provide(Any::class, testInstance)

        // when
        val resolvedInstance = container.resolve(FieldInjectionTest::class)

        // then
        assertThat(resolvedInstance.testInstance).isSameAs(testInstance)
    }

    @Test
    fun `@Inject 외 파라미터는 기본값을 사용한다`() {
        // given
        val testInstance = Any()
        container.provide(Any::class, testInstance)

        // when
        val resolvedInstance = container.resolve(ConstructorInjectionWithDefaultValueTest::class)

        // then
        assertSoftly { softly ->
            softly.assertThat(resolvedInstance.testInstance).isSameAs(testInstance)
            softly.assertThat(resolvedInstance.initialData).isEqualTo("default")
        }
    }

    @Test
    fun `@Inject도 기본값도 없는 생성자 파라미터가 있으면 예외를 던진다`() {
        // given
        val testInstance = Any()
        container.provide(Any::class, testInstance)

        // then
        assertThrows(IllegalArgumentException::class.java) {
            container.resolve(ConstructorInjectionFailTest::class)
        }
    }

    @Test
    fun `재귀 주입이 정상 동작한다`() {
        // when
        val resolvedInstance = container.resolve(A::class)

        // then
        assertThat(resolvedInstance).isNotNull
    }
}

class ConstructorInjectionTest(
    @Inject val testInstance: Any,
)

class ConstructorInjectionWithDefaultValueTest(
    @Inject val testInstance: Any,
    val initialData: String = "default",
)

class ConstructorInjectionFailTest(
    @Inject val testInstance: Any,
    val initialData: String,
)

class FieldInjectionTest {
    @Inject
    lateinit var testInstance: Any
}

class A(
    @Inject instance: B,
) {
    class B(
        @Inject instance: C,
    ) {
        class C
    }
}

object TestModule {
    class ProvidesAnnotationTest

    class SingletonAnnotationTest

    @Provides
    fun provideTestInstance(): ProvidesAnnotationTest = ProvidesAnnotationTest()

    @Provides
    @Singleton
    fun provideTestSingletonInstance(): SingletonAnnotationTest = SingletonAnnotationTest()
}

package com.android.diandroid

import com.android.di.component.DiContainer
import junit.framework.TestCase.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class DiContainerTest {
    private lateinit var diContainer: DiContainer

    @Before
    fun setup() {
        diContainer = DiContainer(null)
    }

    @Test
    fun `bind 시 instance가 주입된다`() {
        diContainer.bind(TestService::class, TestService())
        val instance = diContainer.match(TestService::class)
        assertNotNull(instance)
    }

    @Test
    fun `bind 되지 않은 인스턴스에 접근하면 예외가 발생한다`() {
        assertThrows(IllegalArgumentException::class.java) {
            diContainer.match(OtherService::class)
        }
    }

    @Test
    fun `bind 된 인스턴스가 제거된다`() {
        // given
        diContainer.bind(TestService::class, TestService())
        assertNotNull(diContainer.match(TestService::class))

        // when
        diContainer.remove(TestService::class)

        // Then
        assertThrows(IllegalArgumentException::class.java) {
            diContainer.match(TestService::class)
        }
    }

    @Test
    fun `bind 된 인스턴스가 없다면 부모 컨테이너에서 탐색한다`() {
        // given
        val parentContainer = DiContainer()
        val testService = TestService()
        parentContainer.bind(TestService::class, testService)
        val childContainer = DiContainer(parentContainer)

        // when
        val instance = childContainer.match(TestService::class)

        // then
        assertNotNull(instance)
        assertSame(instance, testService)
    }

    @Test
    fun `Qualifier로 provide시 instance가 주입된다`() {
        // given
        val instance = TestService()
        diContainer.provide(QualifierTest::class, instance)

        // when
        val provideInstance: TestService? = diContainer.matchByQualifier(QualifierTest::class)

        // then
        assertNotNull(provideInstance)
        assertSame(instance, provideInstance)
    }

    @Test
    fun `존재하지 않는 Qualifier 요청 시 예외가 발생한다`() {
        assertThrows(IllegalArgumentException::class.java) {
            diContainer.matchByQualifier<QualifierTest>(QualifierTest::class)
        }
    }
}

package woowacourse.shopping.di

import com.example.di.AppModule
import com.example.di.DIContainer
import com.example.di.Dependency
import com.example.di.annotations.ApplicationScope
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class DIContainerTest {
    // private lateinit var dependency: Dependency

    @Before
    fun setUp() {
        // dependency = Dependency(TestModule(), TestModule::testFunction)
        // DIContainer.addDependency(dependency)
    }

    @Test
    fun `인스턴스를 등록하고 가져올 수 있다`() {
        val dependency = Dependency(TestModule(), TestModule::testFunction)
        DIContainer.addDependency(dependency)

        val retrievedDependency = DIContainer.getDependencyInstance(TestModule::class, null)
        assertEquals(dependency, retrievedDependency)
    }

    @Test
    fun `addDependency should add a dependency`() {
        assertTrue(DIContainer.getDependencyInstance(TestModule::class, null) is TestModule)
    }

    @Test
    fun `getDependencyInstance should return the correct instance`() {
        val instance = DIContainer.getDependencyInstance(TestModule::class, null)
        assertNotNull(instance)
        assertTrue(instance is TestModule)
    }

    @Test
    fun `destroyDependency should remove a dependency`() {
        // DIContainer.destroyDependency(dependency)
        try {
            DIContainer.getDependencyInstance(TestModule::class, null)
            fail("Expected an IllegalStateException to be thrown")
        } catch (e: IllegalStateException) {
            assertEquals("TestModule can not find the corresponding dependency", e.message)
        }
    }

    class TestModule : AppModule {
        @ApplicationScope
        fun testFunction() = "test"
    }

    /*@Before
    fun setUp() {
        DIContainer.depen
        DIContainer.instances.clear() // 테스트 전에 컨테이너 초기화
    }

    @Test
    fun `인스턴스를 등록하고 가져올 수 있다`() {
        val instance = TestClass()
        DIContainer.setInstance(TestClass::class, instance)
        val retrievedInstance = DIContainer.getInstance(TestClass::class)
        assertEquals(instance, retrievedInstance)
    }

    @Test
    fun `Qualifier를 사용하여 인스턴스를 등록하고 가져올 수 있다`() {
        val instance1 = TestClass("1")
        val instance2 = TestClass("2")
        DIContainer.setInstance(TestClass::class, instance1, "qualifier1")
        DIContainer.setInstance(TestClass::class, instance2, "qualifier2")
        val retrievedInstance1 = DIContainer.getInstance(TestClass::class, "qualifier1")
        val retrievedInstance2 = DIContainer.getInstance(TestClass::class, "qualifier2")
        assertEquals(instance1, retrievedInstance1)
        assertEquals(instance2, retrievedInstance2)
    }

    @Test
    fun `등록되지 않은 인스턴스를 가져오려고 하면 예외가 발생한다`() {
        assertThrows(NullPointerException::class.java) {
            DIContainer.getInstance(TestClass::class)
        }
    }

    @Test
    fun `injectFieldDependencies 함수를 사용하여 Qualifier를 지정하여 필드에 의존성을 주입할 수 있다`() {
        val instance = TestClassWithQualifiedFieldInjection()
        DIContainer.setInstance(Dependency::class, Dependency("1"), "qualifier1")
        DIContainer.injectFieldDependencies(instance)
        assertNotNull(instance.dependency)
        assertEquals("1", instance.dependency.value)
    }

    class TestClass(
        val value: String = "",
    )

    class Dependency(
        val value: String = "",
    )

    class TestClassWithQualifiedFieldInjection {
        @FieldInject
        @Qualifier("qualifier1")
        lateinit var dependency: Dependency
    }*/
}

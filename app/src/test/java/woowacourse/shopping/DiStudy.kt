package woowacourse.shopping

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaType

class DiStudy {
    @Test
    fun `DependencyProvider는 리포지터리를 가지고 있는 상태다`() {
        // given
        val dependencyProvider = DependencyProvider.getInstance()

        // when
        val repository = dependencyProvider.dependencies[TestRepository::class.java]

        // then
        assertNotNull(repository)
    }

    @Test
    fun `TestViewModel의 생성자 파라미터는 1개다`() {
        // when
        val parameters = TestViewModel::class.primaryConstructor?.parameters ?: emptyList()

        // then
        assertEquals(parameters.size, 1)
    }

    @Test
    fun `TestViewModel의 생성자 파라미터의 이름은 defaultTestRepository다`() {
        // when
        val parameters = TestViewModel::class.primaryConstructor?.parameters ?: emptyList()
        val name = parameters[0].name

        println(name)

        // then
        assertEquals(name, "defaultTestRepository")
    }

    @Test
    fun `TestViewModel의 생성자 파라미터의 자료형은 TestRepository다`() {
        // when
        val parameters = TestViewModel::class.primaryConstructor?.parameters ?: emptyList()
        val name = parameters[0].type.toString()

        // then
        assertEquals(name, TestRepository::class.java.typeName)
    }

    @Test
    fun `리플렉션을 통해 얻은 주생성자를 이용하여 ViewModel을 생성한다`() {
        // given
        val repository = DependencyProvider.getInstance().dependencies[TestRepository::class.java]

        // when
        val viewModel = TestViewModel::class.primaryConstructor?.call(repository)

        // then
        assertNotNull(viewModel)
    }

    @Test
    fun `DependencyProvider를 통해 파라미터가 하나인 TestViewModel을 생성한다`() {
        // when
        val viewModel = DependencyProvider.getInstance().createInstance(TestViewModel::class)

        // then
        assertNotNull(viewModel)
    }

    @Test
    fun `DependencyProvider를 통해 파라미터가 두개인 TestViewModel2를 생성한다`() {
        // when
        val viewModel2 = DependencyProvider.getInstance().createInstance(TestViewModel2::class)

        // then
        assertNotNull(viewModel2)
    }
}

class DependencyProvider() {
    val dependencies = mutableMapOf<Class<*>, Any>()

    init {
        dependencies[TestRepository::class.java] = DefaultTestRepository()
        dependencies[TestRepository2::class.java] = DefaultTestRepository2()
    }

    companion object {
        private val instance = DependencyProvider()

        fun getInstance(): DependencyProvider {
            return instance
        }
    }

    fun <T : Any>createInstance(clazz: KClass<T>): T {
        val insertParameters: MutableList<Any> = mutableListOf()
        val parameters = clazz.primaryConstructor?.parameters
        parameters?.forEach {
            insertParameters.add(dependencies[it.type.javaType]!!)
        }

        return clazz.primaryConstructor?.call(*insertParameters.toTypedArray())!!
    }
}

class TestViewModel constructor(
    private val defaultTestRepository: TestRepository,
) {
    fun getString(): String {
        return defaultTestRepository.get()
    }
}

class TestViewModel2 constructor(
    private val defaultTestRepository: TestRepository,
    private val defaultTestRepository2: TestRepository2,
)

interface TestRepository {
    fun get(): String
}

interface TestRepository2

class DefaultTestRepository() : TestRepository {
    override fun get(): String {
        return "Im Test Repository"
    }
}

class DefaultTestRepository2() : TestRepository2

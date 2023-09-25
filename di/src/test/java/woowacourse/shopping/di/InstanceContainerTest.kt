package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test
import woowacourse.shopping.di.container.InstanceContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

class InstanceContainerTest {
    class FakeInstanceContainer(instances: List<Any>? = null) : InstanceContainer {
        private val _instances: MutableList<Any> = instances?.toMutableList() ?: mutableListOf()
        override val value: List<Any>
            get() = _instances.toList()

        override fun add(instance: Any) {
            _instances.add(instance)
        }

        override fun find(clazz: Any): Any? {
            return value.find { it::class.isSubclassOf(clazz as KClass<out Any>) }
        }

        override fun remove(clazz: KClass<*>) {
            _instances.removeIf { clazz.isSubclassOf(it::class) or (it::class == clazz) }
        }

        override fun clear() {
            _instances.clear()
        }
    }

    @Test
    fun `컨테이너에 인스턴스를 저장한다`() {
        // given
        val container = FakeInstanceContainer()

        // when
        class ClassA()

        val classA = ClassA()
        container.add(classA)

        // then
        assertEquals(1, container.value.size)
    }

    @Test
    fun `ClassA, ClassB가 있는 컨테이너에서 ClassB 인스턴스를 찾는다`() {
        // given
        class ClassA()
        class ClassB()

        val classA = ClassA()
        val classB = ClassB()

        val container = FakeInstanceContainer(listOf(classA, classB))

        // when
        val actual = container.find(classB::class)

        // then
        assertThat(actual).isEqualTo(classB)
    }

    @Test
    fun `ClassA, ClassB가 있는 컨테이너에서 ClassA 인스턴스를 제거한다`() {
        // given
        class ClassA()
        class ClassB()

        val classA = ClassA()
        val classB = ClassB()

        val container = FakeInstanceContainer(listOf(classA, classB))

        // when
        container.remove(classA::class)

        // then
        val actual = container.find(ClassA::class)
        assertThat(actual).isNull()
    }

    @Test
    fun `ClassA, ClassB가 있는 컨테이너에서 모든 인스턴스를 제거한다`() {
        // given
        class ClassA()
        class ClassB()

        val classA = ClassA()
        val classB = ClassB()

        val container = FakeInstanceContainer(listOf(classA, classB))

        // when
        container.clear()

        // then
        assertEquals(0, container.value.size)
    }
}

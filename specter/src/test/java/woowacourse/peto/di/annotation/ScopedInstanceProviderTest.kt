package woowacourse.peto.di.annotation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import woowacourse.peto.di.DependencyKey
import kotlin.reflect.typeOf

class ScopedInstanceProviderTest {
    private lateinit var scopedInstanceProvider: ScopedInstanceProvider
    private val activityScope = Scope.ACTIVITY
    private val viewModelScope = Scope.VIEWMODEL

    private val key1 = DependencyKey(typeOf<String>())
    private val key2 = DependencyKey(typeOf<Int>())

    private val instance1 = "Hello"
    private val instance2 = "World"

    @Before
    fun setUp() {
        scopedInstanceProvider = ScopedInstanceProvider()
    }

    @Test
    fun `computeIfAbsent는 새로운 인스턴스를 추가해야 한다`() {
        // when
        scopedInstanceProvider.computeIfAbsent(activityScope, key1, instance1)
        val actual = scopedInstanceProvider.get(activityScope, key1)

        // then
        assertEquals(instance1, actual)
    }

    @Test
    fun `get은 저장된 인스턴스를 반환해야 한다`() {
        // given
        scopedInstanceProvider.computeIfAbsent(activityScope, key1, instance1)

        // when
        val actual = scopedInstanceProvider.get(activityScope, key1)

        // then
        assertEquals(instance1, actual)
    }

    @Test
    fun `get은 없는 인스턴스에 대해 null을 반환해야 한다`() {
        // when
        val actual = scopedInstanceProvider.get(activityScope, key1)

        // then
        assertNull(actual)
    }

    @Test
    fun `remove는 해당 scope의 모든 인스턴스를 제거해야 한다`() {
        // given
        scopedInstanceProvider.computeIfAbsent(activityScope, key1, instance1)
        scopedInstanceProvider.computeIfAbsent(activityScope, key2, instance2)
        scopedInstanceProvider.computeIfAbsent(viewModelScope, key1, "other instance")

        // when
        scopedInstanceProvider.remove(activityScope)
        val actual1 = scopedInstanceProvider.get(activityScope, key1)
        val actual2 = scopedInstanceProvider.get(activityScope, key2)
        val otherScopeInstance = scopedInstanceProvider.get(viewModelScope, key1)

        // then
        assertNull(actual1)
        assertNull(actual2)
        assertNotNull(otherScopeInstance)
    }
}

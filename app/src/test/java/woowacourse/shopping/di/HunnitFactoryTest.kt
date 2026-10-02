package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test
import woowacourse.di.Inject

class Leaf

class Branch(
    val leaf: Leaf,
)

class InjectionTarget {
    @field:Inject
    lateinit var branch: Branch

    val untouched: Leaf = Leaf()
}

class CycleA(
    val cycleB: CycleB,
)

class CycleB(
    val cycleA: CycleA,
)

class HunnitFactoryTest {
    @Test
    fun `애노테이션이 붙은 필드에 의존성을 재귀적으로 주입한다`() {
        val target = HunnitFactory.createInstance(InjectionTarget::class)

        assertThat(target.branch.leaf).isSameInstanceAs(HunnitFactory.getInstance(Leaf::class))
    }

    @Test
    fun `애노테이션이 없는 필드는 변경하지 않는다`() {
        val target = HunnitFactory.createInstance(InjectionTarget::class)

        assertThat(target.untouched).isNotSameInstanceAs(target.branch.leaf)
    }

    @Test
    fun `순환 의존성이 있으면 경로를 포함한 오류를 낸다`() {
        val error = assertThrows(IllegalStateException::class.java) { HunnitFactory.createInstance(CycleA::class) }

        assertThat(error).hasMessageThat().isEqualTo("의존성: CycleA - CycleB - CycleA")
    }
}

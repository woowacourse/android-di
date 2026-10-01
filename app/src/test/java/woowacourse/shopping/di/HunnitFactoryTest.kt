package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class Leaf

class Branch(
    val leaf: Leaf,
)

class InjectionTarget {
    @field:Inject
    lateinit var branch: Branch

    val untouched: Leaf = Leaf()
}

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
}

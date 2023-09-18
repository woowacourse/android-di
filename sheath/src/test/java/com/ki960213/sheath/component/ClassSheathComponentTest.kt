package com.ki960213.sheath.component

import com.google.common.truth.Truth.assertThat
import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Inject
import com.ki960213.sheath.annotation.NewInstance
import com.ki960213.sheath.annotation.Prototype
import com.ki960213.sheath.annotation.Qualifier
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

internal class ClassSheathComponentTest {

    @Test
    fun `인스턴스화 할 때 생성자의 매개 변수에 주입할 수 있는 SheathComponent가 부족하면 에러가 발생한다`() {
        val sheathComponent1 = SheathComponentFactory.create(Test1::class)

        try {
            sheathComponent1.instantiate(emptyList())
        } catch (e: IllegalArgumentException) {
            assertThat(e)
                .hasMessageThat()
                .isEqualTo("${Test1::class.qualifiedName}의 종속 항목이 존재하지 않아 인스턴스화 할 수 없습니다.")
        }
    }

    @Component
    class Test1(test2: Test2)

    @Component
    class Test2

    @Test
    fun `인스턴스화 할 때 프로퍼티에 주입할 수 있는 SheathComponent가 부족하면 에러가 발생한다`() {
        val sheathComponent = SheathComponentFactory.create(Test3::class)

        try {
            sheathComponent.instantiate(emptyList())
        } catch (e: IllegalArgumentException) {
            assertThat(e)
                .hasMessageThat()
                .isEqualTo("${Test3::class} 클래스의 ${Test3::class.getProperty("test2").name}에 할당할 수 있는 종속 항목이 존재하지 않습니다.")
        }
    }

    private fun KClass<*>.getProperty(name: String): KProperty1<*, *> =
        declaredMemberProperties.find { it.name == name }!!

    @Component
    class Test3 {

        @Inject
        private lateinit var test2: Test2
    }

    @Test
    fun `인스턴스화 할 때 메서드에 주입할 수 있는 SheathComponent가 부족하면 에러가 발생한다`() {
        val sheathComponent = SheathComponentFactory.create(Test4::class)

        try {
            sheathComponent.instantiate(emptyList())
        } catch (e: IllegalArgumentException) {
            assertThat(e)
                .hasMessageThat()
                .isEqualTo("${Test4::class.qualifiedName}의 종속 항목이 존재하지 않아 인스턴스화 할 수 없습니다.")
        }
    }

    @Component
    class Test4 {
        @Inject
        private fun test(test2: Test2): Unit = Unit
    }

    @Test
    fun `인스턴스화 할 때 한정자가 설정되어 있는 종속 항목이 주어지지 않았다면 에러가 발생한다`() {
        val sheathComponent1 = SheathComponentFactory.create(Test23::class)
        val sheathComponent2 = SheathComponentFactory.create(Test26::class)
        sheathComponent2.instantiate(emptyList())
        try {
            sheathComponent1.instantiate(listOf(sheathComponent2))
        } catch (e: IllegalArgumentException) {
            assertThat(e)
                .hasMessageThat()
                .isEqualTo("${Test23::class.qualifiedName}의 종속 항목이 존재하지 않아 인스턴스화 할 수 없습니다.")
        }
    }

    @Component
    class Test23(@Qualifier(Test25::class) test24: Test24)

    interface Test24

    @Component
    class Test25 : Test24

    @Component
    class Test26 : Test24

    @Test
    fun `인스턴스화 하면 인스턴스가 할당된다`() {
        val sheathComponent1 = SheathComponentFactory.create(Test1::class)
        val sheathComponent2 = SheathComponentFactory.create(Test2::class)
        sheathComponent2.instantiate(emptyList())

        sheathComponent1.instantiate(listOf(sheathComponent2))

        assertThat(sheathComponent1.instance).isInstanceOf(Test1::class.java)
    }

    @Test
    fun `새로운 인스턴스를 반환하면 이전 인스턴스와 다른 새 인스턴스가 반환된다`() {
        val sheathComponent1 = SheathComponentFactory.create(Test1::class)
        val sheathComponent2 = SheathComponentFactory.create(Test2::class)
        sheathComponent2.instantiate(emptyList())
        sheathComponent1.instantiate(listOf(sheathComponent2))

        val actual = sheathComponent1.getNewInstance()

        assertThat(actual).isNotEqualTo(sheathComponent1.instance)
    }

    @Test
    fun `새 인스턴스 반환 시 생성자의 종속 항목이 싱글톤이면 같은 종속 항목을 주입받는다`() {
        val sheathComponent1 = SheathComponentFactory.create(Test5::class)
        val sheathComponent2 = SheathComponentFactory.create(Test6::class)
        sheathComponent2.instantiate(emptyList())
        sheathComponent1.instantiate(listOf(sheathComponent2))

        val actual = (sheathComponent1.getNewInstance() as Test5).test6

        assertThat(actual).isEqualTo(sheathComponent2.instance)
    }

    @Component
    class Test5(val test6: Test6)

    @Component
    class Test6

    @Test
    fun `새 인스턴스 반환 시 생성자의 종속 항목이 싱글톤이 아니면 새 종속 항목을 주입받는다`() {
        val sheathComponent1 = SheathComponentFactory.create(Test7::class)
        val sheathComponent2 = SheathComponentFactory.create(Test8::class)
        sheathComponent2.instantiate(emptyList())
        sheathComponent1.instantiate(listOf(sheathComponent2))

        val actual = (sheathComponent1.getNewInstance() as Test7).test8

        assertThat(actual).isNotEqualTo(sheathComponent2.instance)
    }

    @Component
    class Test7(val test8: Test8)

    @Prototype
    @Component
    class Test8

    @Test
    fun `새 인스턴스 반환 시 프로퍼티의 종속 항목이 싱글톤이면 같은 종속 항목을 주입받는다`() {
        val sheathComponent1 = SheathComponentFactory.create(Test9::class)
        val sheathComponent2 = SheathComponentFactory.create(Test10::class)
        sheathComponent2.instantiate(emptyList())
        sheathComponent1.instantiate(listOf(sheathComponent2))

        val actual = (sheathComponent1.getNewInstance() as Test9).test10

        assertThat(actual).isEqualTo(sheathComponent2.instance)
    }

    @Component
    class Test9 {
        @Inject
        lateinit var test10: Test10
    }

    @Component
    class Test10

    @Test
    fun `새 인스턴스 반환 시 프로퍼티의 종속 항목이 싱글톤이 아니면 새 종속 항목을 주입받는다`() {
        val sheathComponent1 = SheathComponentFactory.create(Test11::class)
        val sheathComponent2 = SheathComponentFactory.create(Test12::class)
        sheathComponent2.instantiate(emptyList())
        sheathComponent1.instantiate(listOf(sheathComponent2))

        val actual = (sheathComponent1.getNewInstance() as Test11).test12

        assertThat(actual).isNotEqualTo(sheathComponent2.instance)
    }

    @Component
    class Test11 {
        @Inject
        lateinit var test12: Test12
    }

    @Prototype
    @Component
    class Test12

    @Test
    fun `새 인스턴스 반환 시 메서드의 종속 항목이 싱글톤이면 같은 종속 항목을 주입받는다`() {
        val sheathComponent1 = SheathComponentFactory.create(Test13::class)
        val sheathComponent2 = SheathComponentFactory.create(Test14::class)
        sheathComponent2.instantiate(emptyList())
        sheathComponent1.instantiate(listOf(sheathComponent2))

        val actual = (sheathComponent1.getNewInstance() as Test13).test14

        assertThat(actual).isEqualTo(sheathComponent2.instance)
    }

    @Component
    class Test13 {
        lateinit var test14: Test14

        @Inject
        private fun test(test14: Test14) {
            this.test14 = test14
        }
    }

    @Component
    class Test14

    @Test
    fun `새 인스턴스 반환 시 메서드의 종속 항목이 싱글톤이 아니면 새 종속 항목을 주입받는다`() {
        val sheathComponent1 = SheathComponentFactory.create(Test15::class)
        val sheathComponent2 = SheathComponentFactory.create(Test16::class)
        sheathComponent2.instantiate(emptyList())
        sheathComponent1.instantiate(listOf(sheathComponent2))

        val actual = (sheathComponent1.getNewInstance() as Test15).test16

        assertThat(actual).isNotEqualTo(sheathComponent2.instance)
    }

    @Component
    class Test15 {
        lateinit var test16: Test16

        @Inject
        private fun test(test16: Test16) {
            this.test16 = test16
        }
    }

    @Prototype
    @Component
    class Test16

    @Test
    fun `새 인스턴스 반환 시 생성자의 매개 변수에 @NewInstance가 붙어있다면 새 종속 항목을 주입받는다`() {
        val sheathComponent1 = SheathComponentFactory.create(Test17::class)
        val sheathComponent2 = SheathComponentFactory.create(Test18::class)
        sheathComponent2.instantiate(emptyList())
        sheathComponent1.instantiate(listOf(sheathComponent2))

        val actual = (sheathComponent1.getNewInstance() as Test17).test18

        assertThat(actual).isNotEqualTo(sheathComponent2.instance)
    }

    @Component
    class Test17(@NewInstance val test18: Test18)

    @Component
    class Test18

    @Test
    fun `새 인스턴스 반환 시 프로퍼티에 @NewInstance가 붙어있다면 새 종속 항목을 주입받는다`() {
        val sheathComponent1 = SheathComponentFactory.create(Test19::class)
        val sheathComponent2 = SheathComponentFactory.create(Test20::class)
        sheathComponent2.instantiate(emptyList())
        sheathComponent1.instantiate(listOf(sheathComponent2))

        val actual = (sheathComponent1.getNewInstance() as Test19).test20

        assertThat(actual).isNotEqualTo(sheathComponent2.instance)
    }

    @Component
    class Test19 {
        @NewInstance
        @Inject
        lateinit var test20: Test20
    }

    @Component
    class Test20

    @Test
    fun `새 인스턴스 반환 시 메서드의 매개 변수에 @NewInstance가 붙어있다면 새 종속 항목을 주입받는다`() {
        val sheathComponent1 = SheathComponentFactory.create(Test21::class)
        val sheathComponent2 = SheathComponentFactory.create(Test22::class)
        sheathComponent2.instantiate(emptyList())
        sheathComponent1.instantiate(listOf(sheathComponent2))

        val actual = (sheathComponent1.getNewInstance() as Test21).test22

        assertThat(actual).isNotEqualTo(sheathComponent2.instance)
    }

    @Component
    class Test21 {
        lateinit var test22: Test22

        @Inject
        private fun test(@NewInstance test22: Test22) {
            this.test22 = test22
        }
    }

    @Component
    class Test22
}

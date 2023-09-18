package com.di.berdi

import androidx.appcompat.app.AppCompatActivity
import com.di.berdi.annotation.Inject
import com.di.berdi.annotation.Qualifier
import com.di.berdi.annotation.Singleton
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType

@RunWith(RobolectricTestRunner::class)
class InjectorTest {

    interface FakeParam
    interface FakeProperty

    class FakeConstructorParam : FakeParam
    class FakeDefaultProperty : FakeProperty
    class FakeTestQualifierParam : FakeParam
    class FakeSingletonProperty : FakeProperty

    object FakeModule : Module {

        @Singleton
        fun provideFakeConstructorParam(): FakeParam = FakeConstructorParam()

        @Qualifier(qualifiedName = "TestQualifierParam")
        fun provideFakeTestQualifierProperty(): FakeParam = FakeTestQualifierParam()

        fun provideFakeDefaultProperty(): FakeProperty = FakeDefaultProperty()

        @Qualifier(qualifiedName = "SingletonProperty")
        @Singleton
        fun provideFakeInjectSingletonProperty(): FakeProperty = FakeSingletonProperty()
    }

    private lateinit var injector: Injector

    @Before
    fun setup() {
        injector = Injector(Container(), FakeModule)
    }

    @Test
    fun `인스턴스를 생성하면 생성자와 필드에 맞는 타입의 객체가 주입되어 생성된다`() {
        // given
        class FakeSomething(val fakeParam: FakeParam) {
            @Inject
            lateinit var fakeProperty: FakeProperty
        }

        val activity = Robolectric
            .buildActivity(AppCompatActivity::class.java)
            .create()
            .get()

        val constructor = requireNotNull(FakeSomething::class.primaryConstructor)

        // when
        val actual = injector.createBy(activity, constructor)

        // then
        assertSoftly {
            it.assertThat(actual).isInstanceOf(FakeSomething::class.java)
            it.assertThat(actual.fakeParam).isExactlyInstanceOf(FakeConstructorParam::class.java)
            it.assertThat(actual.fakeProperty).isExactlyInstanceOf(FakeDefaultProperty::class.java)
        }
    }

    @Test
    fun `Qualifier 어노테이션을 지정해주면 qualifiedName 과 타입에 맞는 객체가 생성되어 주입된다`() {
        // given
        class FakeSomething(@Qualifier(qualifiedName = "TestQualifierParam") val fakeParam: FakeParam)

        val activity = Robolectric
            .buildActivity(AppCompatActivity::class.java)
            .create()
            .get()

        val constructor = requireNotNull(FakeSomething::class.primaryConstructor)

        // when
        val actual = injector.createBy(activity, constructor)

        // then
        assertSoftly {
            it.assertThat(actual).isInstanceOf(FakeSomething::class.java)
            // and
            it.assertThat(actual.fakeParam).isExactlyInstanceOf(FakeTestQualifierParam::class.java)
        }
    }

    @Test
    fun `모듈에 Singleton 어노테이션을 지정해주면 서로 다른 생성자에 같은 객체가 주입된다`() {
        // given
        class FakeFirstViewModel(val fakeParam: FakeParam)
        class FakeSecondViewModel(val fakeParam: FakeParam)

        val activity = Robolectric
            .buildActivity(AppCompatActivity::class.java)
            .create()
            .get()

        val fakeFirstVMConstructor = requireNotNull(FakeFirstViewModel::class.primaryConstructor)
        val fakeSecondVMConstructor = requireNotNull(FakeSecondViewModel::class.primaryConstructor)

        // when
        val first = injector.createBy(activity, fakeFirstVMConstructor)
        val second = injector.createBy(activity, fakeSecondVMConstructor)

        // then
        assertSoftly {
            // and
            it.assertThat(first).isInstanceOf(FakeFirstViewModel::class.java)
            it.assertThat(second).isInstanceOf(FakeSecondViewModel::class.java)
            // and
            it.assertThat(first.fakeParam).isExactlyInstanceOf(FakeConstructorParam::class.java)
            it.assertThat(second.fakeParam).isExactlyInstanceOf(FakeConstructorParam::class.java)
            // and
            it.assertThat(first.fakeParam).isSameAs(second.fakeParam)
        }
    }

    @Test
    fun `Singleton 어노테이션을 지정해주면 새로 생성하지 않고 저장된 인스턴스를 반환한다`() {
        // given
        val activity = Robolectric
            .buildActivity(AppCompatActivity::class.java)
            .create()
            .get()

        val type = FakeProperty::class.starProjectedType

        // when
        val first = injector.getInstanceOf(
            context = activity,
            type = type,
            qualifiedName = "SingletonProperty",
        )
        val second = injector.getInstanceOf(
            context = activity,
            type = type,
            qualifiedName = "SingletonProperty",
        )

        // then
        assertSoftly {
            it.assertThat(first).isInstanceOf(FakeSingletonProperty::class.java)
            // and
            it.assertThat(first).isSameAs(second)
        }
    }
}

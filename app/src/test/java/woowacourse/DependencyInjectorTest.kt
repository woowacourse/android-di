package woowacourse

import com.woowacourse.bunadi.annotation.Inject
import com.woowacourse.bunadi.annotation.Qualifier
import com.woowacourse.bunadi.annotation.Singleton
import com.woowacourse.bunadi.injector.DependencyInjector
import com.woowacourse.bunadi.injector.DependencyKey
import com.woowacourse.bunadi.util.core.Cache
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.fakeClasses.AConstructorDependency
import woowacourse.fakeClasses.AFieldDependency
import woowacourse.fakeClasses.AFieldDependencyQualifier
import woowacourse.fakeClasses.BFieldDependency
import woowacourse.fakeClasses.BFieldDependencyQualifier
import woowacourse.fakeClasses.ConstructorTestActivity
import woowacourse.fakeClasses.FieldTestActivity
import java.lang.IllegalArgumentException
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.isAccessible

@RunWith(RobolectricTestRunner::class)
class DependencyInjectorTest {

    @Before
    fun setup() {
        DependencyInjector.clear()
    }

    @Test
    fun 뷰모델의_생성자_종속_항목을_자동_주입해준다() {
        // given: 뷰모델 생성에 필요한 종속 항목을 주입한다.

        // when: 액티비티를 생성한다.
        val activity = Robolectric.buildActivity(ConstructorTestActivity::class.java)
            .create()
            .get()

        // then: 액티비티의 뷰모델에 종속 항목이 주입되어 있다.
        assertNotNull(activity.viewModel)
        activity.viewModel.run {
            assertTrue(constructorDependency is AConstructorDependency)
        }
    }

    @Test
    fun 클래스_멤버_프로퍼티에_Inject_애노테이션이_있으면_종송_항목을_주입한다() {
        // given: 뷰모델 생성과 필드에 필요한 종속 항목을 주입한다.

        // when: 액티비티를 생성한다.
        val activity = Robolectric.buildActivity(FieldTestActivity::class.java)
            .create()
            .get()

        // then: Inject 애노테이션이 포함된 뷰모델의 필드에 종속 항목이 주입되어 있다.
        assertNotNull(activity.viewModel)
        activity.viewModel.run {
            assertTrue(fieldWithInjectAnnotation is AFieldDependency)
        }
    }

    @Test
    fun 클래스_멤버_프로퍼티에_Inject_애노테이션이_없으면_종속_항목을_주입하지_않는다() {
        // given: 뷰모델 생성과 필드에 필요한 종속 항목을 주입한다.

        // when: 액티비티를 생성한다.
        val activity = Robolectric.buildActivity(FieldTestActivity::class.java)
            .create()
            .get()

        // then: Inject 애노테이션이 포함된 뷰모델의 필드에 종속 항목이 주입되어 있다.
        assertNotNull(activity.viewModel)
        activity.viewModel.run {
            assertFalse(isFieldWithoutInjectAnnotationInitialized())
        }
    }

    @Test
    fun 클래스_멤버_프로퍼티가_복수개여도_종속_항목을_주입한다() {
        // given: 멤버 프로퍼티가 2개인 클래스가 존재한다.
        class TwoMemberPropertyClass {
            @Inject
            @AFieldDependencyQualifier
            private lateinit var first: AFieldDependency

            @Inject
            @BFieldDependencyQualifier
            private lateinit var second: BFieldDependency

            fun isAllMemberPropertyInitialized(): Boolean {
                return ::first.isInitialized && ::second.isInitialized
            }
        }

        // when: 클래스를 주입한다.
        val actual = DependencyInjector.inject(TwoMemberPropertyClass::class)

        // then: 멤버 프로퍼티가 모두 주입되어 있다.
        assertTrue(actual.isAllMemberPropertyInitialized())
    }

    @Test
    fun 클래스_멤버_프로퍼티가_복수일_때_Inject_애노테이션이_없는_멤버는_주입하지_않는다() {
        // given: 멤버 프로퍼티가 2개인 클래스가 존재한다.
        class TwoMemberPropertyClass {
            @Inject
            @AFieldDependencyQualifier
            private lateinit var first: AFieldDependency

            @BFieldDependencyQualifier
            private lateinit var second: BFieldDependency

            fun isFirstInitialized(): Boolean {
                return ::first.isInitialized
            }

            fun isSecondInitialized(): Boolean {
                return ::second.isInitialized
            }
        }

        // when: 클래스를 주입한다.
        val twoMemberClass = DependencyInjector.inject(TwoMemberPropertyClass::class)

        // then: second 멤버는 초기화되어 있지 않다.
        assertTrue(twoMemberClass.isFirstInitialized())
        assertFalse(twoMemberClass.isSecondInitialized())
    }

    // given: 클래스의 프로퍼티도 프로퍼티가 필요하다.
    @Qualifier(Inner::class)
    annotation class InnerQualifier

    @Qualifier(NestedInner::class)
    annotation class NestedInnerQualifier

    @Singleton
    class Outer(@InnerQualifier val inner: Inner)
    class Inner(@NestedInnerQualifier val realInner: NestedInner)
    class NestedInner

    @Test
    fun 클래스에_프로퍼티가_재귀적으로_존재하면_재귀적으로_주입하여_생성한다() {
        // given: 클래스를 재귀적으로 생성할 수 있는 종속 항목을 주입한다.
        val cache = getDependencyInjectorCache()
        val outerKey = DependencyKey.createDependencyKey(Outer::class)

        // when: 주입했던 클래스를 받아온다.
        val actual = DependencyInjector.inject(Outer::class)
        val expected = cache[outerKey]

        // then: 주입했던 클래스를 재귀적으로 생성하여 반환한다.
        assert(actual == expected)
    }

    private fun getDependencyInjectorCache(): Cache {
        val cacheProperty = DependencyInjector::class
            .declaredMemberProperties
            .find { it.returnType == Cache::class.starProjectedType }
            ?.also { it.isAccessible = true }
            ?: throw IllegalStateException("[ERROR] 캐시가 없습니다.")

        return cacheProperty.call() as? Cache
            ?: throw IllegalStateException("[ERROR] 캐시를 생성할 수 없습니다.")
    }

    interface Dependency

    @Test
    fun 인터페이스_타입인_프로퍼티에_식별자_애노테이션을_추가하지_않으면_주입할_때_예외가_발생하다() {
        // given: 인터페이스 타입을 생성자로 프로퍼티를 가진 클래스가 존재한다.
        class InterfacePropertyClass(
            private val dependency: Dependency,
        )

        // when: 클래스를 주입한다.
        // then: 식별자 애노테이션이 없으므로 예외가 발생한다.
        assertThrows<IllegalArgumentException> {
            DependencyInjector.inject(InterfacePropertyClass::class)
        }
    }
}

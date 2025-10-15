package woowacourse.shopping.di

import androidx.lifecycle.ViewModel

interface FakeRepository

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class FooQualifier

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class BarQualifier

class FooFakeRepository : FakeRepository

class BarFakeRepository : FakeRepository

// (1) 단일 구현체 - 성공
class SingleImplViewModel(
    val repo: FakeRepository,
) : ViewModel()

// (2) 다중 구현체, Qualifier 없음 - 실패 예상
class NoQualifierViewModel(
    val repo: FakeRepository,
) : ViewModel()

// (3) 다중 구현체, FooQualifier 지정 - 성공
class FooQualifierViewModel(
    @FooQualifier val repo: FakeRepository,
) : ViewModel()

// (4) 다중 구현체, BarQualifier 지정 - 성공
class BarQualifierViewModel(
    @BarQualifier val repo: FakeRepository,
) : ViewModel()

// @RunWith(RobolectricTestRunner::class)
// class QualifierInjectionTest {
//    private val container = TestAppContainer()
//
//    @Test
//    fun `단일 구현체만 있을 때 Qualifier 없이 주입 성공`() {
//        val fooRepo = FooFakeRepository()
//        container.addDependency(FakeRepository::class, fooRepo)
//
//        val factory = ConstructorInjectViewModelFactory(container)
//        val viewModel = factory.create(SingleImplViewModel::class.java)
//
//        assertThat(fooRepo).isEqualTo(viewModel.repo)
//    }
//
//    @Test(expected = IllegalArgumentException::class)
//    fun `두 개의 구현체가 등록되어 있고 Qualifier가 없으면 실패`() {
//        container.addDependency(FakeRepository::class, FooFakeRepository())
//        container.addDependency(FakeRepository::class, BarFakeRepository())
//
//        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()
//
//        activity.viewModelNoQualifier // 실패 지점
//    }
//
//    @Test
//    fun `두 개의 구현체가 등록되어 있고 FooQualifier가 있으면 Foo 주입 성공`() {
//        // given
//        val fooRepo = FooFakeRepository()
//        val barRepo = BarFakeRepository()
//        container.addDependency(FakeRepository::class, fooRepo, FooQualifier::class)
//        container.addDependency(FakeRepository::class, barRepo, BarQualifier::class)
//
//        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()
//
//        // when
//        val viewModel = activity.viewModelWithFooQualifier
//
//        // then
//        assertEquals(fooRepo, viewModel.fakeRepository)
//    }
//
//    @Test
//    fun `두 개의 구현체가 등록되어 있고 BarQualifier가 있으면 Bar 주입 성공`() {
//        // given
//        val fooRepo = FooFakeRepository()
//        val barRepo = BarFakeRepository()
//        container.addDependency(FakeRepository::class, fooRepo, FooQualifier::class)
//        container.addDependency(FakeRepository::class, barRepo, BarQualifier::class)
//
//        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()
//
//        // when
//        val viewModel = activity.viewModelWithBarQualifier
//
//        // then
//        assertEquals(barRepo, viewModel.fakeRepository)
//    }
//
//    @After
//    fun tearDown() {
//        container.clear()
//    }
// }

package io.hyemdooly.di

import io.hyemdooly.di.annotation.InMemory
import io.hyemdooly.di.annotation.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InjectorTest {

    class FakeDatabase

    interface FakeRepository {
        @Inject
        val items: List<String>
    }

    @InMemory
    class DefaultFakeRepository(private val database: FakeDatabase) : FakeRepository {
        @Inject
        override val items: List<String> = emptyList()
    }

    class FakeViewModel(
        @InMemory val fakeRepository: FakeRepository,
    )

    @BeforeEach
    fun init() {
        Container.clear()
    }

    @Test
    fun `Container에서 타입에 맞는 instance를 찾아 의존성을 주입한다`() {
        // given
        val items = listOf("item1", "item2", "item3")

        // when
        Container.addInstance(List::class, items)
        Container.addInstance(FakeRepository::class, Injector.inject(FakeDatabase::class))
        Container.addInstance(FakeRepository::class, Injector.inject(DefaultFakeRepository::class))
        val viewModel = Injector.inject<FakeViewModel>(FakeViewModel::class)

        // then
        assertNotNull(viewModel)
        assertNotNull(viewModel.fakeRepository)
        assertEquals(items, viewModel.fakeRepository.items)
    }

    @Test
    fun `Container에서 찾을 수 없는 instance는 재귀로 생성하여 주입한다`() {
        // given
        val items = listOf("item1", "item2", "item3")

        // when
        Container.addInstance(List::class, items)
        Container.addInstance(FakeRepository::class, Injector.inject(DefaultFakeRepository::class))
        val viewModel = Injector.inject<FakeViewModel>(FakeViewModel::class)

        // then
        assertNotNull(viewModel)
        assertNotNull(viewModel.fakeRepository)
        assertEquals(items, viewModel.fakeRepository.items)
    }

    @Test
    fun `주입해야하는 인스턴스를 Container에서 찾고 Injector로 생성할 수 없다면 에러를 띄운다`() {
        // given

        // when

        // then
        assertThrows<IllegalArgumentException> {
            Injector.inject<FakeViewModel>(FakeViewModel::class)
        }
    }
}

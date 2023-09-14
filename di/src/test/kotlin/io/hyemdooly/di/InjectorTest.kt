package io.hyemdooly.di

import io.hyemdooly.di.annotation.Inject
import io.hyemdooly.di.annotation.Qualifier
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InjectorTest {
    class DbModule : Module {
        fun provideDatabase(): FakeDatabase = DefaultFakeDatabase()
    }

    class NameModule : Module {
        fun provideName(): FakeName = DefaultFakeName()
    }

    interface FakeDatabase
    class DefaultFakeDatabase : FakeDatabase

    interface FakeName {
        val name: String
    }
    class DefaultFakeName : FakeName {
        override val name: String = "SONG"
    }

    interface FakeRepository {
        var name: FakeName
    }

    class DefaultFakeRepository(@Qualifier(DefaultFakeDatabase::class) private val database: FakeDatabase) :
        FakeRepository {
        @Inject
        override lateinit var name: FakeName
    }

    class FakeViewModel(
        @Qualifier(DefaultFakeRepository::class) val fakeRepository: FakeRepository,
    )

    @BeforeEach
    fun init() {
        Container.clear()
    }

    @Test
    fun `Container에서 타입에 맞는 instance를 찾아 의존성을 주입한다`() {
        // given
        val nameModule = NameModule()
        val dbModule = DbModule()

        // when
        Container.addInstances(nameModule)
        Container.addInstances(dbModule)
        Container.addInstance(Injector.inject(DefaultFakeRepository::class))
        val viewModel = Injector.inject<FakeViewModel>(FakeViewModel::class)

        // then
        assertAll(
            { assertNotNull(viewModel) },
            { assertNotNull(viewModel.fakeRepository) },
            { assertEquals(nameModule.provideName().name, viewModel.fakeRepository.name.name) },
        )
    }

    @Test
    fun `Container에서 찾을 수 없는 instance는 재귀로 생성하여 주입한다`() {
        // given
        val nameModule = NameModule()
        val dbModule = DbModule()

        // when
        Container.addInstances(nameModule)
        Container.addInstances(dbModule)
        val viewModel = Injector.inject<FakeViewModel>(FakeViewModel::class)

        // then
        assertAll(
            { assertNotNull(viewModel) },
            { assertNotNull(viewModel.fakeRepository) },
            { assertEquals(nameModule.provideName().name, viewModel.fakeRepository.name.name) },
        )
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

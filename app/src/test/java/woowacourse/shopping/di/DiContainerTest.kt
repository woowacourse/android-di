package woowacourse.shopping.di

import junit.framework.TestCase.assertTrue
import org.junit.Test

class DiContainerTest {
    interface FakeDiRepository {
        fun get(): String
    }

    interface FakeDiDataSource {
        fun get(): String
    }

    private class FakeViewModel(
        private val diRepository: FakeDiRepository
    ) {
        fun get(): String {
            return diRepository.get()
        }
    }

    private class FakeDiProtoTypeRepository(
        private val diDataSource: FakeDiDataSource
    ) : FakeDiRepository {
        override fun get(): String {
            return diDataSource.get()
        }
    }

    class FakeDiProtoTypeDataSource : FakeDiDataSource {
        override fun get(): String {
            return "FakeDiProtoTypeDataSource"
        }
    }

    private class FakeDiContainer : DiContainer() {
        private val fakeDiDataSource: FakeDiDataSource =
            this.createInstance(FakeDiProtoTypeDataSource::class)
        private val fakeDiRepository: FakeDiRepository =
            this.createInstance(FakeDiProtoTypeRepository::class)
    }

    private val fakeDiContainer = FakeDiContainer()

    @Test
    fun `DiContainer안에 있는 객체를 반환한다 1`() {
        // given & when
        val fakeDiRepository = fakeDiContainer.get(FakeDiRepository::class)

        // then
        assertTrue(fakeDiRepository is FakeDiProtoTypeRepository)
    }

    @Test
    fun `DiContainer안에 있는 객체를 반환한다 2`() {
        // given & when
        val fakeDiDataSource = fakeDiContainer.get(FakeDiDataSource::class)

        // then
        assertTrue(fakeDiDataSource is FakeDiProtoTypeDataSource)
    }

    @Test
    fun `첫번째 생성자 파라미터가 있으면 자동으로 주입하고 객체를 반환한다`() {
        // given & when
        val fakeDiRepository = fakeDiContainer.createInstance(FakeViewModel::class)

        // then
        assertTrue(fakeDiRepository is FakeViewModel)
    }

    @Test
    fun `Getter를 통해 주입된 객체를 반환한다`() {
        // given
        val fakeDiObject = object : DiContainer() {
            val fakeDiDataSource: FakeDiDataSource
                get() = this.createInstance(FakeDiProtoTypeDataSource::class)
        }

        // when
        val fakeDiDataSource = fakeDiObject.get(FakeDiDataSource::class)

        // then
        assertTrue(fakeDiDataSource is FakeDiProtoTypeDataSource)
    }

    @Test
    fun `by lazy로 지연 초기화가 가능하다`() {
        // given
        val fakeDiObject = object : DiContainer() {
            val fakeDiDataSource: FakeDiDataSource
                by lazy { this.createInstance(FakeDiProtoTypeDataSource::class) }
        }

        // when
        val fakeDiDataSource = fakeDiObject.get(FakeDiDataSource::class)

        // then
        assertTrue(fakeDiDataSource is FakeDiProtoTypeDataSource)
    }

    @Test
    fun `DiContainer에서 없는 리포지터리 객체를 요청하면 예외를 발생시킨다`() {
        // given
        class MockRepository

        // when
        runCatching { fakeDiContainer.get(MockRepository::class) }
            // then
            .onSuccess { throw IllegalArgumentException() }
            .onFailure { assertTrue(it is IllegalArgumentException) }
    }
}

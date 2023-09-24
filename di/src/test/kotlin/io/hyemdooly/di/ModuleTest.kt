package io.hyemdooly.di

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class ModuleTest {
    private val fakeParentModule = FakeParentModule()
    private val fakeChildModule = FakeChildModule(fakeParentModule)

    @Test
    fun `싱글톤 어노테이션이 지정된 provider가 생성하는 객체는 싱글톤으로 관리한다`() {
        // given

        // when
        val firstInstance = fakeParentModule.getInstance(FakeDao::class)
        val secondInstance = fakeParentModule.getInstance(FakeDao::class)

        // then
        assertEquals(firstInstance, secondInstance)
    }

    @Test
    fun `싱글톤 어노테이션이 지정되지 않은 provider가 생성하는 객체는 싱글톤이 아니다`() {
        // given

        // when
        val firstInstance = fakeParentModule.getInstance(FakeFamily::class)
        val secondInstance = fakeParentModule.getInstance(FakeFamily::class)

        // then
        assertNotEquals(firstInstance, secondInstance)
    }

    @Test
    fun `type과 일치하는 인스턴스가 없을 경우 type의 자식 클래스 객체를 리턴할 수 있다`() {
        // given
        fakeParentModule.getInstance(FakeDaoImpl::class) // 생성

        // when
        val secondInstance = fakeParentModule.getInstance(FakeDao::class)

        // then
        assertInstanceOf(FakeDaoImpl::class.java, secondInstance)
    }

    @Test
    fun `모듈에서 해당하는 provider가 없으면 부모 모듈로부터 찾아 사용한다`() {
        // given

        // when
        val instance = fakeChildModule.getInstance(FakeDao::class)

        // then
        assertNotNull(instance)
    }

    @Test
    fun `부모 모듈에도 해당하는 provider가 없으면 직접 생성한다`() {
        // given

        // when
        val instance = fakeChildModule.getInstance(FakePerson::class)

        // then
        assertNotNull(instance)
    }

    @Test
    fun `모듈 자기 자신과 부모 모듈을 모두 사용하여 객체를 생성한다`() {
        // given

        // when
        val instance = fakeChildModule.getInstance(FakeRepository::class)
        val dao = fakeParentModule.getInstance(FakeDao::class)

        // then
        assertAll(
            { assertNotNull(instance) },
            { assertEquals((instance as FakeRepositoryImpl).dao, dao) },
        )
    }
}

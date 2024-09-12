package com.example.di

import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test

class DependencyInjectorTest {
    private lateinit var injector: DependencyInjector
    private lateinit var mockDiContainer: DiContainer

    @Before
    fun setup() {
        mockDiContainer = DiContainer()
        injector = DependencyInjector(mockDiContainer)
        val remoteTestDataSource = RemoteTestDataSource()
        mockDiContainer.addInstance(TestDataSource::class, remoteTestDataSource)
    }

    @Test
    fun `inject 시 DiContainer에 인스턴스가 존재하지 않으면 새 인스턴스를 생성한다`() {
        // given: DiContainer에 인스턴스가 존재하지 않을 때

        // when: inject 호출 (Singleton 어노테이션이 없는 RemoteTestDataSource)
        val instance = injector.inject(RemoteTestDataSource::class)

        // then: 새 인스턴스가 반환되었는지 확인
        assertThat(instance is RemoteTestDataSource).isTrue()
    }

    @Test
    fun `inject 시 @Singleton 어노테이션이 없다면 DiContainer에 인스턴스가 존재해도 새 인스턴스를 반환한다`() {
        // given: DiContainer에 RemoteTestDataSource 인스턴스가 미리 존재함
        val existingInstance = RemoteTestDataSource()
        mockDiContainer.addInstance(RemoteTestDataSource::class, existingInstance)

        // when: inject 호출 (Singleton 어노테이션이 없는 RemoteTestDataSource)
        val newInstance = injector.inject(RemoteTestDataSource::class)

        // then: 새 인스턴스가 반환되었는지 확인
        assertThat(newInstance is RemoteTestDataSource).isTrue()
        assertThat(newInstance !== existingInstance).isTrue()
    }

    @Test
    fun `inject 시 @Singleton 어노테이션이 있다면 기존의 인스턴스를 반환한다`() {
        // given: inject 호출 (Singleton 어노테이션이 있는 SingletonDataSource)
        val singletonInstance = injector.inject(SingletonDataSource::class)

        // when: inject 재호출
        val anotherSingletonInstance = injector.inject(SingletonDataSource::class)

        // then: 기존의 인스턴스가 반환되었는지 확인
        assertSame(singletonInstance, anotherSingletonInstance)
    }

    @Test
    fun `inject 어노테이션이 있는 필드에 자동 의존성 주입을 수행한다`() {
        val testRepository =
            injector.inject(FieldInjectedRepository::class) as FieldInjectedRepository
        assertNotNull(testRepository.dataSource)
    }
}

package com.example.di

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class DependencyInjectorTest {
    private lateinit var container: DiContainer
    private lateinit var injector: DependencyInjector

    @Before
    fun setUp() {
        // fixture : DiContainer와 DependencyInjector를 정의한다
        container = DiContainer()
        container.addModule(DatabaseModule, RetrofitModule)
        injector = DependencyInjector(container)
    }

    @Test
    fun `inject로 프레임워크 컴포넌트를 주입할 수 있다`() {
        val result = injector.inject(TestDataSourceImpl::class)
        assertThat(result).isInstanceOf(TestDataSourceImpl::class.java)
    }

    @Test
    fun `inject로 생성자 파라미터 주입을 할 수 있다`() {
        val result = injector.inject(ParameterRepositoryImpl::class)
        assertThat(result).isInstanceOf(ParameterRepositoryImpl::class.java)
    }

    @Test
    fun `inject로 @inject 어노테이션이 있는 필드를 주입할 수 있다`() {
        val instance = injector.inject(FieldRepositoryImpl::class)
        val result = (instance as FieldRepositoryImpl).testDataSource

        assertThat(instance).isInstanceOf(FieldRepositoryImpl::class.java)
        assertThat(result).isInstanceOf(TestDataSource::class.java)
    }

    @Test(expected = UninitializedPropertyAccessException::class)
    fun `inject는 @inject 어노테이션이 없는 필드는 주입하지 못한다`() {
        val instance =
            injector.inject(TestDataSourceNoAnnotationImpl::class) as TestDataSourceNoAnnotationImpl
        instance.testDao
    }

    @Test(expected = UninitializedPropertyAccessException::class)
    fun `inject는 @inject 어노테이션이 없는 필드는 주입하지 못한다2`() {
        val instance =
            injector.inject(FieldRepositoryNoAnnotationImpl::class) as FieldRepositoryNoAnnotationImpl
        instance.testDataSource
    }

    @Test
    fun `inject로 재귀 주입을 할 수 있다`() {
        val result = injector.inject(TestUseCase::class)
        assertThat(result).isInstanceOf(TestUseCase::class.java)
    }

    @Test
    fun `@Singleton 어노테이션이 없다면 inject시 다른 인스턴스를 반환한다`() {
        val result1 = injector.inject(TestUseCase::class)
        val result2 = injector.inject(TestUseCase::class)
        assertThat(result1).isNotSameInstanceAs(result2)
    }

    @Test
    fun `@Singleton 어노테이션이 있다면 inject시 동일한 인스턴스를 반환한다`() {
        val result1 = injector.inject(SingletonTestUseCase::class)
        val result2 = injector.inject(SingletonTestUseCase::class)
        assertThat(result1).isSameInstanceAs(result2)
    }
}

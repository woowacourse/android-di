package com.example.di

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class InstanceContainerTest {
    private lateinit var sourceContainer: SourceContainer
    private lateinit var instanceContainer: InstanceContainer

    @Before
    fun setUp() {
        // fixture : Container를 정의한다
        sourceContainer = SourceContainer()
        sourceContainer.addModule(DatabaseModule, RetrofitModule)
        instanceContainer = InstanceContainer(sourceContainer)
    }

    @Test
    fun `inject로 프레임워크 컴포넌트를 주입할 수 있다`() {
        val result = instanceContainer.inject(TestDataSourceImpl::class)
        assertThat(result).isInstanceOf(TestDataSourceImpl::class.java)
    }

    @Test
    fun `inject로 생성자 파라미터 주입을 할 수 있다`() {
        val result = instanceContainer.inject(ParameterRepositoryImpl::class)
        assertThat(result).isInstanceOf(ParameterRepositoryImpl::class.java)
    }

    @Test
    fun `inject로 @inject 어노테이션이 있는 필드를 주입할 수 있다`() {
        val instance = instanceContainer.inject(FieldRepositoryImpl::class)
        val result = (instance as FieldRepositoryImpl).testDataSource

        assertThat(instance).isInstanceOf(FieldRepositoryImpl::class.java)
        assertThat(result).isInstanceOf(TestDataSource::class.java)
    }

    @Test(expected = UninitializedPropertyAccessException::class)
    fun `inject는 @inject 어노테이션이 없는 필드는 주입하지 못한다`() {
        val instance =
            instanceContainer.inject(TestDataSourceNoAnnotationImpl::class) as TestDataSourceNoAnnotationImpl
        instance.testDao
    }

    @Test(expected = UninitializedPropertyAccessException::class)
    fun `inject는 @inject 어노테이션이 없는 필드는 주입하지 못한다2`() {
        val instance =
            instanceContainer.inject(FieldRepositoryNoAnnotationImpl::class) as FieldRepositoryNoAnnotationImpl
        instance.testDataSource
    }

    @Test
    fun `inject로 재귀 주입을 할 수 있다`() {
        val result = instanceContainer.inject(TestUseCase::class)
        assertThat(result).isInstanceOf(TestUseCase::class.java)
    }

    @Test
    fun `@Singleton 어노테이션이 없다면 inject시 다른 인스턴스를 반환한다`() {
        val result1 = instanceContainer.inject(TestUseCase::class)
        val result2 = instanceContainer.inject(TestUseCase::class)
        assertThat(result1).isNotSameInstanceAs(result2)
    }

    @Test
    fun `@Singleton 어노테이션이 있다면 inject시 동일한 인스턴스를 반환한다`() {
        val result1 = instanceContainer.inject(SingletonTestUseCase::class)
        val result2 = instanceContainer.inject(SingletonTestUseCase::class)
        assertThat(result1).isSameInstanceAs(result2)
    }
}

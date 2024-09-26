package com.example.di

import com.example.di.annotation.Inject
import com.example.di.annotation.Qualifier
import com.example.di.annotation.Singleton
import com.example.di.annotation.lifecycle.LifeCycle

class TestUseCase(
    @Inject
    @Qualifier(ParameterRepositoryImpl::class)
    private val parameterRepository: TestRepository,
) {
    @Inject
    @Qualifier(FieldRepositoryImpl::class)
    private lateinit var fieldRepository: TestRepository
}

@Singleton
class SingletonTestUseCase(
    @Inject
    @Qualifier(ParameterRepositoryImpl::class)
    private val parameterRepository: TestRepository,
) {
    @Inject
    @Qualifier(FieldRepositoryImpl::class)
    private lateinit var fieldRepository: TestRepository
}

class ParameterRepositoryImpl(
    @Inject
    @Qualifier(TestDataSourceImpl::class)
    private val testDataSource: TestDataSource,
) : TestRepository

class FieldRepositoryImpl : TestRepository {
    @Inject
    @Qualifier(TestDataSourceImpl::class)
    lateinit var testDataSource: TestDataSource
}

class FieldRepositoryNoAnnotationImpl : TestRepository {
    lateinit var testDataSource: TestDataSource
}

class TestDataSourceImpl(
    @Inject
    private val testDao: TestDao,
) : TestDataSource

class TestDataSourceNoAnnotationImpl : TestDataSource {
    lateinit var testDao: TestDao
}

interface TestRepository

interface TestDataSource

@LifeCycle
annotation class ExampleLifeCycle

class ConstructorDeleteTestParent1(
    @Inject
    @ExampleLifeCycle
    private val constructorDeleteTestChild: ConstructorDeleteTestChild1,
)

class ConstructorDeleteTestParent2(
    @Inject
    @ExampleLifeCycle
    val constructorDeleteTestChild1: ConstructorDeleteTestChild1,
    @Inject
    val constructorDeleteTestChild2: ConstructorDeleteTestChild2,
)

class ConstructorDeleteTestChild1

class ConstructorDeleteTestChild2

class FieldDeleteTestParent1 {
    @Inject
    @ExampleLifeCycle
    lateinit var fieldDeleteTestChild: FieldDeleteTestChild1
}

class FieldDeleteTestParent2 {
    @Inject
    @ExampleLifeCycle
    lateinit var fieldDeleteTestChild1: FieldDeleteTestChild1

    @Inject
    lateinit var fieldDeleteTestChild2: FieldDeleteTestChild2
}

class FieldDeleteTestChild1

class FieldDeleteTestChild2

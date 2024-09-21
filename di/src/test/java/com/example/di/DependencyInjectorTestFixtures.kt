package com.example.di

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

class ARepository : TestRepository

class BRepository : TestRepository

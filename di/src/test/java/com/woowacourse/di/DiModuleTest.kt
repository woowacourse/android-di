package com.woowacourse.di

import com.google.common.truth.Truth.assertThat
import com.woowacourse.di.testfixture.FakeContext
import com.woowacourse.di.testfixture.FakeDatabaseModule
import com.woowacourse.di.testfixture.FakeProductRepository
import com.woowacourse.di.testfixture.FakeRepositoryModule
import org.junit.Before
import org.junit.Test
import kotlin.reflect.KClass

class DiModuleTest {
    private lateinit var fakeContext: FakeContext
    private lateinit var diModule: DiModule

    @Before
    fun setUp() {
        fakeContext = FakeContext()

        val modules: List<KClass<*>> =
            listOf(FakeDatabaseModule::class, FakeRepositoryModule::class)

        DiModule.setInstance(fakeContext, modules)
        diModule = DiModule.getInstance()
    }

    @Test
    fun `FakeProductRepository 인스턴스가 제대로 생성되는지 테스트`() {
        val productRepository = diModule.resolve(FakeProductRepository::class)
        assertThat(productRepository).isNotNull()
    }

    @Test
    fun `FakeRepositoryModule이 FakeProductRepository를 올바르게 제공하는지 테스트`() {
        val repositoryModule = diModule.resolve(FakeRepositoryModule::class)
        assertThat(repositoryModule).isNotNull()
        assertThat(repositoryModule.provideFakeProductRepository()).isNotNull()
    }

    @Test
    fun `FakeDatabaseModule이 FakeContext를 사용하여 데이터베이스 인스턴스를 올바르게 제공하는지 테스트`() {
        val databaseModule = diModule.resolve(FakeDatabaseModule::class)
        assertThat(databaseModule).isNotNull()
        assertThat(databaseModule.provideShoppingDatabase(fakeContext)).isNotNull()
    }
}

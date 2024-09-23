package com.zzang.di

import com.google.common.truth.Truth.assertThat
import com.zzang.di.annotation.QualifierType
import com.zzang.di.testfixture.FakeDatabaseRepository
import com.zzang.di.testfixture.FakeInMemoryRepository
import com.zzang.di.testfixture.TestRepository
import org.junit.Test

class QualifierTest {
    @Test
    fun `Qualifier가 InMemory일때 FakeInMemoryRepository 인스턴스를 가져온다`() {
        // given
        DIContainer.registerSingletonInstance(TestRepository::class, FakeInMemoryRepository(), QualifierType.IN_MEMORY)
        DIContainer.registerSingletonInstance(TestRepository::class, FakeDatabaseRepository(), QualifierType.DATABASE)

        // when
        val inMemoryRepo = DIContainer.resolve(TestRepository::class, QualifierType.IN_MEMORY)

        // then
        assertThat(inMemoryRepo).isInstanceOf(FakeInMemoryRepository::class.java)
    }

    @Test
    fun `Qualifier가 Database일때 FakeDatabaseRepository 인스턴스를 가져온다`() {
        // given
        DIContainer.registerSingletonInstance(TestRepository::class, FakeInMemoryRepository(), QualifierType.IN_MEMORY)
        DIContainer.registerSingletonInstance(TestRepository::class, FakeDatabaseRepository(), QualifierType.DATABASE)

        // when
        val dbRepo = DIContainer.resolve(TestRepository::class, QualifierType.DATABASE)

        // then
        assertThat(dbRepo).isInstanceOf(FakeDatabaseRepository::class.java)
    }

    @Test
    fun `Qualifier가 없을 경우 기본값인 Database의 FakeDatabaseRepository 인스턴스를 가져온다`() {
        // given
        DIContainer.registerSingletonInstance(TestRepository::class, FakeInMemoryRepository(), QualifierType.IN_MEMORY)
        DIContainer.registerSingletonInstance(TestRepository::class, FakeDatabaseRepository(), QualifierType.DATABASE)

        // when
        val dbRepo = DIContainer.resolve(TestRepository::class)

        // then
        assertThat(dbRepo).isInstanceOf(FakeDatabaseRepository::class.java)
    }
}

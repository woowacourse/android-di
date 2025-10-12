package com.medandro.di

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.medandro.di.annotation.InjectField
import com.medandro.di.fixture.TestDao
import com.medandro.di.fixture.TestDatabase
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DIContainerRoomTest {
    private lateinit var database: TestDatabase

    @Before
    fun setUp() {
        database =
            Room
                .inMemoryDatabaseBuilder(
                    ApplicationProvider.getApplicationContext(),
                    TestDatabase::class.java,
                ).allowMainThreadQueries()
                .build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `Room 데이터베이스에서 DAO를 자동 주입한다`() {
        // given
        class TestService {
            @InjectField
            lateinit var testDao: TestDao
        }

        // when
        val diContainer = DIContainer().registerSingleton(database)
        val service = TestService()
        diContainer.injectFields(service)

        // then
        assertThat(service.testDao).isNotNull
        assertThat(service.testDao).isInstanceOf(TestDao::class.java)
    }

    @Test
    fun `DAO와 일반 클래스가 함께 주입된다`() {
        // given
        data class Config(
            val setting: String = "설정",
        )

        class TestService {
            @InjectField
            lateinit var testDao: TestDao

            @InjectField
            lateinit var config: Config
        }

        // when
        val diContainer = DIContainer().registerSingleton(database)
        val service = TestService()
        diContainer.injectFields(service)

        // then
        assertThat(service.testDao).isNotNull
        assertThat(service.config.setting).isEqualTo("설정")
    }

    @Test
    fun `데이터베이스 없이 DAO 주입 시 예외가 발생한다`() {
        // given
        class TestService {
            @InjectField
            lateinit var testDao: TestDao
        }

        // when
        val diContainer = DIContainer()
        val service = TestService()

        // then
        val exception =
            assertThatThrownBy {
                diContainer.injectFields(service)
            }.isInstanceOf(IllegalStateException::class.java)

        exception.hasMessageContaining("필드 주입 실패")
    }
}

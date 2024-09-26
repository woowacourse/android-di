package com.example.di

import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test

class SourceContainerTest {
    private lateinit var sourceContainer: SourceContainer

    @Before
    fun setUp() {
        // fixture : DatabaseModule를 추가한 DiContainer 정의
        sourceContainer = SourceContainer()
        sourceContainer.addModule(DatabaseModule)
    }

    @Test
    fun `getInstanceOrNull로 해당하는 타입의 인스턴스를 찾아 반환한다`() {
        // given : 모듈에서 메서드 인자가 없는 provideTestDataBase 확인

        // when : TestDataBase의 인스턴스를 가져온다
        val result = sourceContainer.getSourceOrNull(TestDataBase::class)

        // then
        Assertions.assertThat(result).isInstanceOf(TestDataBase::class.java)
    }

    @Test
    fun `getInstanceOrNull로 해당하는 타입의 인스턴스를 찾아 반환한다2`() {
        // given : 모듈에서 메서드 인자가 있는 provideTestDao 확인

        // when : TestDao의 인스턴스를 가져온다
        val result = sourceContainer.getSourceOrNull(TestDao::class)

        // then
        Assertions.assertThat(result).isInstanceOf(TestDao::class.java)
    }

    @Test
    fun `getInstanceOrNull는 해당하는 타입의 인스턴스가 없을 때 null을 반환한다`() {
        // given : TestApi의 인스턴스를 제공하는 RetrofitModule를 DiContainer에 추가하지 않았을 때

        // when : TestApi의 인스턴스를 가져온다
        val result = sourceContainer.getSourceOrNull(TestApi::class)

        // then
        Assertions.assertThat(result).isNull()
    }

    @Test
    fun `모듈을 새로 등록한 후 getInstanceOrNul로 해당하는 타입의 인스턴스를 찾아 반환한다`() {
        // given : DiContainer에 RetrofitModule을 추가해 TestApi의 인스턴스를 제공
        sourceContainer.addModule(RetrofitModule)

        // when : TestApi의 인스턴스를 가져온다
        val result = sourceContainer.getSourceOrNull(TestApi::class)

        // then
        Assertions.assertThat(result).isInstanceOf(TestApi::class.java)
    }
}

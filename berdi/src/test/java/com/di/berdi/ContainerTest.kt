package com.di.berdi

import com.di.berdi.fake.DefaultFirstDataSource
import com.di.berdi.fake.DefaultSecondDataSource
import com.di.berdi.fake.FakeObj
import com.di.berdi.fake.FakeRepository
import com.di.berdi.fake.InMemoryFakeRepository
import com.di.berdi.fake.OnDiskFakeRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ContainerTest {

    @Test
    fun `컨테이너에 아무 값도 넣지 않았을 때 값을 찾을 수 없다`() {
        // given
        val container = Container()

        // when
        val actual = container.getInstance(FakeObj::class, "fake")

        // then
        assertThat(actual).isNull()
    }

    @Test
    fun `컨테이너에 가짜 객체가 들어있을 때 kclass 와 qualifiedName 으로 Instance 를 얻을 수 있다`() {
        // given
        val expected = FakeObj
        val container = Container().apply {
            setInstance(FakeObj, FakeObj::class, "fake")
        }

        // when
        val actual = container.getInstance(FakeObj::class, "fake")

        // then
        assertThat(actual).isSameAs(expected)
    }

    @Test
    fun `컨테이너에 같은 인터페이스의 객체가 들어있을 떄 qualifiedName 로 Instance 를 얻을 수 있다`() {
        // given
        val inMemoryRepository: FakeRepository = InMemoryFakeRepository()
        val onDiskRepository: FakeRepository = OnDiskFakeRepository(
            firstDataSource = DefaultFirstDataSource(),
            secondDataSource = DefaultSecondDataSource(),
        )

        val container = Container().apply {
            setInstance(inMemoryRepository, FakeRepository::class, "InMemory")
            setInstance(onDiskRepository, FakeRepository::class, "onDisk")
        }

        // when
        val actual = container.getInstance(FakeRepository::class, "InMemory")

        // then
        assertThat(actual).isSameAs(inMemoryRepository)
    }
}

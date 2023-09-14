package com.app.covi_di.core

import com.app.covi_di.annotation.Inject
import com.app.covi_di.annotation.InjectField
import com.app.covi_di.annotation.Qualifier
import com.app.covi_di.module.DependencyModule
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

internal class InjectorTest {
    @Before
    fun setup() {

    }

    @Test
    fun `객체는 Inject 어노테이션을 가질 수 있다 `() {
        // given
        val instance = FakeRepositoryImpl::class

        // when
        val actual = instance.primaryConstructor?.hasAnnotation<Inject>()

        // then
        assertTrue(actual ?: false)
    }

    @Test
    fun `객체는 InjectField 어노테이션을 가질 수 있다`() {
        // when
        val actual = FakeRepositoryImpl::class.java.fields.any {
            it.isAnnotationPresent(InjectField::class.java)
        }

        // then
        assertTrue(actual)
    }

    @Test
    fun `객체 인자를 주입 후 객체를 반환한다`() {
        // given
        DIContainer.init(listOf(FakeRepositoryModule), listOf())

        // when
        val instance = Injector.inject<FakeRepository>(FakeRepositoryImpl::class)

        // then
        assertTrue(instance is FakeRepositoryImpl)
    }

    @Test
    fun `interface에 구현체가 두개 이상 있으면 Qualifier 어노테이션이 붙은 객체를 가져온다`() {
        // given
        DIContainer.init(listOf(FakeRepositoryModule), listOf())
        val instance = Injector.inject<FakeRepository>(FakeRepositoryImpl::class)

        // when
        assertTrue(instance is FakeRepositoryImpl)
    }

    @Test
    fun `interface에 구현체가 두개 이상 있으면 Qualifier 어노테이션이 붙지 않는 객체는 가져오지 않는다`() {
        // given
        DIContainer.init(listOf(FakeRepositoryModule), listOf())
        val instance = Injector.inject<FakeRepository>(FakeRepositoryImpl::class)

        // when
        assertFalse(instance is FakeRepositoryImpl2)
    }


}

interface FakeRepository



@Qualifier
class FakeRepositoryImpl @Inject constructor(private val fakeClass: FakeClass) : FakeRepository {
    @InjectField
    lateinit var fakeClass2: FakeClass2
}

class FakeRepositoryImpl2() : FakeRepository

interface FakeDataSource

class FakeDataSourceImpl: FakeDataSource


class FakeClass()

class FakeClass2()

object FakeRepositoryModule : DependencyModule {
    override fun invoke(): Map<KClass<*>, KClass<*>> {
        return mapOf(
            FakeRepository::class to FakeRepositoryImpl::class,
            FakeRepository::class to FakeRepositoryImpl2::class
        )
    }
}
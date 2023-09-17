package com.app.covi_di.core

import com.app.covi_di.annotation.Inject
import com.app.covi_di.annotation.InjectField
import com.app.covi_di.annotation.Qualifier
import com.app.covi_di.annotation.QualifierType
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
        val instance = Injector.inject(FakeRepositoryImpl::class)

        // then
        assertTrue(instance is FakeRepositoryImpl)
    }


}

interface FakeRepository


@Qualifier(QualifierType.DB)
class FakeRepositoryImpl @Inject constructor(private val fakeClass: FakeClass) : FakeRepository {
    @InjectField
    lateinit var fakeClass2: FakeClass2
}

class FakeRepositoryImpl2() : FakeRepository

interface FakeDataSource

class FakeViewModel @Inject constructor(
    @Qualifier(QualifierType.DB)
    private val fakeRepository: FakeRepository
)


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
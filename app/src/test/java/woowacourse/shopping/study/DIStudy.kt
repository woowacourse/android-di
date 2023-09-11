package woowacourse.shopping.study

import org.junit.Before
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

interface FakeRepository

interface FakeRepository2

interface FakeDao

class DefaultFakeRepository(fakeDao: FakeDao) : FakeRepository
class DefaultFake2Repository : FakeRepository2

class FakeViewModel(
    fakeRepository2: FakeRepository2,
    fakeRepository: FakeRepository,
) {
    fun print() {
        println("잘 됐네~")
    }
}

class FakeInjector(val name: String) {

    fun provideFakeRepository2(): FakeRepository2 = DefaultFake2Repository()

    fun provideFakeRepository(fakeDao: FakeDao): FakeRepository {
        return DefaultFakeRepository(fakeDao)
    }

    fun provideFakeDao(): FakeDao = object : FakeDao {}
}

class DiTest {
    lateinit var injector: FakeInjector

    @Before
    fun setup() {
        injector = FakeInjector("")
    }

    @Test
    fun `리플렉션 테스트`() {
//        println("public func: ${injector::class.declaredFunctions}")

//        val functions = injector::class.declaredFunctions

//        functions.forEach { function ->
//            when (function.returnType.jvmErasure.qualifiedName) {
//                FakeRepository::class.qualifiedName -> {
//                    println("Here FakeRepo")
//                }
//
//                FakeDao::class.qualifiedName -> {
//                    println("Here FakeDao")
//                }
//            }
//        }

        val clazz = FakeViewModel::class.java

        val primaryConstructor =
            clazz.kotlin.primaryConstructor ?: run {
                println("주생성자가 없네요~")
                return
            }

        val instances = getInstances(primaryConstructor)
        println("인스턴스들을 만들어봤다. : $instances")

        val injectInstance = primaryConstructor.call(*instances.toTypedArray())
        injectInstance.print()
    }

    fun getInstances(kFunction: KFunction<*>): List<Any?> =
        kFunction.valueParameters.map { kParameter ->
            findInstance(kParameter.type.jvmErasure)
        }

    private fun findInstance(kClass: KClass<*>): Any? {
        println("들어온 kClass: $kClass")
        val constructor = kClass.primaryConstructor ?: run {
            requireNotNull(
                injector::class.declaredFunctions
                    .filter { it.visibility == KVisibility.PUBLIC }
                    .find { it.returnType.jvmErasure == kClass },
            )
        }

        val parameters = constructor.valueParameters

        if (parameters.isEmpty()) {
            println("아무것도 없네요 그냥 만들게요~")
            val instance = injector::class.declaredFunctions
                .filter { it.visibility == KVisibility.PUBLIC }
                .find { it.returnType.jvmErasure == kClass }?.call(injector) ?: constructor.call()
            println("만든거 : $instance")
            return instance
        }
        val args =
            constructor.valueParameters.map { findInstance(it.type.jvmErasure) }.toTypedArray()
        return constructor.call(injector, *args)
    }
}

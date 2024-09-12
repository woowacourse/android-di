package com.woowa.di.fixture

import com.woowa.di.injection.Module
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

class FakeModule : Module<FakeModule, FakeDI> {
    private val fakeBinder: FakeBInder = FakeBInder()
    private val fakeList: List<Pair<String, KFunction<FakeDI>>> = createFakes()

    override fun getDIInstance(type: KClass<out FakeDI>): FakeDI {
        val kFunction =
            fakeList?.find { it.first == type.simpleName }?.second
                ?: error("${type.simpleName} 해당 interface에 대한 객체가 없습니다.")
        return kFunction.call(fakeBinder)
    }

    override fun getDIInstance(
        type: KClass<out FakeDI>,
        qualifier: KClass<out Annotation>,
    ): FakeDI {
        val kFunction =
            fakeList.find {
                it.first == type.simpleName &&
                    it.second.annotations.any { it.annotationClass.isSubclassOf(qualifier) }
            }?.second
                ?: error("${type.simpleName} 해당 interface에 대한 객체가 없습니다.")
        return kFunction.call(fakeBinder)
    }

    private fun createFakes(): List<Pair<String, KFunction<FakeDI>>> {
        return FakeBInder::class.declaredMemberFunctions
            .filter { it.returnType.jvmErasure.isSubclassOf(FakeDI::class) }
            .map { kFunction ->
                val key =
                    kFunction.returnType.jvmErasure.simpleName
                        ?: error("$kFunction 의 key값을 지정할 수 없습니다.")
                key to (kFunction as KFunction<FakeDI>)
            }
    }

    companion object {
        fun getInstance(): FakeModule = FakeModule()
    }
}

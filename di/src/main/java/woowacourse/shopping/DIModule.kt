package woowacourse.shopping

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.jvmErasure

open class DIModule {

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> getHowToImplement(
        clazz: KClass<T>,
        qualifier: Annotation?,
    ): KFunction<T>? {
        var functions = this::class.functions.filter {
            it.returnType.jvmErasure == clazz
        }

        if (qualifier != null) {
            functions = functions.filter { it.annotations.contains(qualifier) }
        }

        return when (functions.size) {
            0 -> null
            1 -> functions.first() as KFunction<T>
            else -> throw IllegalStateException("주입을 위한 함수를 특정할 수 없습니다.")
        }
    }
}

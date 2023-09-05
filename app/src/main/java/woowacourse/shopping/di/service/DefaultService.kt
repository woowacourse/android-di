package woowacourse.shopping.di.service

import kotlin.reflect.KClass
import kotlin.reflect.full.superclasses

// 최종적으로 Client(Activty 또는 뷰모델) 이 사용할 서비스
class DefaultService(
    override val type: KClass<*>,
    override val instance: Any,
) : Service {
    companion object {
        fun create(type: KClass<*>, instance: Any): Service {
            if (instance.isMatchWith(type)) {
                return DefaultService(type, instance)
            }
            throw IllegalStateException("MisMatch Type And Instance")
        }

        private fun Any.isMatchWith(type: KClass<*>): Boolean {
            return this::class.superclasses.contains(type) || this::class == type
        }
    }
}

package woowacourse.shopping.di

import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberFunctions

class InstanceContainer(
    modules: List<InjectionModule>,
) {
    private val instances: Map<KType, Any> =
        modules
            .flatMap(::propertyInstance)
            .associateBy { it::class.supertypes.first() }

    fun <T : Any> instanceOf(kType: KType): T {
        val instance = instances[kType]
        checkNotNull(instance) { EXCEPTION_NO_MATCHING_PROPERTY.format(kType) }
        return instance as T
    }

    private fun propertyInstance(module: InjectionModule): List<Any> =
        module::class.declaredMemberFunctions.mapNotNull { memberFunction ->
            memberFunction.call(module)
        }

    companion object {
        private const val EXCEPTION_NO_MATCHING_PROPERTY =
            "No matching property found for parameter %s"
    }
}

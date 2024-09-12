package woowacourse.shopping.di

import kotlin.reflect.KClass

object DependencyRegistry {
    private val instances = mutableMapOf<KClass<*>, Any>()
    private val modules = mutableListOf<Module>()

    fun findImplClassType(classType: KClass<*>): KClass<*> {
        var result: Any? = null
        for (module in modules) {
            val found = module.findQualifierOrNull(classType)
            if (found != null) {
                if (result != null) throw IllegalStateException("여러 개의 의존성이 발견되었습니다.")
                result = found
            }
        }
        return result as? KClass<*>
            ?: throw IllegalArgumentException("인터페이스에 대한 구현체를 찾을 수 없습니다: ${classType.simpleName}")
    }

    fun initModule(vararg module: Module) {
        modules.addAll(module)
        module.forEach { it.provideInstance(this) }
    }

    fun addInstance(
        classType: KClass<*>,
        instance: Any,
    ) {
        instances[classType] = instance
    }

    fun getInstanceOrNull(classType: KClass<*>): Any? {
        return instances[classType]
    }
}

package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object AppContainer {
    private val providers = mutableMapOf<KClass<*>, Any>()

    init {
        provideModule(RepositoryModule::class)
    }

    fun <T : Any> resolve(clazz: KClass<T>): T = providers[clazz] as? T ?: createInstance(clazz)

    private fun <T : Any> createInstance(clazz: KClass<T>): T {
        val constructor =
            requireNotNull(clazz.primaryConstructor) { "${clazz.simpleName}에 public 생성자가 없습니다." }

        return constructor.parameters
            .associateWith { param ->
                requireNotNull(param.type.classifier as? KClass<*>) {
                    "타입 정보를 알 수 없습니다: $param"
                }.let(::resolve)
            }.let(constructor::callBy)
    }

    private fun <T : Any> provideModule(moduleKClazz: KClass<T>) {
        val module = moduleKClazz.objectInstance ?: return

        moduleKClazz.memberProperties.forEach { property ->
            property.isAccessible = true
            val instance = property.get(module) ?: return@forEach
            providers[property.returnType.classifier as KClass<*>] = instance
        }
    }
}

package woowacourse.shopping.ui.injection

import woowacourse.shopping.ui.injection.repository.RepositoryDI
import woowacourse.shopping.ui.injection.repository.RepositoryModule
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

fun <T : DIInjection> createInjectedInstance(clazz: KClass<T>): T {
    val primaryConstructor =
        clazz.primaryConstructor
            ?: throw IllegalArgumentException("주 생성자가 없는 class에서는 해당 로직을 사용할 수 없습니다.")

    val diParameters =
        primaryConstructor.parameters.map { param ->
            val paramType = param.type.jvmErasure
            when {
                paramType.isSubclassOf(RepositoryDI::class) ->
                    RepositoryModule.getInstance()
                        .getRepository(paramType as KClass<out RepositoryDI>)

                else -> error("inject 에러: ${paramType.simpleName}는 DI 모듈을 적용하지 않았습니다")
            }
        }.toTypedArray()

    return primaryConstructor.call(*diParameters)
}

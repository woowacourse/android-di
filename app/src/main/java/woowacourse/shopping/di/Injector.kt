package woowacourse.shopping.di

import woowacourse.shopping.di.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

class Injector(private val appContainer: AppContainer) {

    fun <T : Any> inject(clazz: KClass<T>): T {
        val primaryConstructor =
            clazz.primaryConstructor ?: throw IllegalArgumentException("주생성자 없음")

        val filteredParams = primaryConstructor.parameters.filter { it.hasAnnotation<Inject>() }

        val args = filteredParams.associateWith { getInstanceFromContainer(it) }

        return primaryConstructor.callBy(args)
    }

    private fun getInstanceFromContainer(parameter: KParameter): Any {
        return appContainer.getInstance(parameter.type)
    }
}

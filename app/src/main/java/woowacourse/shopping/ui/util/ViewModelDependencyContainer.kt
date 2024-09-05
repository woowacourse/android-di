package woowacourse.shopping.ui.util

import woowacourse.shopping.data.repository.CartDefaultRepository
import woowacourse.shopping.data.repository.ProductDefaultRepository
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.full.createInstance

object ViewModelDependencyContainer : DependencyContainer {
    private val dependencies: Map<KClassifier, KClass<*>> =
        mapOf(
            ProductRepository::class to ProductDefaultRepository::class,
            CartRepository::class to CartDefaultRepository::class
        )

    override fun <T : Any> getInstance(kClassifier: KClassifier): T {
        val kClass = dependencies[kClassifier] ?: throw IllegalArgumentException("$kClassifier : 알 수 없는 클래스 지정자 입니다.")
        return kClass.createInstance() as T
    }
}

package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

class DependencyInjector(
    private val appContainer: AppContainer,
) {
    inline fun <reified VM : ViewModel> inject(): VM {
        val viewModelKClass: KClass<VM> = VM::class
        val primaryConstructor = viewModelKClass.primaryConstructor

        // 생성자 파라미터 → 의존성 주입
        return if (primaryConstructor != null) {
            val constructorParams: List<KParameter> = primaryConstructor.parameters
            val parameters: List<Any?> = constructorParams.map(::injectInstance)
            primaryConstructor.call(*parameters.toTypedArray())
        } else {
            // 기본 생성자가 있는 경우
            viewModelKClass.createInstance()
        }
    }

    // Parameter에 해당하는 Instance를 주입
    fun injectInstance(kParameter: KParameter): Any? =
        when (kParameter.type.classifier) {
            ProductRepository::class -> appContainer.productRepository
            CartRepository::class -> appContainer.cartRepository
            else -> null
        }
}

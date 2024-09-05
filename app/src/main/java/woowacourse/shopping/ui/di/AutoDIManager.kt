package woowacourse.shopping.ui.di

import androidx.lifecycle.ViewModel
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.Repository
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object AutoDIManager {
    // 이 주석 부분은 다음 스텝에서 사용할 예정입니다. 처음에는 리포지토리를 싱글톤으로 만들지 않고 구현하려 했는데
    // 시간이 없어서 다음 스텝으로 넘기려고 합니다 ㅠㅠ
//    private val repositories: MutableMap<String, Any> = mutableMapOf()
//
//    init {
//        registerRepository(ProductRepositoryImpl)
//        registerRepository(CartRepositoryImpl)
//    }
//
//    private fun registerRepository(repository: Any) {
//        repositories[repository::class.typeParameters.toString()] = repository
//    }

    private fun <T : Any> createInstanceWithParameters(
        clazz: KClass<T>,
        typeInputs: Map<KClass<*>, Any?>,
    ): T? {
        val constructor = clazz.primaryConstructor ?: return null

        val args =
            constructor.parameters.associateWith { parameter ->
                typeInputs[parameter.type.classifier as? KClass<*>]
            }
        constructor.isAccessible = true
        return constructor.callBy(args)
    }

    fun <T : ViewModel> createViewModelFactory(viewModelClass: KClass<T>): GenericViewModelFactory<ViewModel> {
        // 어떤 생성자 파라미터의 타입에 어떤 인스턴스를 주입할지를 정해주는 부분입니다.
        // 파라미터가 더 생길 경우 이곳에 추가하면 됩니다.
        val args: Map<KClass<*>, Repository> =
            mapOf(
                ProductRepository::class to ProductRepositoryImpl,
                CartRepository::class to CartRepositoryImpl,
            )

        return GenericViewModelFactory(viewModelClass) {
            createInstanceWithParameters(viewModelClass, args) ?: error("Failed to create ViewModel instance")
        }
    }
}

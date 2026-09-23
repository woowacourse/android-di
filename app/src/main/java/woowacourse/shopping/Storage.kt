package woowacourse.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.repository_impl.DefaultCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.repository_impl.FakeCartRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

object Storage {
    val cartRepository: CartRepository = FakeCartRepository() // TODO: DAO 주입
    val productRepository: ProductRepository = ProductRepository()
}

object SamDi {
    fun resolve(modelClass: KClass<*>): Any {
        val constructor = modelClass.primaryConstructor!! // 생성자 확인
        val types = constructor.parameters.map { it.type.classifier as KClass<*> }
        val repos = Storage::class.memberProperties

        val inst =
            types.map { type ->
                val property =
                    repos.single {
                        it.returnType.classifier == type
                    }
//                    resolve(type)
                property.getter.call(Storage)
            }

        return constructor.call(*inst.toTypedArray())
    }

    fun viewModelFactory(): ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T = resolve(modelClass.kotlin) as T
        }
}

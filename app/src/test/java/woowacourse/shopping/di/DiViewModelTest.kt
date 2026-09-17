package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


val productRepository = ProductRepository()
val cartRepository = CartRepository()

class DiViewModel : ViewModel() {
    // 객체 목록 (Key, Value) -> (클래스, 클래스 객체)
    val objectsMap: MutableMap<Any, Any> = mutableMapOf()

    fun hasObject(modelClass: Class<*>): Boolean = objectsMap.keys.contains(modelClass)

    // 객체를 탐색한다.
    fun <T : Any> searchObject(modelClass: Class<T>): Any {
        if (hasObject(modelClass)) {
            return objectsMap[modelClass]!!
        } else {
            val instance = createObject(modelClass)
            objectsMap[modelClass] = instance
            return instance
        }
    }

    // 만약 객체 목록 안에 클래스 key가 존재한다면, 해당 객체를 반환한다.
    // 그게 아니라면, 객체를 만들어서 추가하고 반환한다.
    // 지금 구조에서는 찾기와 객체 생성이 함께 꼬여있다. 이를 분리하면 객체를 잘 탐색하는지, 생성하는지를 알 수 있지 않을까?
    fun <T : Any> createObject(modelClass: Class<T>): Any {
        val constructor = modelClass.kotlin.primaryConstructor!!
        val types = constructor.parameters.map { it.type.classifier as KClass<*> }
        if (types.isEmpty()) { // 파라미터가 없으면 그냥 생성한다.
            return constructor.call()
        } else { // 그게 아니라면 다시 탐색해서 객체를 찾아온다.
            val typesConstructors = types.map { type ->
                searchObject(type.java)
            }
            return constructor.call(*typesConstructors.toTypedArray())
        }
    }

    fun <T : ViewModel> create(modelClass: Class<T>): T {
        // 전달받은 뷰모델의 타입을 런타임시점에 생성한다.
        val constructor = modelClass.kotlin.primaryConstructor!!

        // 해당 뷰모델의 생성자의 파라미터의 클래스 타입을 알아낸다.
        val types = constructor.parameters.map { it.type.classifier as KClass<*> }

        val typesConstructors = types.map { type ->
            type.primaryConstructor!!.call()
        }

        return constructor.call(*typesConstructors.toTypedArray())
    }
}

@RunWith(RobolectricTestRunner::class)
class DiViewModelTest {
    @Test
    fun `ViewModel을 받았을 때 해당 ViewModel이 어떤 클래스인지 알 수 있다`() {
        // given
        val diViewModel = DiViewModel()

        // when
        val productsViewModel = diViewModel.create(ProductsViewModel::class.java)
        val cartViewModel = diViewModel.create(CartViewModel::class.java)

        // then
        assertThat(productsViewModel::class.simpleName).isEqualTo("ProductsViewModel")
        assertThat(cartViewModel::class.simpleName).isEqualTo("CartViewModel")
    }

    @Test
    fun `ViewModel이 알맞은 파라미터 객체를 찾을 수 있다`() {
        // given
        val diViewModel = DiViewModel()

        // when
        diViewModel.objectsMap[ProductRepository::class.java] = ProductRepository()

        // then
        assertThat(diViewModel.hasObject(ProductRepository::class.java)).isTrue()
        assertThat(diViewModel.hasObject(CartRepository::class.java)).isFalse()
    }
}

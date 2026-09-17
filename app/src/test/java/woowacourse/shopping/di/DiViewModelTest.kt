package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

object DiContainer {
    // 객체 목록 (Key, Value) -> (클래스, 클래스 객체)
    val objectsMap: MutableMap<Any, Any> = mutableMapOf()

    fun hasObject(modelClass: Class<*>): Boolean = objectsMap.keys.contains(modelClass)

    // 객체를 탐색한다.
    fun <T : Any> searchObject(modelClass: Class<T>): T {
        if (hasObject(modelClass)) {
            return objectsMap[modelClass] as? T ?: throw IllegalArgumentException("객체를 찾을 수 없습니다.")
        } else {
            val instance = createObject(modelClass)
            objectsMap[modelClass] = instance
            return instance
        }
    }

    // 만약 객체 목록 안에 클래스 key가 존재한다면, 해당 객체를 반환한다.
    // 그게 아니라면, 객체를 만들어서 추가하고 반환한다.
    fun <T : Any> createObject(modelClass: Class<T>): T {
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

    class DiViewModelFactory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return createObject(modelClass)
        }
    }
}

@RunWith(RobolectricTestRunner::class)
class DiViewModelTest {
    @Test
    fun `ViewModel을 받았을 때 해당 ViewModel이 어떤 클래스인지 알 수 있다`() {
        // given
        val diContainer = DiContainer

        // when
        val productsViewModel = diContainer.createObject(ProductsViewModel::class.java)
        val cartViewModel = diContainer.createObject(CartViewModel::class.java)

        // then
        assertThat(productsViewModel).isInstanceOf(ProductsViewModel::class.java)
        assertThat(cartViewModel).isInstanceOf(CartViewModel::class.java)
    }

    @Test
    fun `ViewModel이 알맞은 파라미터 객체를 찾을 수 있다`() {
        // given
        val diContainer = DiContainer

        // when
        diContainer.objectsMap.clear()
        diContainer.objectsMap[ProductRepository::class.java] = ProductRepository()

        // then
        assertThat(diContainer.hasObject(ProductRepository::class.java)).isTrue()
        assertThat(diContainer.hasObject(CartRepository::class.java)).isFalse()
    }

    @Test
    fun `다른 ViewModel을 만들어도 ViewModel 객체를 생성할 수 있다`() {
        // given
        val diContainer = DiContainer

        // when
        diContainer.objectsMap.clear()
        assertThat(diContainer.createObject(TestViewModel::class.java)).isInstanceOf(TestViewModel::class.java)
    }
}

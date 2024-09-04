package woowacourse.shopping.di

import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.ShoppingApplication
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.typeOf
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.MainActivity

class Injector(
    private val appModule: AppModule = ShoppingApplication.appModule,
) {
    fun inject(activity: AppCompatActivity) {
        val properties =
            activity::class.declaredMemberProperties
                .filterIsInstance<KMutableProperty<*>>()
                .filter { it.isLateinit }

        properties.forEach { property ->
            injectProperty(property, activity)
        }
    }

    private fun injectProperty(
        property: KMutableProperty<*>,
        activity: AppCompatActivity,
    ) {
        property.isAccessible = true
        findValueForProperty(property, appModule)?.let { value ->
            property.setter.call(activity, value)
        }
    }

    private fun findValueForProperty(
        property: KMutableProperty<*>,
        appModule: AppModule,
    ): Any? {
        return when (property.returnType) {
            typeOf<CartRepository>() -> appModule.cartRepository
            typeOf<ProductRepository>() -> appModule.productRepository
            else -> null
        }
    }
}

fun main() {


    /** class vs type
     * class:
     * 코드에서 직접 정의되는 구조체로, 객체의 속성과 메서드를 정의합니다.
     * 클래스를 정의함으로써 새로운 타입을 생성합니다.
     * type:
     * 클래스, 인터페이스, 배열, 기본형 등을 포함하는 개념으로, 변수나 표현식이 가질 수 있는 데이터의 형태를 의미합니다.
     * 타입은 클래스의 이름일 수도 있고, 기본 자료형일 수도 있으며, 컴파일러가 변수의 유형을 추론하는 데 사용됩니다.
     */

    /**
     * KClass
     * 여러 데이터 요구 가능
     * open/final 여부, function인지 여부, 등등
     * members : 모든 함수와 프로퍼티
     * declaredMemberProperties : 상속받은 것 포함한 모든 프로퍼티들
     */
    val mainActivityType = MainActivity::class // KType

    /**
     * KType
     * class vs type
     * class : an actual class with optional type arguments
     * -> the type of String : class String
     * nullable or nonnullable
     */
    val type = typeOf<Int>()
    val kTypeOfProductFirstParameterOfFirstConstructor = Product::class.constructors.first().parameters.first().type
    Product::class.createType().classifier

    /**
     * KFunction
     *
     */
    val isOdd1: (Int) -> Boolean = ::isOdd
    val isOdd2: Function<Boolean> = ::isOdd
    val isOdd3: KFunction<Boolean> = ::isOdd // 모든 KFunction은 Function이자 Callable
    val isOdd4: KCallable<Boolean> = ::isOdd

    isOdd3.call(5)
    isOdd4.call(5)

    /**
     * KProperty
     *
     */
    val property: KProperty<String> = Product::name
    property.getter.call(Product("test", 1000, ""))
}

fun isOdd(value: Int): Boolean {
    return true
}

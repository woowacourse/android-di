package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.di.DiContainer
import kotlin.reflect.KClass

/**
 * 생성자 주입은 객체를 만들 때, 필드 주입은 이미 만든 ViewModel을 반환하기 전에 수행한다.
 * 인터페이스의 구현 규칙과 실제 객체의 보관은 별개다. 규칙만으로 DAO 같은 객체를 만들 수는 없다.
 */
object AoDi : ViewModelProvider.Factory {
    private val container = DiContainer()

    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras,
    ): T {
        val vm = container.instantiate(modelClass)
        // lateinit 필드를 사용하기 전에 주입을 마쳐야 한다. 필드 주입에는 이 순서 제약이 있다.
        container.inject(vm)

        return vm
    }

    fun <T : Any> instantiate(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T = container.instantiate(type, qualifier)

    fun <T : Any> register(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        container.register(type, instance, qualifier)
    }

    fun registerInterfaceRule(
        type: KClass<*>,
        implType: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ) {
        container.registerInterfaceRule(type, implType, qualifier)
    }

    fun inject(target: Any) = container.inject(target)
}

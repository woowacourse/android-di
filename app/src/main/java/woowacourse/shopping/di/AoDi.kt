package woowacourse.shopping.di

import android.R.attr.type
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.cast
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

/**
 * 생성자 주입은 객체를 만들 때, 필드 주입은 이미 만든 ViewModel을 반환하기 전에 수행한다.
 * 인터페이스의 구현 규칙과 실제 객체의 보관은 별개다. 규칙만으로 DAO 같은 객체를 만들 수는 없다.
 */
object AoDi : ViewModelProvider.Factory {
    // 요청된 타입을 키로 사용하므로 CartRepository를 요구한 곳에는 같은 객체를 재사용한다.
    private val store = mutableMapOf<KClass<*>, Any>()

    // 인터페이스를 직접 생성할 수 없으므로 요청 타입을 생성 가능한 구현 클래스에 연결한다.
    private val interfaceRule = mutableMapOf<KClass<*>, KClass<*>>()

    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras,
    ): T {
        val vm = instantiate(modelClass)
        // lateinit 필드를 사용하기 전에 주입을 마쳐야 한다. 필드 주입에는 이 순서 제약이 있다.
        inject(vm)

        return vm
    }

    fun <T : Any> instantiate(type: KClass<T>): T {
        // 규칙은 완성된 객체가 아니라 구현 클래스의 KClass를 제공한다.
        val implementationType =
            interfaceRule[type]
                ?: type
        val constructor =
            requireNotNull(implementationType.primaryConstructor) {
                "생성자가 없습니다."
            }

        val dependencies =
            constructor.parameters.map { parameter ->
                // 생성자 파라미터의 타입은 KType이다. 널 가능성 등의 정보와 별개로 classifier에서 KClass를 얻는다.
                val type =
                    parameter.type.classifier as KClass<*>

                // 먼저 등록된 객체를 찾고, 없다면 그 타입의 생성자 의존성까지 재귀적으로 만든다.
                store.getOrPut(type) {
                    instantiate(type)
                }
            }

        // 연결 규칙 때문에 실제 생성 타입과 요청 타입이 다를 수 있어 요청 타입과의 호환성을 검사한다.
        return type.cast(constructor.call(*dependencies.toTypedArray()))
    }

    fun <T : Any> register(
        type: KClass<T>,
        instance: T,
    ) {
        // Room이 만들어 주는 DAO처럼 일반 생성자로 만들 수 없는 객체는 외부에서 전달한다.
        store[type] = instance
    }

    fun registerInterfaceRule(
        type: KClass<*>,
        implType: KClass<*>,
    ) {
        interfaceRule[type] = implType
    }

    fun <T : ViewModel> inject(vm: T) {
        // vm::class는 객체의 KClass다. 어노테이션을 실행 중에 읽어 주입 대상만 선택한다.
        val annotatedProperties =
            vm::class
                .declaredMemberProperties
                .filter { property -> property.annotations.any { it is FieldInject } }

        annotatedProperties.forEach { property ->
            // 생성 후 값을 채워야 하므로 val이 아닌 var의 setter가 필요하다.
            val mutableProperty =
                property as? KMutableProperty1<*, *>
                    ?: error("주입 대상은 var여야 합니다")
            val dependencyType =
                mutableProperty.returnType.classifier as? KClass<*>
                    ?: error("주입 대상의 타입을 확인할 수 없습니다: ${property.name}")

            // 생성자 주입과 같은 보관함 및 연결 규칙을 사용한다. 등록이 없으면 재귀적으로 생성한다.
            val actualDependency =
                store.getOrPut(dependencyType) {
                    instantiate(dependencyType)
                }

            // private 선언은 그대로 두고, 이 리플렉션 객체의 JVM 접근 검사 설정만 바꾼다.
            // 호출 후 자동 복원되지는 않지만 일반 Kotlin 코드의 접근 권한에는 영향을 주지 않는다.
            mutableProperty.isAccessible = true
            mutableProperty.setter.call(vm, actualDependency)
        }
    }
}

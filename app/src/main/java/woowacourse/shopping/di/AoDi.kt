package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object AoDi : ViewModelProvider.Factory {
    private val store = mutableMapOf<KClass<*>, Any>()

    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras,
    ): T = instantiate(modelClass)

    fun <T : Any> instantiate(type: KClass<T>): T {
        val constructor =
            requireNotNull(type.primaryConstructor) {
                "생성자가 없습니다."
            }

        val dependencies =
            constructor.parameters.map { parameter ->
                val type = parameter.type.classifier as KClass<*>

                store.getOrPut(type) {
                    instantiate(type)
                }
            }

        return constructor.call(*dependencies.toTypedArray())
    }
}

/*
*
ViewModel 생성 요청을 받으면:
    1. 요청받은 ViewModel 클래스 정보를 확인한다
    2. 그 클래스의 생성자를 찾는다
    3. 생성자가 요구하는 파라미터 타입들을 읽는다
    4. 각 타입의 객체를 재귀적으로 구한다
    5. 준비된 객체들을 넣어 생성자를 호출한다
    6. 완성된 ViewModel을 반환한다

Repository 객체를 구할 때:
    1. 보관함을 먼저 조회한다
    2. 있으면 기존 객체를 반환한다
    3. 없으면 생성자를 읽어 새 객체를 생성한다
    4. 생성한 객체를 보관함에 넣는다
    5. 객체를 반환한다
* */

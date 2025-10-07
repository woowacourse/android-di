package woowacourse.shopping.di.definition

import woowacourse.shopping.di.AppInjector
import woowacourse.shopping.di.Provider
import kotlin.reflect.KClass

/**
 * 클래스 타입을 어떻게 생성할지에 대한 정보 담는 클래스
 *
 * @param T 생성 대상 타입
 * @property kclass 생성 대상 클래스 타입
 * @property qualifier 객체 구별용 추가 식별자
 * @property kind 생성 방식
 * @property provider 인스턴스를 제공하기 위한 Provider 생성 함수
 */
data class DefinitionInformation<T : Any>(
    val kclass: KClass<T>,
    val qualifier: Qualifier? = null,
    val kind: Kind = Kind.FACTORY,
    val provider: (AppInjector) -> Provider<T>,
)

# 3단계 - Qualifier

## Qualifier의 표현 방식
Qualifier의 역할은 동일한 타입으로 등록된 여러 의존성을 구분하기 위한 식별자입니다.

Qualifier를 표현하는 방식으로는 두 가지의 후보가 존재했습니다.
1. 애노테이션
2. 마커 인터페이스

저는 이 Qualifier를 애노테이션으로 표현하였습니다. 인터페이스 방식은 다음과 같이 마커를 붙여 구현할 수 있습니다.
```kotlin
interface InMemory

class InMemoryCartRepository : CartRepository, InMemory
```

하지만 사용하는 곳에서는 InMemory가 들어가있음을 알 수 없습니다. 반대로 InMemory를 받는 Repository의 타입으로 선언한다면, CartRepository를 알 수 없게 됩니다. 반면, 애노테이션의 장점은 interface에서 못했던 CartRepository를 유지하면서 InMemory로 받는다는 정보까지 전달할 수 있습니다. 즉, 애노테이션과 다르게 interface는 필드만 보고 선택하기 어렵습니다.

또한 인터페이스의 경우 구현에 따라 다양한 구현체를 계속 생성해야 하지만 애노테이션의 경우는 그런 상황이 발생하지 않는다는 이점이 있습니다.

이러한 장점을 보고 애노테이션을 통해 Qualifier를 표현하였습니다.

## di 모듈 형태 선택

순수 JVM 모듈 형태를 선택하였습니다. 현재 제가 구현한 DI 모듈에는 android 코드가 존재하지 않고, 순수 Kotlin으로 동작하도록 구현하였기 때문입니다.

## 기능 요구 사항

### Qualifier
- [x] 상황에 따라 개발자가 Room DB 의존성을 주입받을지, In-Memory 의존성을 주입받을지 선택할 수 있다.
- [x] 내가 만든 DI 라이브러리가 Qualifier를 제대로 해석하는지 테스트를 작성한다. Qualifier가 없을 때 예외가 나는 것까지 검증한다.

### 모듈 분리
- [x] 내가 만든 DI 라이브러리를 모듈로 분리한다.

## 선택 요구 사항
- [ ] DSL을 활용한다.
- [ ] 내가 만든 DI 라이브러리를 배포하고 적용한다.

## 프로그래밍 요구 사항
- [x] 같은 타입의 구현체가 둘 등록되어 있고 Qualifier가 없으면 명확한 오류를 낸다. 임의로 하나를 고르지 않는다.
- [x] 분리한 DI 모듈은 쇼핑 앱의 도메인 타입(CartRepository, Product 등)을 알지 못해야 한다.
- [x] Qualifier를 무엇으로 표현했는지(애노테이션 / 문자열 키)와 :di 모듈을 안드로이드 모듈·순수 JVM 모듈 중 무엇으로 만들었는지, 그 선택의 근거를 페어와 정해 README.md에 남긴다. 모듈 형태의 선택은 4단계의 화면 스코프 구현에 영향을 준다.

## 리뷰 체크리스트
- [x] Room DB 구현체와 In-Memory 구현체를 개발자가 선택해서 주입받을 수 있다.
- [x] Qualifier 없이 모호한 상황이 되면 명확한 예외 메시지가 나온다.
- [x] DI 라이브러리가 별도 모듈로 분리되어 있다.
- [x] :di 모듈이 앱의 도메인 타입에 의존하지 않는다.
- [x] Qualifier 동작에 대한 테스트가 있다.
- [x] Qualifier 표현 방식과 :di 모듈 형태(안드로이드 / 순수 JVM)의 선택 근거가 README.md에 있다.

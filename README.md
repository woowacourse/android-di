# android-di

## 기능 목록

- 리플렉션으로 ViewModel 생성자의 의존성 타입을 확인한다.
- 생성자에 필요한 의존성을 재귀적으로 생성하고 주입한다.
- 생성한 객체를 보관하고 같은 타입이 요청되면 기존 인스턴스를 재사용한다.
- 하나의 `ViewModelProvider.Factory`로 여러 ViewModel을 생성한다.
- Compose의 `viewModel()`에 공용 팩토리를 전달해 ViewModel을 생성한다.

### 2단계 - 필드 주입

- 이미 생성된 ViewModel에서 `@FieldInject`가 붙은 프로퍼티를 찾는다.
- 해당 프로퍼티에 의존성을 주입하고, 애노테이션이 없는 프로퍼티는 변경하지 않는다.
- 필드에 필요한 객체가 등록되지 않았다면 인터페이스 연결 규칙과 생성자 의존성을 따라 생성한다.
- ViewModel의 비공개 필드에도 의존성을 주입한다.

### 3단계 - Qualifier

- DI 코어를 앱 도메인 타입을 모르는 순수 JVM `:di` 모듈로 분리한다.
- Android의 `ViewModelProvider.Factory` 연동은 `:app`에 유지한다.
- 동일 인터페이스의 여러 구현체를 Qualifier로 구분해 등록하고 선택한다.
- 구현체가 여러 개인데 Qualifier가 없으면 요청 타입과 후보를 알 수 있는 오류를 낸다.

### 4단계 - Lifecycle

- `ScopeType`으로 앱, ViewModel, 화면이라는 수명 종류를 표현한다.
- `ScopeContext`가 수명 종류를 현재 요청의 실제 `scopeId`에 연결한다.
- `DiKey`의 정확한 Qualifier 규칙을 먼저 찾고, 없으면 Qualifier가 없는 타입 공통 규칙을 사용한다.
- 스코프 문맥을 사용하는 요청에 규칙이 없거나 실제 `scopeId`가 없으면 오류로 처리한다.
- 외부 객체 등록, 생성자 주입, 필드 주입이 같은 `ScopeContext`로 보관함을 선택한다.
- 앱 스코프에는 `Context`, DAO, `CartRepository`를 보관한다.
- ViewModel마다 별도 스코프를 열어 `ProductRepository`를 보관하고 ViewModel 정리 시 함께 닫는다.
- `NavBackStackEntry`마다 화면 스코프를 열어 `DateFormatter`를 보관하고 엔트리의 `ViewModelStore`가 정리될 때 함께 닫는다.
- 같은 스코프의 객체 재사용, 서로 다른 스코프의 객체 분리, 종료 후 참조 제거와 재생성을 테스트한다.
- 스코프 종류가 늘어나도 컨테이너에 종류별 `when` 분기를 추가하지 않는다.

순수 JVM 모듈을 선택한 이유는 객체 생성과 주입 규칙이 Android 생명주기와 무관하기 때문이다.
`:app`만 `:di`에 의존하게 하여 DI 코어가 `CartRepository`, `Product` 같은 쇼핑 앱 타입을 알지 않게 한다.
Qualifier는 문자열 대신 애노테이션 타입으로 표현한다. 문자열 오타를 피하고 생성자와 필드에 선택 의도를 직접 표시하기 위해서다.
앱 타입과 생성 규칙을 아는 등록은 `ShoppingApplication`이 담당하고, `:di`는 애노테이션 타입을 키로만 보관한다.
존재하지 않는 애노테이션 타입 참조와 요청 타입에 속하지 않는 구현체 등록은 컴파일할 때 발견된다.
Qualifier 등록 누락과 Qualifier가 없는 모호한 요청은 저장소 내용이 정해지는 실행 중에 발견된다.

## 설계 결론

### DI와 서비스 로케이터

#### 판단 근거

- `CartScreen`과 `ProductsScreen`은 필요한 ViewModel과 `DateFormatter`를 매개변수로 받으며 컨테이너를 직접 조회하지 않는다.
- `ShoppingNavHost`도 `ViewModelProvider.Factory`와 `CartScreenScopeFactory`만 받아 화면 의존성을 조립한다.
- `MainActivity`와 `ShoppingApplication`이 객체 그래프의 조립 지점 역할을 맡고, 소비자는 객체를 요청하는 방법이 아니라 사용할 객체만 안다.
- 반면 `AoDi`는 임의 타입을 조회하고 등록할 수 있는 `instantiate()`, `register()`, `inject()`를 공개한다. 이 API를 소비자가 직접 호출하면 서비스 로케이터 방식으로 되돌아갈 수 있다.
- `CartViewModel`의 필드 주입은 소비자가 컨테이너를 조회하지는 않지만, 생성 직후에는 의존성이 채워지지 않은 불완전한 상태가 존재하고 의존성이 생성자에 드러나지 않는다는 한계가 있다.

#### 결론

현재 구조는 소비자 밖의 조립 지점에서 의존성을 만들어 전달하므로 DI를 추구하며 DI에 가깝다. 다만 범용 `AoDi` API와 필드 주입에는 서비스 로케이터로 사용될 여지와 숨은 의존성이라는 흔적이 남아 있다.

### DIP와 IoC

#### 판단 근거

- IoC는 객체가 자신의 생성과 생명주기를 직접 통제하지 않고 외부 프레임워크나 조립 코드에 제어를 맡기는 원칙이다. 이 프로젝트에서는 Android의 `ViewModelProvider`와 `AoDi`가 ViewModel을 만들고, `ViewModelStore`의 정리 시점에 DI 스코프도 닫는다.
- DI는 IoC를 구현하는 방법 중 하나다. 객체가 필요한 의존성을 외부에서 받게 하여 객체 생성에 대한 제어를 밖으로 옮긴다.
- DIP는 상위 정책과 하위 구현이 구체 클래스가 아니라 추상화에 의존하도록 경계를 세우는 원칙이다. `ProductsViewModel`과 `CartViewModel`은 `DefaultCartRepository`가 아니라 `CartRepository`에 의존한다.
- 화면 계층도 구체적인 `AoDi` 대신 `ViewModelProvider.Factory`와 `CartScreenScopeFactory`에 의존한다.
- `ProductRepository`와 `DateFormatter`처럼 대체 가능성이 낮은 모든 타입까지 인터페이스로 만들지는 않았다. 구체 타입 의존 자체가 곧 DIP 위반은 아니며, 정책과 구현을 분리해야 하는 경계에서 추상화했는지가 판단 기준이다.

#### 결론

IoC는 객체 생성과 생명주기의 제어를 Android와 `AoDi`에 넘긴 형태로 적용되어 있다. DIP는 `CartRepository`와 화면 팩토리 같은 주요 경계에 적용했지만 모든 의존성을 추상화한 구조는 아니다.

### 리플렉션과 KSP

#### 판단 근거

[Kotlin reflection](https://kotlinlang.org/docs/reflection.html)은 실행 중에 클래스의 생성자와 프로퍼티를 조사한다. 현재 컨테이너는 `primaryConstructor`, 생성자 매개변수, `declaredMemberProperties`, 애노테이션을 읽고 `call()`로 생성과 주입을 수행한다. 구현이 단순하고 실행 시점에 주어진 타입을 처리하기 쉽지만, 누락된 바인딩과 잘못된 객체 그래프가 해당 경로를 실행할 때 발견된다.

[KSP](https://kotlinlang.org/docs/ksp-overview.html)는 컴파일 중 Kotlin 선언과 타입을 분석해 소스 코드를 생성하는 도구다. KSP로 바꾸려면 다음 정보를 빌드 시점에 수집해야 한다.

- 주입 가능한 타입과 사용할 생성자
- 생성자 매개변수의 타입, 순서, Qualifier
- 인터페이스와 구현체의 바인딩
- 타입별 `ScopeType` 규칙
- 필드 주입 대상과 접근 가능 여부
- `Context`와 DAO처럼 외부에서 제공해야 하는 객체의 진입점

이 정보로 팩토리와 직접 호출 코드를 생성하면 다수의 누락과 모호성을 빌드 중 진단할 수 있다. 다만 실행 중 전달되는 `scopeId`나 외부 인스턴스의 실제 존재까지 모두 컴파일 시점에 검증할 수 있는 것은 아니다. KSP는 기존 소스 코드를 수정하지 않으므로 비공개 필드에 직접 접근하는 현재 주입 방식은 생성자 주입이나 접근 가능한 주입 지점으로 바꿔야 한다.

| 기준 | 리플렉션 | KSP 코드 생성 |
| --- | --- | --- |
| 런타임 유연성 | 실행 시점의 타입과 애노테이션을 조사하기 쉽다. | 빌드 때 생성한 그래프와 진입점을 사용한다. |
| 오류 발견 시점 | 해당 객체 생성 경로를 실행할 때 발견한다. | 생성 가능한 그래프의 많은 오류를 빌드 중 발견한다. |
| R8 | 간접 참조를 R8이 추론하지 못할 수 있어 [keep 규칙](https://developer.android.com/topic/performance/app-optimization/keep-rules-overview)이 필요할 수 있다. 현재 프로젝트는 release 축소가 꺼져 있어 실제 호환성은 검증하지 않았다. | 직접 참조가 생성되므로 리플렉션 대상 보존 위험이 줄어든다. |
| 실행 성능 | 타입 탐색과 동적 호출 비용이 객체 생성 시 발생한다. | 생성된 직접 호출을 사용해 런타임 탐색을 줄인다. 이 프로젝트에서 차이는 측정하지 않았다. |
| 구현과 빌드 비용 | 학습용 구현이 짧고 변경을 바로 실험하기 쉽다. | 프로세서, 생성 코드, 오류 보고, 증분 빌드 대응이 추가된다. |
| KMP 비용 | 현재 `kotlin.reflect.jvm.isAccessible` 사용은 JVM에 묶여 있다. | [대상과 컴파일별 KSP 설정](https://kotlinlang.org/docs/ksp-multiplatform.html)과 생성 코드의 소스 세트 배치, 각 타깃 검증이 필요하다. |

#### 결론

리플렉션 방식은 구조가 단순하고 유연해 DI 원리를 학습하는 현재 목적에 적합하다. 그러나 오류 발견이 런타임으로 늦어지고, 실행 중 탐색 비용과 R8 보존 규칙 위험이 있다. KSP는 생성자, Qualifier, 바인딩, 스코프, 주입 지점을 빌드 때 분석해 직접 호출 코드를 생성함으로써 많은 오류를 앞당기고 런타임 탐색과 R8 위험을 줄인다. 대신 프로세서 구현과 빌드 구성, KMP 타깃별 설정과 검증 비용이 늘어난다. 이 라이브러리를 학습 범위를 넘어 운영 환경으로 확장한다면 KSP 방식이 더 적합하다.

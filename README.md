# android-di
## 0.5단계
---
- [x] `MainActivityTest`를 수동으로 주입시켜 테스트를 통과시킨다.
- [x] DI 컨테이너를 만들지 않는다
- [x] `viewModel()`을 호출하는 자리에 `ViewModelProvider.Factory`를 직접 넘겨서 repository를 넘겨준다.

## 1단계
---
### 기능 요구 사항
- [x] 생성자 주입을 통해 자동으로 DI를 한다
  - [x] ViewModel에 수동으로 주입되고 있는 의존성을 자동으로 주입하도록 한다
  - [x] 특정 ViewModel에서만이 아닌, 범용적으로 활용될 수 있는 자동 주입 로직을 작성한다.
  - [x] `ProductsViewModel`, `CartViewModel` 모두 하나의 로직을 참조하는지 확인한다

## 체크리스트
---
- [x] 사전에 주어진 테스트 코드가 모두 성공해야 한다.
- [x] Annotation을 지금 활용하지 않는다.

## 2단계
---
- [x] 재귀적으로 다른 객체들을 참조하도록 변경
  - [x] `CartRepository` 인터페이스화 
  - [x] `CartRepository`가 Dao를 참조하도록 변경
  - [x] `Product`에 식별자 추가
  - [x] Repository 에 suspend 추가
  - [x] 담은 시각을 표시한다. (`createAt`이 언제 담겼는지 들고 있다. 날짜 포맷팅은 `DateFormatter`가 담당한다.)
- [x] 의존성 주입이 필요한 필드와 그렇지 않은 필드 구분
  - [x] Annotation을 붙여서 필요한 요소에만 의존성 주입
  - [x] 내가 만든 의존성 라이브러리가 제대로 작동하는지 테스트 코드 작성
    - [x] 애노테이션이 붙은 ViewModel 필드만 주입되는지 확인
    - [x] CartRepository의 DAO 의존성이 재귀적으로 해결되는지 확인


### 프로그래밍 요구사항
---
- [x] 테스트 코드가 모두 통과한다
- [x] 필드 주입 대상은 어노테이션으로 명시된 필드만 한다. 모든 필드를 훑어 주입하지 않는다

## 3단계
---

### 기능 요구사항
---
- [x] Qualifier로 Room과 InMemory중 지정한 구현체 주입
- [x] 같은 타입의 후보가 둘 이상인데 Qualifier가 없다면 예외가 발생한다
- [x] Qualifier 선택과 모호한 요청에 대한 테스트 작성
- [x] di 모듈 분리

### 설계 선택
---

#### Qualifier 표현 방식

Qualifier는 어노테이션 타입으로 표현한다. 
문자열 키보다 오타를 컴파일 시점에 발견하기 쉽고, 의존성을 요청하는 생성자 매개변수에서 어떤 구현체를 선택하는지 명시적으로 드러낼 수 있다.
`@RoomRepo`와 `@InMemoryRepo`에는 DI 모듈의 `@Qualifier`를 붙여 Qualifier임을 표시한다.

#### DI 모듈 형태

`:di`는 순수 JVM 모듈로 구성했다. 의존성 등록과 재귀적 생성자 주입, Qualifier 해석, 필드 주입은 Android API 없이 동작할 수 있지 않을까? 라고 생각했다.
쇼핑 앱의 도메인 타입과 분리하게 되면 재사용이 쉬울 것이라고 생각했다.
Step2에선 Context를 넘겨주도록 구현했는데, 모듈을 분리하면서 안드로이드 의존성 없이 구현을 가능하게 했다.

### 프로그래밍 요구사항
---

- [x] 같은 타입의 구현체가 둘 이상이고 Qualifier가 없으면 명확한 예외를 낸다
- [x] `:di` 모듈은 쇼핑 앱의 도메인 타입에 의존하지 않는다

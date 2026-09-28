# android-di

## 기능 목록

- 리플렉션으로 ViewModel 생성자의 의존성 타입을 확인한다.
- 생성자에 필요한 의존성을 재귀적으로 생성하고 주입한다.
- 생성한 의존성을 보관하고 같은 타입이 요청되면 기존 인스턴스를 재사용한다.
- 하나의 `ViewModelProvider.Factory`로 여러 ViewModel을 생성한다.
- Compose의 `viewModel()`에 공용 팩토리를 전달해 ViewModel을 생성한다.

### 2단계 - 필드 주입 (진행 중)

- 이미 생성된 ViewModel에서 `@FieldInject`가 붙은 프로퍼티를 찾는다.
- 해당 프로퍼티에 의존성을 주입하고, 애노테이션이 없는 프로퍼티는 변경하지 않는다.
- 필드에 필요한 객체가 등록되지 않았다면 인터페이스 연결 규칙과 생성자 의존성을 따라 생성한다.
- ViewModel의 비공개 필드에도 의존성을 주입한다.

### 3단계 - Qualifier (진행 중)

- DI 코어를 앱 도메인 타입을 모르는 순수 JVM `:di` 모듈로 분리한다.
- Android의 `ViewModelProvider.Factory` 연동은 `:app`에 유지한다.
- 동일 인터페이스의 여러 구현체를 Qualifier로 구분해 등록하고 선택한다.
- 구현체가 여러 개인데 Qualifier가 없으면 요청 타입과 후보를 알 수 있는 오류를 낸다.

순수 JVM 모듈을 선택한 이유는 객체 생성과 주입 규칙이 Android 생명주기와 무관하기 때문이다.
`:app`만 `:di`에 의존하게 하여 DI 코어가 `CartRepository`, `Product` 같은 쇼핑 앱 타입을 알지 않게 한다.

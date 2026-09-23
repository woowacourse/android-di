# android-di

## 기능 목록

- 리플렉션으로 ViewModel 생성자의 의존성 타입을 확인한다.
- 생성자에 필요한 의존성을 재귀적으로 생성하고 주입한다.
- 생성한 의존성을 보관하고 같은 타입이 요청되면 기존 인스턴스를 재사용한다.
- 하나의 `ViewModelProvider.Factory`로 여러 ViewModel을 생성한다.
- Compose의 `viewModel()`에 공용 팩토리를 전달해 ViewModel을 생성한다.

### 2단계 - 필드 주입 (구현 예정)

- 이미 생성된 ViewModel에서 `@FieldInject`가 붙은 프로퍼티를 찾는다.
- 해당 프로퍼티에 의존성을 주입하고, 애노테이션이 없는 프로퍼티는 변경하지 않는다.

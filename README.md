# android-di

## 2단계 · 필드 주입과 재귀 주입

### 구현할 기능

**장바구니 데이터**

- `id`와 `createdAt`을 가진 `CartProduct` 도메인 모델을 만든다.
- `CartProductEntity`를 도메인으로 변환하는 `toDomain()`을 추가한다.
- `CartRepository`를 인터페이스와 DAO 기반 구현체로 분리한다.
- DAO를 호출하는 Repository 함수를 `suspend`로 변경한다.
- 상품 삭제에는 목록 인덱스가 아닌 실제 `id: Long`을 사용한다.

**의존성 주입**

- `@KirbyInject`가 붙은 프로퍼티에만 필드 주입을 수행한다.
- 어노테이션은 `PROPERTY` 대상에 `RUNTIME`으로 보존한다.
- 생성자 의존성을 재귀적으로 해결한다.
- 순환 의존성이 있으면 명확한 오류를 낸다.
- 두 화면의 ViewModel을 공용 컨테이너로 생성한다.

**화면**

- Repository의 `suspend` 함수를 `viewModelScope`에서 호출한다.
- 장바구니에 상품명과 담은 시각을 표시한다.
- 담은 시각은 `DateFormatter`로 포맷한다.
- `LazyColumn` 항목은 상품 `id`를 key로 사용한다.

### 설계 선택

**필드 주입을 사용한 이유**

Activity·Fragment처럼 OS가 직접 만드는 객체는 생성자 인자를
전달하기 어려워 필드 주입이 필요할 수 있다.
ViewModel은 Factory를 통한 생성자 주입이 가능하다.
이 단계에서는 필드 주입을 학습하지만, 가능한 곳에서는 생성자 주입을 우선한다.

**어디까지 자동으로 생성하는가?**

컨테이너는 일반 객체의 생성자를 따라 의존성을 해결한다.

```text
CartViewModel
  → CartRepository
  → CartProductDao
```

`CartRepository` 요청은 등록된 `DefaultCartRepository`로 연결한다.
그 생성자가 요구하는 `CartProductDao`는 등록된 인스턴스를 사용한다.

**Room은 왜 수동으로 준비하는가?**

`ShoppingDatabase`는 추상 `RoomDatabase`다.
현재 컨테이너의 생성자 리플렉션만으로 만들 수 없다.

- `ShoppingApplication`이 `Room.databaseBuilder(...)`로 DB를 만든다.
- DB에서 `CartProductDao`를 얻어 컨테이너에 등록한다.
- DB 자체는 컨테이너에 등록하지 않는다.

프레임워크 생성 API나 외부 설정이 필요한 객체는 앱에서 준비한다.
그 객체에 의존하는 일반 객체는 컨테이너가 생성한다.

**순환 의존성은 어떻게 처리하는가?**

컨테이너는 현재 생성 중인 타입을 추적한다.
`A → B → A`처럼 같은 타입을 다시 만나면 무한 재귀하지 않는다.

`순환 의존성이 발견되었습니다`라는 오류를 낸다.
성공하거나 실패해도 생성 경로에서 해당 타입을 제거한다.

### 검증

- 어노테이션이 없는 프로퍼티에는 주입하지 않는다.
- `CartRepository`의 DAO 의존성을 재귀적으로 해결한다.
- 순환 의존성 테스트가 통과한다.
- 목록 중간 항목을 삭제해도 누른 상품이 지워진다.
- UI 계층은 `CartProductEntity`를 직접 참조하지 않는다.
- 사전 제공 테스트가 통과한다.

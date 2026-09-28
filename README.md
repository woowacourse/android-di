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

## 3단계 · Qualifier와 모듈 분리

### 구현할 기능

**Qualifier**

- Room·In-Memory `CartRepository`를 각각 등록한다.
- 주입 지점의 Qualifier로 구현체를 선택한다.
- 등록과 캐시의 키를 `(요청 타입, Qualifier)`로 구분한다.
- 후보가 하나면 Qualifier 없이도 선택한다.
- 후보가 둘 이상인데 Qualifier가 없으면 오류를 낸다.
- 같은 키의 중복 등록과 이미 해결한 타입의 뒤늦은 등록도 거부한다.
- 두 ViewModel은 Room 구현체를 사용하도록 표시한다.

**모듈 분리**

- DI 코어와 코어 테스트를 순수 JVM `:di` 모듈로 옮긴다.
- `:app`은 `:di`를 의존한다.
- ViewModel Factory와 Compose 연결은 `:app`에 남긴다.
- Room 생성과 쇼핑 타입 등록도 `:app`에 남긴다.

### 설계 선택

**Qualifier는 어노테이션 타입으로 표현한다.**

문자열보다 이름 오류를 컴파일 단계에서 발견하기 쉽다.
대신 런타임 Retention과 Kotlin 리플렉션의 탐색 위치를 맞춰야 한다.

**`:di`는 순수 JVM 모듈로 만든다.**

DI 코어에는 Android나 쇼핑 앱 타입이 필요하지 않다.
Android 생명주기와 화면 연결은 `:app`이 담당한다.

**등록된 바인딩을 조용히 덮어쓰지 않는다.**

이미 주입한 객체는 등록을 바꿔도 교체되지 않는다.
같은 키의 재등록은 오류로 처리한다.
실행 중 구현체 전환이 필요하다면 별도 선택자나 스코프를 설계한다.

### 검증

- 두 Qualifier가 서로 다른 구현체를 선택한다.
- Qualifier 없는 모호한 요청은 후보를 알려주는 오류를 낸다.
- In-Memory 저장소는 ID 기반 삭제를 수행한다.
- `:di`의 소스와 빌드 설정에 쇼핑 도메인 의존성이 없다.
- `:di` 테스트와 앱의 기존 테스트가 통과한다.

등록 DSL과 라이브러리 배포·적용은 선택 요구 사항으로 남긴다.

# 🛠️ android-di

## 📚️ 개요
- 의존성 주입(DI)을 직접 구현하며, 수동 → 자동 주입으로 확장한다.
- ViewModel과 Repository 간 결합을 줄이고 테스트 용이성을 높인다.

---

## 🧱 기능 요구 사항 (0.5단계)

- **생성자 주입 – 수동**
    - 테스트하기 어렵다.
    - Repository 교체 시 ViewModel에 직접 변경이 필요하다.

## 🛠️ 구현할 기능 (0.5단계)

- [x] ViewModel에서 Repository를 수동으로 생성자 주입한다.
- [x] Repository 교체 시 ViewModel 코드 변경 필요성을 확인한다.

---

## 🧱 기능 요구 사항 (1단계)

- **생성자 주입 – 자동**
    - ViewModel에서 참조하는 Repository가 정상적으로 주입되지 않는 문제 해결.
    - 새로운 객체가 Repository를 참조하면 매번 주입 코드를 작성해야 하는 문제 해결.
    - ViewModel 의존성 주입을 범용적인 자동 주입 로직으로 대체.
        - MainViewModel, CartViewModel 모두 하나의 로직만 참조.
        - 100개의 ViewModel이 생겨도 자동 주입 로직은 하나만 존재해야 한다.
    - CartRepository를 매번 새로 생성하지 않고 최초 한 번만 인스턴스화한다.

## 🛠️ 구현할 기능 (1단계)

- [x] 자동 주입 로직을 구현한다.
- [x] MainViewModel, CartViewModel에서 동일한 로직을 통해 Repository가 주입되도록 한다.
- [x] 범용적으로 확장 가능한 ViewModel 자동 주입 구조를 만든다.
- [x] CartRepository를 싱글 오브젝트로 관리한다.

---

## 🧱 선택 요구 사항 (1단계)

- [ ] TDD로 DI 구현
- [x] Robolectric 기능 테스트
- [x] ViewModel 테스트
- [x] 도메인 로직, Repository 단위 테스트

---

## 🛠️ 프로그래밍 요구 사항 (1단계)

- [x] 제공된 테스트 코드 모두 성공해야 한다.
- [x] Annotation은 사용하지 않는다.

---

## 🧱 기능 요구 사항 (2단계)

- **필드 주입 – Annotation**
    - 의존성이 필요한 필드와 그렇지 않은 필드를 구분할 수 없다.
    - Annotation을 붙여 필요한 필드에만 의존성을 주입하도록 한다.
    - 필드 주입이 정상적으로 작동하는지 테스트 코드를 작성한다.

- **Recursive DI**
    - CartRepository가 DAO 객체를 참조하도록 변경한다.
    - DAO, Entity를 통해 장바구니 생성 시간을 저장하고 화면에 표시한다.

## 🛠️ 구현할 기능 (2단계)

- [x] @InjectField(가칭) Annotation 정의
- [x] ViewModel 내 필드 주입 로직 구현
- [x] 의존성 주입 라이브러리의 필드 주입 기능 테스트
- [x] CartRepository 내부에 CartProductDao 의존성 추가
- [x] CartProductEntity의 createdAt을 UI(ViewHolder)에 표시
- [x] position 기반 삭제 로직을 id 기반으로 개선

---

## 🛠️ 프로그래밍 요구 사항 (2단계)

- [x] 사전에 주어진 테스트 코드 모두 성공해야 한다.
- [x] Annotation을 활용해 필드 주입을 구현한다.

---

## 🧱 기능 요구 사항 (3단계)

- **Qualifier**
  - 하나의 인터페이스에 여러 구현체가 등록된 경우, 어떤 구현체를 사용할지 명시할 수 있어야 한다.
  - 개발자가 상황에 따라 Room 기반 Repository 또는 In-Memory 기반 Repository를 선택할 수 있어야 한다.
  - `@Qualifier` 어노테이션을 만들어 DI 컨테이너가 올바른 의존성을 주입할 수 있도록 한다.

- **모듈 분리**
  - 직접 구현한 DI 라이브러리를 별도의 모듈로 분리한다.
  - 다른 프로젝트에서도 재사용 가능하도록 구조를 개선한다.
  - DI 모듈은 `core-di` 혹은 `library-di` 형태로 구성할 수 있다.

---

## 🛠️ 구현할 기능 (3단계)

- [ ] `@Qualifier` 어노테이션 정의 (예: `@InMemory`, `@Database`)
- [ ] `Container` 또는 `AppContainer`에서 동일 인터페이스의 다중 구현체를 구분 주입하도록 변경
- [ ] Repository 구현체를 `InMemoryCartRepository`, `RoomCartRepository`로 분리
- [ ] AppContainer에서 사용할 구현체를 선택적으로 주입
- [x] DI 라이브러리를 독립 모듈로 분리 (`core-di` 등)
- [ ] 선택: DSL 기반 의존성 등록 문법 추가 (예: `container.bind<T> { ... }`)
- [ ] 선택: Jitpack을 활용해 라이브러리 배포 및 적용 테스트

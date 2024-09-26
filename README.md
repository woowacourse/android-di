# android-di 👻

---

## 0.5단계: 생성자 주입 - 수동

### 기능 요구 사항 🛠️

- 테스트하기 어려운 코드를 테스트하기 쉬운 코드로 변경하기 위해 생성자 주입을 사용한다.
- Repository 객체를 교체하기 위해 또다른 객체를 만들어 바꾸지 않는다. 즉, ViewModel에 직접적인 변경 사항 발생을 막는다.

---

## 1단계: 생성자 주입 - 자동

### 기능 요구 사항 🛠️

- ViewModel에서 참조하는 Repository가 정상적으로 주입되어야 한다.
- Repository를 참조하는 다른 객체가 생기면 주입 코드를 매번 만들지 않도록 구현한다.
    - ViewModel에 수동으로 주입되고 있는 의존성들을 자동으로 주입되도록 바꿔본다.
    - 특정 ViewModel에서만이 아닌, 범용적으로 활용될 수 있는 자동 주입 로직을 작성한다. (MainViewModel, CartViewModel 모두 하나의 로직만
      참조한다)
    - 100개의 ViewModel이 생긴다고 가정했을 때, 자동 주입 로직 100개가 생기는 것이 아니다. 하나의 자동 주입 로직을 재사용할 수 있어야 한다.
- 장바구니에 접근할 때마다 매번 CartRepository 인스턴스를 새로 만들지 않고, 한 번만 만들어서 재사용한다.
    - 여러 번 인스턴스화할 필요 없는 객체는 최초 한 번만 인스턴스화한다. (단순히 싱글 오브젝트로도 구현 가능)

### 선택 요구 사항 📦

- TDD로 DI 구현하기
- Robolectric을 사용하여 빠르게 안드로이드 테스트하기
- ViewModel 테스트
- 모든 도메인 로직, Repository 단위 테스트

### 프로그래밍 요구 사항 👩🏻‍💻

- 사전에 주어진 테스트 코드가 모두 성공해야 한다.
- Annotation은 이 단계에서 활용하지 않는다.

---

## 2단계: Annotation을 활용한 자동 주입

### 기능 요구 사항 🛠️

- 필드 주입
    - ViewModel 내 필드 주입을 구현한다.
- Annotation을 활용하여 의존성 주입을 구현한다.
    - 의존성 주입이 필요한 필드와 그렇지 않은 필드를 구분할 수 있다.
    - Annotation을 활용하여 의존성 주입이 필요한 필드에만 의존성을 주입한다.
    - 내가 만든 의존성 라이브러리가 제대로 작동하는지 테스트 코드를 작성한다.
- Recursive DI
    - CartRepository가 DAO 객체를 참조하도록 변경한다.
    - CartProductEntity에는 `createdAt` 프로퍼티가 있어서 언제 장바구니에 상품이 담겼는지를 알 수 있다.
    - CartProductViewHolder의 `bind` 함수에 다음 구문을 추가하여 뷰에서도 날짜 정보를 확인할 수 있도록 한다.

### 선택 요구 사항 📦

- 현재는 장바구니 아이템 삭제 버튼을 누르면 RecyclerView의 position에 해당하는 상품이 지워진다.
    - 상품의 position과 `CartRepository::deleteCartProduct`의 id가 동일한 값임을 보장할 수 없다는 문제를 해결한다.
- 뷰에서 `CartProductEntity`를 직접 참조하지 않는다.

### 프로그래밍 요구 사항 👩🏻‍💻

- 사전에 주어진 테스트 코드가 모두 성공해야 한다.

---

## 3단계: Qualifier

### 기능 요구 사항 🛠️

- Qualifier를 활용하여 의존성 주입을 구현한다.
    - 하나의 인터페이스의 여러 구현체가 DI 컨테이너에 등록된 경우, 어떤 의존성을 가져와야 할지 알 수 있게 한다.
        - 상황에 따라 개발자가 Room DB 의존성을 주입받을지, In-Memory 의존성을 주입받을지 선택할 수 있다.
- 모듈 분리
    - DI 라이브러리를 모듈로 분리한다.

### 선택 요구 사항 📦

- DSL을 활용한다.
- 내가 만든 DI 라이브러리를 배포하고 적용한다.

---

## 4단계: LifeCycle

### 기능 요구 사항 🛠️

- CartActivity에서 사용하는 DateFormatter의 인스턴스를 매번 개발자가 관리해야 하는 문제를 해결한다.
- 모든 의존성이 싱글 오브젝트로 만들어질 필요 없다.
    - CartRepository는 앱 전체 LifeCycle 동안 유지되도록 구현한다.
    - ProductRepository는 ViewModel LifeCycle 동안 유지되도록 구현한다.
    - DateFormatter는 Activity LifeCycle 동안 유지되도록 구현한다.
- 내가 만든 DI 라이브러리가 잘 작동하는지 테스트를 작성한다.

### 선택 요구 사항 📦

- DateFormatter가 Configuration Changes에도 살아남을 수 있도록 구현한다.
- Activity, ViewModel 외에도 다양한 컴포넌트(Fragment, Service 등)별 유지될 의존성을 관리한다.

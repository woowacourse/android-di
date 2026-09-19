# android-di

# 0.5단계 - 생성자 주입 - 수동

## 기능 요구 사항

의도적으로 실패하는 테스트(MainActivityTest)를 수동 주입으로 통과시킨다. DI 컨테이너를 만들지 않는다. 컴포저블이 viewModel()을 호출하는 자리에 ViewModelProvider.Factory를 직접 넘겨, 그 화면이 필요로 하는 Repository를 손으로 만들어 넣어주면 된다.

```text
viewModel: ProductsViewModel = viewModel(factory = /* 여기서 직접 만들어 넣는다 */)
```

이렇게 만들어 두고 다음 문제점을 확인한다. 1단계는 여기서 출발한다.

- 화면이 늘어날 때마다 팩토리를 하나씩 새로 쓴다.
- Repository 객체를 교체하기 위해 또다른 객체를 만들어 바꿔줘야 한다. 즉, ViewModel에 직접적인 변경사항이 발생한다. 

0.5단계는 1단계의 출발점이다. OT에서는 수동 DI 구현을 먼저 PR로 리뷰받는다. 진행 순서와 오늘의 리뷰 기준은 OT 문서를 따른다.

## 구현한 목록

### 1. viewModel을 수동으로 주입하여 테스트를 통과하도록 구현함
`MainActivityTest.kt` 코드는 MainActivity의 생명주기를 진행시키고 테스트가 잘 생성되는지를 검증하는 코드입니다.

`.setup()`은 onCreate -> onStart -> onResume의 단계를 진행시킵니다.

`.get()`은 Activity의 인스턴스를 반환하는 것입니다.

결과적으로 isNotNull()을 통해 MainActivity가 잘 생성되었는지 확인합니다.

테스트가 실패한 이유는 MainActivity안의 컴포저블 함수를 생성하는 과정에서 필요한 ViewModel의 의존성을 주입받지 못했기 때문입니다.

그래서 ViewModelProvider.Factory를 사용해 컴포저블 함수의 ViewModel()이 필요한 의존성을 전달하도한 목록 구현하였습니다.

이렇게 만든 ViewModel을 Screen컴포저블 함수에 주입함으로써 `MainActivityTest.kt`가 통과할 수 있었습니다.

# 1단계 - 생성자 주입 - 자동

## 구현한 목록

### 1. 의존성을 관리하는 DiContainer 구현
DiContainer의 핵심 아이디어는 다음과 같습니다.
- 만들고자 한 객체의 파라미터를 탐색하여 만들 수 있다.
- 이미 생성한 객체를 재사용하자.
객체를 재사용하기 위해 `objectsMap`이라는 Map을 구현하였습니다. Map으로 구현한 이유는 클래스 타입에 따라 객체를 저장하고, 객체를 탐색할 기준을 클래스 타입으로 잡기 위해 Key로 지정하였습니다.

파라미터를 탐색하기 위해서 `searchObject()` 함수를 구현하였습니다. 이 함수의 의도는 입력받은 파라미터인 Class<T>가 objectsMap의 Key로 존재하느냐를 기준으로 잡고, 존재한다면 이미 존재하는 객체를 불러와서 사용합니다. 반면 `objectsMap`에 존재하지 않는다면, `createObject()` 함수를 통해 객체를 생성하여 `objectsMap`에 저장합니다.

객체를 생성하기 위해 `createObject()` 함수를 구현하였습니다. 이 함수의 의도는 입력받은 타입에 따라 객체를 생성하는 것입니다. 만들고자 한 타입의 객체가 파라미터를 요구하지 않는다면 객체를 바로 생성합니다. 반면 요구하는 파라미터가 존재한다면 `searchObject()` 함수를 사용해 파라미터 타입을 다시 탐색하고 객체를 생성합니다.

### 2. 한 가지 자동 주입 로직으로 ViewModel에 의존성 주입 구현
`viewModel(factory = ...)`에 `DiContainer.DiViewModelFactory()`라는 한 가지 로직을 넣어줌으로써 개발자가 직접 ViewModel에 필요한 파라미터를 주입하지 않아도 되도록 구현하였습니다.

이는 `DiViewModelFactory()`의 `create()`함수가 `createObject()` 함수를 호출함으로써 객체를 자동으로 탐색하고 생성할 수 있도록 구현하였습니다.

## DiContainer 실행 흐름
```text
ProductsViewModel 요청
        ↓
DiViewModelFactory
        ↓
createObject(ProductsViewModel)
        ↓
생성자 분석
        ↓
ProductRepository 필요
        ↓
searchObject(ProductRepository)
        ↓
    ┌───────────────┐
    │ 이미 존재함? │
    └───────┬───────┘
        Yes │ No
         ↓  │  ↓
       반환 │ 생성
            │  ↓
            └→ Map 저장
                ↓
ProductRepository 반환
        ↓
ProductsViewModel 생성
```

## 사용하는 방법

의존성 주입을 받고자 하는 ViewModel의 생성자를 다음과 같이 주입한다.

```kotlin
@Composable
fun ProductScreen(
    viewModel: ProductsViewModel = viewModel(factory = DiContainer.DiViewModelFactory())
)
```

이를 통해 ViewModel마다 직접 Repository를 생성하고 주입해줘야하는 불편함을 해결할 수 있다.

## 기능 요구 사항
다음 문제점을 해결한다.

- [x] ViewModel에서 참조하는 Repository가 정상적으로 주입되지 않는다.
- [x] Repository를 참조하는 다른 객체가 생기면 주입 코드를 매번 만들어줘야 한다.
  - [x] ViewModel에 수동으로 주입되고 있는 의존성들을 자동으로 주입되도록 바꿔본다.
  - [x] 특정 ViewModel에서만이 아닌, 범용적으로 활용될 수 있는 자동 주입 로직을 작성한다. (ProductsViewModel, CartViewModel 모두 하나의 로직만 참조한다)
  - [x] 100개의 ViewModel이 생긴다고 가정했을 때, 자동 주입 로직 100개가 생기는 것이 아니다. 하나의 자동 주입 로직을 재사용할 수 있어야 한다.
- [x] 0.5단계처럼 화면 진입 지점마다 CartRepository를 직접 만들면, 상품 목록 화면과 장바구니 화면이 서로 다른 인스턴스를 갖게 된다. CartRepository는 담은 상품을 메모리에만 들고 있으므로, 상품을 장바구니에 담고 장바구니 화면으로 이동하면 목록이 비어 있다. 앱을 실행해 직접 확인해 보자.
  - [x] 여러 번 인스턴스화할 필요 없는 객체는 최초 한 번만 인스턴스화한다. (이 단계에서는 너무 깊게 생각하지 말고 싱글 오브젝트로 구현해도 된다.)

## 선택 요구 사항
- [x] TDD로 DI 구현
- [ ] Robolectric으로 기능 테스트 (다음 자료 「Robolectric」을 먼저 읽는다. 이 문서 힌트의 MainActivityTest 가 같은 러너를 쓴다)
- [ ] ViewModel 테스트
- [ ] 모든 도메인 로직, Repository 단위 테스트

## 프로그래밍 요구 사항
- 사전에 주어진 테스트 코드가 모두 성공해야 한다.
- Annotation은 이 단계에서 활용하지 않는다.

## 리뷰 체크리스트
- [x] 앱을 실행해 상품 목록과 장바구니가 정상 동작한다.
- [x] ProductsViewModel과 CartViewModel이 같은 자동 주입 로직을 사용한다.
- [x] ViewModel을 하나 더 추가해도 주입 로직을 새로 작성하지 않는다.
- [x] CartRepository 인스턴스가 장바구니 진입마다 새로 생성되지 않는다.
- [x] 사전 제공 테스트가 모두 통과한다.
- [x] Annotation을 사용하지 않았다.

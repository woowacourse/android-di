# android-di

## 2단계 구현 목록

1. Annotation 도입
   - [x] 의존성 주입이 필요한 필드와 그렇지 않은 필드 구분
   - [x] 어노테이션이 명시된 요소만 의존성 주입
   - [x] 어노테이션 동작 테스트 작성
2. Recursive 의존성 주입 및 구조 개선
   - [x] CartRepository가 CartProductDao를 주입받아 참조하도록 변경
   - [x] CartProduct 도메인 모델 추가 및 toDomain() 매퍼 함수 추가
   - [x] CartProduct id 필드 기반 상품 삭제 로직 구현
   - [x] viewModelScope를 통한 비동기 로직 구현
   - [x] 장바구니에 상품 추가 시 상품을 추가한 시각 기록 및 장바구니 화면 표시
3. 선택 요구사향
   - [x] LazyColumn의 items에 key 지정
   - [x] UI 계층에서 CartProductEntity를 직접 참조하지 않도록 구현

## 3단계 구현 목록

1. Qualifier
    - [x] 하나의 인터페이스에 여러 구현체를 `Qualifier` 어노테이션으로 구분

2. 모듈 분리
   - [x] DI 관련 코드를 별도의 모듈로 분리

## 설계 선택 근거

### 1. Qualifier를 애노테이션으로 표현

1. `@field:RoomBacked`처럼 의존성을 받는 위치에서 주입 의도가 잘 보임
2. 선언과 사용이 어노테이션 타입으로 연결되 오타와 이름 충돌을 방지할 수 있음

### 2. `:di`를 순수 JVM 모듈로 구성

DI 관련 파일을 DependencyContainer로 변경하고 별도의 모듈로 분리했음
DependencyContainer는 도메인 객체와 Android Room에 대한 의존성을 갖지 않기 때문에 따로 분리함
Room 데이터 베이스와 Dao를 제공하는 `DataContainer`와 DI 객체를 조립하는 `ShoppingApplication`은 앱에 남겼음

## 4단계 구현 목록

- [x] 바인딩에 스코프 종류 추가
- [x] 스코프별 인스턴스 캐시 분리
- [x] 스코프 종료 시 캐시와 부모, 자식 참조 제거
- [x] CartRepository 앱 스코프 적용
- [x] ProductRepository ViewModel 스코프 적용
- [x] ViewModel clear 시 스코프 종료
- [x] DateFormatter 화면 스코프 등록
- [x] NavBackStackEntry에 화면 스코프 연결
- [x] CartScreen에 DateFormatter 전달
- [x] 스코프 생성, 재사용, 종료 테스트
- [x] 실제 내비게이션 진입, 이탈 테스트
- [x] DI / 서비스 로케이터와 DIP / IoC 설명
- [ ] KSP 전환 시 재설계 지점 설명

### ❓ 내가 만든 컨테이너는 DI인가, 서비스 로케이터인가? 그렇게 판단한 근거를 코드의 구체적인 지점을 들어 설명한다. 그리고 그 판단이 DIP·IoC와는 각각 어떻게 다른 이야기인지 함께 정리한다.

1. 내가 만든 컨테이너는 DI인가, 서비스 로케이터인가?

   - DI: 객체가 필요한 의존성을 외부에서 전달받는 방식으로, 객체는 전달받은 의존성을 사용하고, 어떤 구현을 생성하고 연결할지는 외부에서 결정
   ```kotlin
   class CartViewModel(private val repository: CartRepository,)
   ```
  
   - 서비스 로케이터: 객체가 필요한 의존성을 조회 창구에 요청해 가져오는 방식, 조회 창구가 등록된 서비나 생성 방법을 관리하고, 요청에 맞는 객체를 반환
   ``` kotlin
   class CartViewModel(
       private val locator: ServiceLocator,
   ) {
       private val repository = locator.get(CartRepository::class)
   }
   ```

   - 현재 구현에서 DependencyContainer는 외부에서 전달받은 바인딩 정보와 InstanceProvider를 바탕으로 의존성을 해석하고, 필드 주입과 생성자 파라미터 주입을 수행합니다. 의존성을 사용하는 객체가 직접 구현체를 생성하거나 조회하지 않고 외부에서 전달받는다는 점에서 DI라고 생각합니다. 
   - 다만 NavHost가 화면을 조립하면서 얻은 dateFormatter를 CartScreen에 전달하므로, NavHost에서는 서비스 로케이터 방식으로 의존성을 조회하고, CartScreen에서는 DI 방식으로 의존성을 전달받습니다. 따라서 현재 구현은 DI 컨테이너를 중심으로 구성되어 있으며, 일부 조립 지점에서 서비스 로케이터 방식의 조회를 함께 사용합니다.

2. DIP·IoC와는 각각 어떻게 다른 이야기인가?
    
    - DIP: 상위 모듈이 하위 모듈의 세부 구현에 의존하지 않고 추상화(인터페이스)에 의존해야 한다는 원칙이다. 즉, 특정 객체의 의존성 주입 자체 보단 의존성을 주입하는 과정에서 내부 구현에 영향을 받지 않고 내 서비스에선 항상 동일한 동작을 제공해야 한다는 원칙이라고 생각한다.
    - IoC: 객체의 생성과 의존성 연결 같은 제어 권한을 애플리케이션 코드가 아닌 외부(컨테이너, 프레임워크 등)가 담당 하도록 하는 원칙이다. 이는 의존성 주입보다 더 큰 개념이며, DI는 IoC를 구현하는 여러 방법론 중 하나라고 할 수 있을 것 같다.

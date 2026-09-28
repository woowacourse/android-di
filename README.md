# android-di

## 0.5단계
- [x] 생성자 주입 - 수동

## 1단계
- [x] ViewModel에 수동으로 주입되고 있는 의존성을 자동 주입으로 바꾸기
  - ProductsViewModel, CartViewModel 동일한 자동 주입 로직 적용
- [x] 여러 번 인스턴스화할 필요 없는 객체는 최초 한번만 인스턴스화 하기 
  - ProductsViewModel, CartViewModel이 동일한 CartRepository 사용

## 2단계 기능 구현 목록

- [ ] 런타임 애노테이션을 사용한 ViewModel 필드 주입
  - `@Inject`가 붙은 필드만 주입하고, 표시하지 않은 필드는 유지한다.
  - ProductsViewModel과 CartViewModel에 동일한 필드 주입을 적용한다.
  - 주입 대상 구분, 상속받은 필드, 생성자·필드의 재귀 주입을 테스트한다.
- [ ] 등록한 의존성의 재귀 해결과 싱글톤 관리
  - 외부에서 생성해야 하는 객체와 인터페이스의 구현체를 생성 함수로 등록한다.
  - 등록된 객체는 컨테이너 안에서 한 번만 만들고 공유한다.
  - 순환 의존성은 의존 경로가 포함된 오류로 알리고 테스트한다.
- [ ] 장바구니 도메인 모델과 엔티티 매퍼
  - `CartProduct`에 실제 상품 식별자와 담은 시각, 화면에 필요한 상품 정보를 담는다.
  - `CartProductEntity.toDomain()`을 추가하고 ID와 담은 시각 보존을 테스트한다.
- [ ] Room 기반 장바구니 저장·조회·삭제와 재귀 주입 연결
  - CartRepository 인터페이스와 CartProductDao를 주입받는 DefaultCartRepository를 만든다.
  - 애플리케이션에서 Room Database와 DAO 생성 방법을 등록한다.
  - ViewModel은 Repository의 suspend 함수를 viewModelScope 안에서 호출한다.
  - UI는 CartProduct를 사용하고 리스트 인덱스 대신 Long ID로 삭제한다.
  - 실제 Room 저장·재조회, 중간 항목 삭제, ViewModel 비동기 동작 및 DI 연결을 테스트한다.
- [ ] 장바구니의 담은 시각 표시와 항목 식별
  - DateFormatter로 담은 시각을 표시하고 LazyColumn의 key로 상품 ID를 사용한다.
  - 사전 제공 화면 테스트와 Preview를 새 타입에 맞추고 날짜·삭제 동작을 검증한다.
- [ ] 기존 코드 스타일 오류 정리 및 전체 검증
  - 프로젝트의 ktlint 규칙을 적용한다.
  - 사전 제공 테스트와 추가 테스트, 디버그 APK 빌드 및 lint 검사를 실행한다.

### 의존성 생성 경계

일반 클래스는 주 생성자와 `@Inject` 필드를 통해 자동으로 생성한다. 인터페이스의 구현체는
등록한 생성 함수로 결정한다. Room Database는 추상 클래스이며 Android Context와 Room의
빌더가 필요하므로 애플리케이션에서 생성 방법을 등록한다. DAO도 Room Database를 통해
얻도록 등록한다. Database, DAO, CartRepository는 애플리케이션의 컨테이너 안에서 공유하고,
ViewModel은 ViewModelProvider가 관리한다. DateFormatter의 화면 스코프 주입은 기존 다음
단계 과제로 남겨 두고 현재 화면에서 생성한 인스턴스를 파라미터로 전달한다.

# android-di

## 2단계 구현 목록

1. Annotation 도입
   - [] 의존성 주입이 필요한 필드와 그렇지 않은 필드 구분
   - [] 어노테이션이 명시된 요소만 의존성 주입
   - [] 어노테이션 동작 테스트 작성
2. Recursive 의존성 주입 및 구조 개선
   - [] CartRepository가 CartProductDao를 주입받아 참조하도록 변경
   - [] CartProduct 도메인 모델 추가 및 toDomain() 매퍼 함수 추가
   - [] CartProduct id 필드 기반 상품 삭제 로직 구현
   - [] viewModelScope를 통한 비동기 로직 구현
   - [] 장바구니에 상품 추가 시 상품을 추가한 시각 기록 및 장바구니 화면 표시
3. 선택 요구사향
   - [] LazyColumn의 items에 key 지정
   - [] UI 계층에서 CartProductEntity를 직접 참조하지 않도록 구현
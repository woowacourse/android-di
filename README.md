# android-di

## 구현할 기능 목록

### step0.5

- [x] 수동 생성자 주입
    - [x] 테스트하기 어려운 문제 해결
    - [x] ViewModel에 직접적인 변경사항이 발생하지 않도록 변경

### step1

- [x] 자동 생성자 주입
    - [x] 범용적으로 재사용 될 수 있는 자동 주입 로직을 작성
    - [x] ViewModel에 수동으로 주입되고 있는 의존성들을 자동 주입 로직을 사용하여 주입
- [x] CartRepository 객체는 최초 한 번만 인스턴스화 하도록 작성

### step2

- [x] ViewModel 내 필드 주입을 구현
- [x] 의존성 주입이 필요한 필드와 그렇지 않은 필드를 구분
    - [x] Annotation을 붙여서 필요한 요소에만 의존성을 주입
    - [x] 작성한 의존성 라이브러리가 제대로 작동하는지 테스트 코드를 작성
- [x] CartRepository가 DAO 객체를 참조하도록 변경
- [x] CartProductViewHolder의 bind 함수를 수정하여 뷰에서도 날짜 정보를 확인할 수 있도록 변경

### step3

- [ ] DIContainer의 수동 매핑 생성자를 제거하고 인터페이스를 통해 구현체를 자동 연동
- [ ] 하나의 인터페이스의 여러 구현체가 DIContainer에 등록된 경우를 Qualifier로 개선
- [ ] DI 라이브러리를 모듈로 분리

# android-di

## 0.5단계
- [x] 생성자 주입 - 수동

## 1단계
- [x] ViewModel에 수동으로 주입되고 있는 의존성을 자동 주입으로 바꾸기
  - ProductsViewModel, CartViewModel 동일한 자동 주입 로직 적용
- [x] 여러 번 인스턴스화할 필요 없는 객체는 최초 한번만 인스턴스화 하기 
  - ProductsViewModel, CartViewModel이 동일한 CartRepository 사용
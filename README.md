# android-di

## 0.5단계 변경사항
1. CartViewModel factory 메서드 추가
2. ProductViewModel factory 메서드 추가
3. 각 스크린 별 ViewModelFactory 추가

## 1단계 변경사항
1. Repository 인스턴스 초기화를 위한 커스텀 Application 클래스 생성
   - AndroidManifest 등록
2. Repository 인스턴스를 갖는 container 클래스 생성
3. 공통 ViewModelFactory 생성
4. MainActivity -> ShoppingNavHost로 ViewModelFactory 주입
5. ShoppingNavHost로 -> 각 스크린으로 ViewModelFactory 주입
6. 각 스크린 별로 선언되어 있던 ViewModelFactory 메서드 제거
# 4단계 요구 사항

## 기능 요구 사항

다음 문제점을 해결한다.

- [x] `CartActivity` 에서 사용하는 `DateFormatter` 의 인스턴스를 매번 개발자가 관리해야 한다.
- [x] 모든 의존성이 싱글 오브젝트로 만들어질 필요 없다.
  - [x] `CartRepository` 는 앱 전체 LifeCycle 동안 유지되도록 구현한다.
    - Application의 LifeCycle에 aware하게 만들자
  - [x] `ProductRepository` 는 ViewModel LifeCycle 동안 유지되도록 구현한다.
    - ViewModel의 LifeCycle에 aware하게 만들자
  - [x] `DateFormatter` 는 Activity LifeCycle 동안 유지되도록 구현한다.
    - Activity의 LifeCycle에 aware하게 만들자
- [ ] 내가 만든 DI 라이브러리가 잘 작동하는지 테스트를 작성한다.

## 선택 요구 사항

- [ ] DateFormatter가 Configuration Changes에도 살아남을 수 있도록 구현한다.
- [ ] Activity, ViewModel 외에도 다양한 컴포넌트(Fragment, Service 등)별 유지될 의존성을 관리한다.

## 질문
- 내가 만든 것은 라이브러리인가? 프레임워크인가?
- 내가 만든 것은 서비스 로케이터인가? DI인가?
- 내가 만든 것은 자동 DI인가? 수동 DI인가?
  - 안드로이드 애플리케이션의 구조적 한계
  - 그런데 사용자에게 맡기는 게 자연스러운 걸까?

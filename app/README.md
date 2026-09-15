# 만들면서 배우는 DI 1단계

## 0.5단계

### 목적 
실패하는 테스트(`MainActivityTest`)를 통과시키기 위해 `ProductsScreen`에 `ProductViewModel`을 수동으로 주입한다. 

### 기능 목록

- [x] `ProductViewModel`에 ViewModel factory를 구현한다.
- [x] `ProductViewModel`의 팩토리를 생성하여 필요한 Repository를 주입하고 이를 `ProductsScreen`에 넘긴다.

### 문제점 

1. 화면이 늘어날 때마다 팩토리를 하나씩 새로 쓴다.
2. Repository 객체를 교체하기 위해 또다른 객체를 만들어 바꿔줘야 한다. 즉, ViewModel에 직접적인 변경사항이 발생한다.

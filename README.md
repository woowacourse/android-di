# android-di

## 🚀만들면서 배우는 DI - 0.5단계 생성자 주입(수동)

### 기능 요구 사항

- [x] 테스트하기 어렵다.
- [x] Repository 객체를 교체하기 위해 또다른 객체를 만들어 바꿔줘야 한다. 즉, ViewModel에 직접적인 변경사항이 발생한다.

### 접근 방향

```
테스트 하기 어렵다. & Repository 객체를 교체하기 위해 또다른 객체를 만들어 바꿔줘야 한다.
- Repository 내부에서 사용중인 데이터를 제어할 수 없어서 테스트하기 어렵다.
- Repository를 interface로 추상화하고, ViewModel에서는 추상화된 Repository 타입을 생성자 파라미터로 받는다. 
  -> interface를 구현하는 테스트용 Repository를 생성 후 넣어준다.
  -> ViewModel에서는 직접적인 변경사항 없이 외부에서 해당 Repository 인터페이스를 구현한 객체들을 자유롭게 변경할 수 있다. 
```
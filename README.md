# 🛠️ android-di

## 📚️ 개요
- 의존성 주입(DI)을 직접 구현하며, 수동 → 자동 주입으로 확장한다.
- ViewModel과 Repository 간 결합을 줄이고 테스트 용이성을 높인다.

---

## 🧱 기능 요구 사항 (0.5단계)

- **생성자 주입 – 수동**
    - 테스트하기 어렵다.
    - Repository 교체 시 ViewModel에 직접 변경이 필요하다.

## 🛠️ 구현할 기능 (0.5단계)

- [x] ViewModel에서 Repository를 수동으로 생성자 주입한다.
- [x] Repository 교체 시 ViewModel 코드 변경 필요성을 확인한다.

---

## 🧱 기능 요구 사항 (1단계)

- **생성자 주입 – 자동**
    - ViewModel에서 참조하는 Repository가 정상적으로 주입되지 않는 문제 해결.
    - 새로운 객체가 Repository를 참조하면 매번 주입 코드를 작성해야 하는 문제 해결.
    - ViewModel 의존성 주입을 범용적인 자동 주입 로직으로 대체.
        - MainViewModel, CartViewModel 모두 하나의 로직만 참조.
        - 100개의 ViewModel이 생겨도 자동 주입 로직은 하나만 존재해야 한다.
    - CartRepository를 매번 새로 생성하지 않고 최초 한 번만 인스턴스화한다.

## 🛠️ 구현할 기능 (1단계)

- [ ] 자동 주입 로직을 구현한다.
- [ ] MainViewModel, CartViewModel에서 동일한 로직을 통해 Repository가 주입되도록 한다.
- [ ] 범용적으로 확장 가능한 ViewModel 자동 주입 구조를 만든다.
- [ ] CartRepository를 싱글 오브젝트로 관리한다.

---

## 🧱 선택 요구 사항

- [ ] TDD로 DI 구현
- [ ] Robolectric 기능 테스트
- [ ] ViewModel 테스트
- [ ] 도메인 로직, Repository 단위 테스트

---

## 🛠️ 프로그래밍 요구 사항

- [ ] 제공된 테스트 코드 모두 성공해야 한다.
- [ ] Annotation은 사용하지 않는다.

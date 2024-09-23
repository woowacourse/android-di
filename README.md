# android-di

## 4단계

### 기능 요구 사항

다음 문제점을 해결한다.

- [x] CartActivity에서 사용하는 DateFormatter의 인스턴스를 매번 개발자가 관리해야 한다.
- [x] 모든 의존성이 싱글 오브젝트로 만들어질 필요 없다.
    - [x] CartRepository는 앱 전체 LifeCycle 동안 유지되도록 구현한다.
    - [ ] ProductRepository는 ViewModel LifeCycle 동안 유지되도록 구현한다.
    - [x] DateFormatter는 Activity LifeCycle 동안 유지되도록 구현한다.
- [ ] 내가 만든 DI 라이브러리가 잘 작동하는지 테스트를 작성한다.

### 선택 요구 사항

- [ ] DateFormatter가 Configuration Changes에도 살아남을 수 있도록 구현한다.
- [ ] Activity, ViewModel 외에도 다양한 컴포넌트(Fragment, Service 등)별 유지될 의존성을 관리한다.

### 힌트

각 LifeCycle을 어떻게 관리하느냐에 따라 구현이 천차만별로 달라진다. 이 과정에서 Android LifeCycle에 대한 학습이 요구될 수 있다.

#### Hilt 구현 예시 - Context

Hilt에서는 Predefined qualifiers가 존재한다.
예를 들어 Context 타입의 Android Context를 이용하고 싶다면 @ActivityContext qualifier를 활용하여 해당 인스턴스를 주입받을 수 있다.

```kotlin
class AnalyticsAdapter @Inject constructor(
    @ActivityContext private val context: Context,
    private val service: AnalyticsService
) { ... }
```

#### Hilt 구현 예시 - LifeCycle 관리

Hilt는 Android LifeCycle에 따라 생성된 인스턴스를 자동으로 만들고 제거한다.

[공식 문서 - Hilt Component](https://developer.android.com/training/dependency-injection/hilt-android#generated-components)

| 생성된 구성 요소                 | 생성 위치                  | 소멸 위치                |
|---------------------------|------------------------|----------------------|
| SingletonComponent        | Application#onCreate() | Application 소멸됨      |
| ActivityRetainedComponent | Activity#onCreate()    | Activity#onDestroy() |
| ViewModelComponent        | ViewModel 생성됨          | ViewModel 소멸됨        |
| ActivityComponent         | Activity#onCreate()    | Activity#onDestroy() |
| FragmentComponent         | Fragment#onAttach()    | Fragment#onDestroy() |
| ViewComponent             | View#super()           | View 소멸됨             |
| ViewWithFragmentComponent | View#super()           | View 소멸됨             |
| ServiceComponent          | Service#onCreate()     | Service#onDestroy()  |

### Koin 구현 예시

Android 컴포넌트에 바인딩된 특정 스코프를 생성하여 관리할 수 있다.
[Koin Android Scope API](https://insert-koin.io/docs/reference/koin-android/scope/#android-scope-api)

* `createActivityScope()` - 현재 Activity에 대한 스코프 생성
* `createActivityRetainedScope()` - 현재 Activity에 대한 스코프 생성(Configuration Changes 대응)
* `createFragmentScope()` - 현재 Fragment에 대한 스코프 생성 및 부모 Activity 스코프에 링크

### LifeCycle 시나리오 테스트

다음을 참고하여 Activity 등 컴포넌트의 LifeCycle에 따라 달라지는 상태를 검증하는 테스트 시나리오를 추가할 수 있다.
[Robolectric - Androidx Test](https://robolectric.org/androidx_test)

```kotlin
val controller = buildActivity<ExampleActivity>().setup()
controller.recreate()
controller.pause().stop()
```

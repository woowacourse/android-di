# Hilt - APP

Hilt : Dagger 를 기반으로 만들어진 Android 의 DI 프레임워크로, Dagger 를 사용하기 쉽게 만들어진 라이브러리
따라서, 안드로이드 앱을 위한 DI 프레임워크기에 Pure Java/Kotlin 환경의 Unit Test 에서는 사용하지 못합니다.  
Pure Java/Kotlin 환경에서는 [Dagger2](https://dagger.dev/dev-guide/) 를 사용할 수 있습니다.

- [Hilt](https://dagger.dev/hilt/) 을 활용하여 DI 를 적용한 샘플 앱입니다.
- [Android 공식문서: Dependency injection with Hilt](https://developer.android.com/training/dependency-injection/hilt-android) 을 참고하여 구현하였습니다.
- [Android 공식문서: Hilt testing guide](https://developer.android.com/training/dependency-injection/hilt-testing) 를 참고해서 테스트했습니다.
- [Robolectric - AndroidX Test](https://robolectric.org/androidx_test/) 를 활용하여 통합 테스트를 했습니다.


# 기능 요구 사항

## Step 1
- [x] CartRepository 는 앱 전체 LifeCycle 동안 유지되도록 구현한다.
- [x] ProductRepository 는 앱 전체 LifeCycle 동안 유지되도록 구현한다.
- [x] DateFormatter 는 앱 전체 LifeCycle 동안 유지되도록 구현한다.

---

## Step 2
- [x] 액티비티 컨택스트를 활용해서 DateFormatter 를 생성하도록 구현한다. (Application Context 사용이 더 바람직하나 연습을 위해~)
- [x] DateFormatter 는 ActivityRetainedScoped LifeCycle 동안 유지되도록 구현한다.
- [x] DateFormatter 는 Configuration Changes 에도 살아남을 수 있도록 구현한다.
- [x] CartViewModel 에서는 ViewModelScope 생명주기를 따르는 CartRepository 를 주입받아 사용한다.
- [x] MainViewModel 에서는 SingleTone 생명주기를 따르는 ProductRepository 를 주입받아 사용한다.

## Learn

[Hilt Instrumented Test](https://developer.android.com/training/dependency-injection/hilt-testing#instrumented-tests) 에 따르면 Custom AndroidTestRunner 를 만들고
build.gradle 에 runner 를 갈아껴주면 HiltTestApplication 을 사용할 수 있다고 나온다.


```kotlin
// ❌ HiltTestApplication 으로 변경 안됨
// A custom runner to set up the instrumented application class for tests.
class CustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}
// build.gradle.kts

android {
    defaultConfig {
        // Unit 
        testInstrumentationRunner = "com.example.android.dagger.CustomTestRunner"
    }
}
```

`test` 패키지에서 AndroidX Test 를 활용할 때 위 방법으로 CustomTestRunner 를 적용해보려 했으나 적용이 안됐습니다!

-  위 방식은 `androidTest` 에서만 사용할 수 있다는 것을 배웠습니다.

[Hilt Robolectric tests](https://developer.android.com/training/dependency-injection/hilt-testing#robolectric-tests) 처럼 해야 HiltsTestApplication 로 갈아 낄 수 있습니다.

```kotlin
// ✅ HiltTestApplication 으로 변경
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class)
class MainActivityTest {
    @get:Rule(order = 0) // 얘부터 시작하도록 순서 지정
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val scenarioRule = activityScenarioRule<MainActivity>()
    private val scenario get() = scenarioRule.scenario
}
```

[img.png](image/img.png)

위에 처럼 어떤 Rule 부터 실행할 지 `order` 를 지정해주지 않으면, Hilt 컴포넌트가 초기화 되지 않아서 에러가 발생합니다.
HiltAndroidRule 이 실행 되고 나서 ActivityScenarioRule 이 실행 되도록 순서를 지정해줬습니다.
# Hilt - APP

Hilt : Dagger 를 기반으로 만들어진 Android 의 DI 프레임워크로, Dagger 를 사용하기 쉽게 만들어진 라이브러리
따라서, 안드로이드 앱을 위한 DI 프레임워크기에 Pure Java/Kotlin 환경의 Unit Test 에서는 사용하지 못합니다.  
Pure Java/Kotlin 환경에서는 [Dagger2](https://dagger.dev/dev-guide/) 를 사용할 수 있습니다.

- [Hilt](https://dagger.dev/hilt/) 을 활용하여 DI 를 적용한 샘플 앱입니다.
- [Android 공식문서: Dependency injection with Hilt](https://developer.android.com/training/dependency-injection/hilt-android) 을 참고하여 구현하였습니다.
- [Android 공식문서: Hilt testing guide](https://developer.android.com/training/dependency-injection/hilt-testing) 를 참고해서 테스트했습니다.
- [Robolectric - AndroidX Test](https://robolectric.org/androidx_test/) 를 활용하여 Instrument Test 했습니다.


# 기능 요구 사항

## Step 1
- [x] CartRepository 는 앱 전체 LifeCycle 동안 유지되도록 구현한다.
- [x] ProductRepository 는 앱 전체 LifeCycle 동안 유지되도록 구현한다.
- [x] DateFormatter 는 앱 전체 LifeCycle 동안 유지되도록 구현한다.

---

## Step 2
- [] 액티비티 컨택스트를 활용해서 DateFormatter 를 생성하도록 구현한다. (Application Context 사용이 더 바람직하나 연습을 위해~)
- [] DateFormatter 는 Activity LifeCycle 동안 유지되도록 구현한다.
- [] DateFormatter 는 Configuration Changes 에도 살아남을 수 있도록 구현한다.
- [] CartRepository 는 ViewModel LifeCycle 동안 유지되도록 구현한다.
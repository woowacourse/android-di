# 내 생각 저장소

## 나는 뭘 만들었는가... 프레임워크? 라이브러리??
- 라이브러리와 프레임워크의 차이가 뭘까?
  - 라이브러리는 개발자가 필요할 때 직접 찾아서 사용하는 도구
  - 프레임워크는 프레임워크가 프로그램의 전체적인 실행 흐름과 구조를 관리하고, 정해진 시점에 개발자가 작성한 코드를 호출한다.
  - 즉, 라이브러리는 내가 불러서 사용하고, 프레임워크는 내가 작성한 코드를 정해진 규칙에 따라 불러서 실행한다.
    - 현재 실행 흐름의 주도권이 누구에게 있는가?가 구분의 기준이 될 것 같다.
- 내가 만든 것은 라이브러리 라고 생각한다.
  - 왜냐하면, 의존성을 제거하기 위해 개발자(나)가 의존성 주입이 필요한 부분에 명시를 해줘야한다.
  - 무엇보다 내가 필요로 하는 곳에 DiManager라는 객체를 호출해서 직접 적용하여 사용하기 때문이다.

## 생명주기란?
- 어떤 대상이 생성되어 존재하다가 소멸하기까지의 시간적 흐름.
  - 앱 생명주기: 앱이 실행된 동안
  - ViewModel 생명주기: ViewModel이 생성된 후 onCleared() 될 때까지
  - 화면 생명주기: 화면이 백스택에 들어온 후 백스택에서 제거될 때까지
- 즉, 생명주기는 언제 생성되고 언제 사라지는가? 에 대한 개념이다.

## 스코프란?
- 객체를 생성하고 보관하며 공유하는 범위. 같은 스코프 안에서 객체를 요청하면 같은 인스턴스를 재사용하고, 스코프가 끝나면 해당 인스턴스에 대한 참조를 버린다.
  - 앱 스코프: 앱 전체에서 같은 CartRepository를 사용
  - ViewModel 스코프: 하나의 ViewModel 안에서 같은 ProductRepository 사용
  - 화면 스코프: 하나의 화면 안에서 같은 DataFormatter 사용
- 즉, 스코프는 어디까지 같은 객체를 공유할 것인가? 에 대한 개념이다.

## DisposeEffect
- Compose에서 어떤 작업을 시작하고, 해당 Composable이 더 이상 필요 없어졌을 때 그 작업을 정리하기 위한 API

```kotlin
DisposeEffect(key) {
    start()
    onDispose {
        cleanup()
    }
} 
```

- 즉, DisposeEffect는 같은 key가 유지되는 동안 Effect를 한 번 실행한다.
  - key가 변경되면 기존 Effect를 onDispose로 정리하고, 새로운 Effect를 실행한다.
  - 또한 Composable이 컴포지션에서 사라질 때도 onDispose가 실행된다.

# LifecycleObserver

- LifecycleObserver는 Lifecycle 상태 변경을 수신하기 위한 콜백 인터페이스다.
  - 클래스가 LifecycleObserver와 DefaultLifecycleObserver를 모두 구현하면, DefaultLifecycleObserver의 메서드가 먼저 호출되고, 그 다음에 LifecycleEventObserver.onStateChanged의 호출이 이어진다.
- 재사용 가능하도록 설계할 수 있다.
- 특정 이벤트만 설정할 수 있다.
- Lifecycle-Aware하지 않은 컴포넌트에 적용할 수 있다.
  - Lifecycle Aware은 생명주기를 인식한다는 의미다.
  - 객체나 작업이 Activity, Fragment 같은 UI 컴포넌트의 현재 생명주기 상태를 알고, 그 상태에 맞춰 동작한다는 의미다.
- collectAsState() 는 컴포지션이 살아 있는 동안 계속 수집한다.
  - collectAsStateWithLifecycle()은 안드로이드 생명주기에 맞춰서 수집을 허용 및 중단하는 함수다.
  - 데이터를 계속 받아야 하는 특별한 상황이 아니라면 안드로이드에서는 collectAsStateWithLifecycle()을 사용한다.
    - 안드로이드 생명주기를 사용하지 않는 멀티플랫폼에서는 collectAsStateWithLifecycle()을 사용할 수 없기에 이 때는 collectAsState() 함수를 사용한다.

# ViewModel Lifecycle
- ViewModel은 Activity와 다른 생명주기를 가지고 있기 떄문에 회전이 발생해도 데이터를 유지할 수 있다.
  - 구성 변경이 일어나면 Activity 인스턴스는 소멸하고 새로 만들어지지만, ViewModelStore는 새 인스턴스로 그대로 넘겨진다.
  - Activity는 교체되지만 ViewModelStore는 교체되지 않으므로 ViewModel도 살아남는다.
  - Activity의 onDestory가 호출된 시점에,
    - isChangingConfigurations()가 true이면 ViewModel이 유지된다.
    - isChangingConfigurations()가 false이면 ViewModelStore가 비워지고, ViewModel의 onCleared()가 호출된다.

- ViewModel은 구성 변경까지만 살아남고 프로세스가 종료되면 사라진다.
- Saved Instance State는 프로세스가 종료돼도 복원되지만, Bundle에 담기기 때문에 크기 제약이 있다.

- ViewModel이 View, Context 등을 참조했을 때 문제점은?
  - ViewModel의 수명 주기가 UI보다 길기 때문에, ViewModel 내에 수명 주기 관련 API를 보유하면 메모리 누수가 발생할 수 있다.
  - 메모리 누수란 : 필요하지 않은 메모리를 계속 점유하고 있는 현상
  - GC란 : JVM에서 사용되지 않는 객체를 관리하는 것?

## 지금 내 상황

- 현재 내 앱에서 DiManager가 MutableMap을 통해 한 번 생성한 객체를 관리하며 생성된 객체를 불러오는 작업을 진행한다.
  - 이들은 Application에서 생성한 DiManager가 갖고 있기에 앱 스코프로 관리된다.
  
- 그러면 좋은거 아닌가? 다시 생성하지 않아도 모든 객체를 재사용할 수 있잖아.
  - 객체를 다시 만들지 않아도 된다는 것은 장점이다. 하지만 다음과 같은 상황에서는 앱 스코프로 유지하는 것이 문제가 발생할 수 있다.
    - 화면마다 달라야 하는 객체를 스코프로 보관한다면
      -  화면을 나갔다 다시 들어와도 이전 상태가 남을 수 있다.
      - 서로 다른 화면이나 ViewModel이 같은 객체를 공유하게 된다.
      - 이미 사라진 화면이나 Context를 객체가 참조하면 메모리 누수가 발생할 수 있다.
      - 더 이상 필요 없는 객체도 앱이 종료될 때까지 메모리에 남아 있을 수 있다.
  - 즉, 스코프를 관리하는 것의 목적은 생성 비용을 줄이는 것이 아니라, 객체의 상태와 참조를 필요한 기간만 유지하는 것이다.

## 질문
- ProductsViewModel #2는 새로운 ProductRepository를 받게 될까? 아니면 #1을 받게 될까?
  - 현재는 기존의 ProductRepository를 받게 된다. ProductsViewModel이 생성될 떄 instanceMap에 ProductRepository 객체를 저장한 후, ProductRepository가 불렸을 때 instanceMap에서 찾아서 불러오기 떄문이다.
- DiManager는 두 요청이 서로 다른 ViewModel에서 발생했다는 것을 알 수 있을까?
  - ProductsViewModel #1 과 ProductsViewModel #2는 타입이 같기에, 이 둘을 구분할만한 기준이 없다.
  - 어느 ViewModel을 위해 요청했는가???
- ProductsViewModel #1이 소멸했을 때 ProductRepository #1을 instanceMap에서 제거하는 코드는 현재 존재하는가?
  - 존재하지 않는다. 이전에는 지워야한다는 것을 의식하지 않았기 때문에 구현하지 않았다. 하지만 4단계에서 생명주기에 따라 관리하기 위해서는 instanceMap에서 지우는 기능을 구현해야 할 것이라고 생각한다.
- ProductsViewModel을 보관했다가 다시 반환하는 객체는 ViewModelStore 객체고, ProductRepository를 보관했다가 다시 반환하는 객체는 DiManager의 instanceMap이다.
- ProductsViewModel이 onCleared() 되면 ViewModelStore에서는 제거될 수 있지만, DiManager.instanceMap의 ProductRepository에도 그 사실이 전달되는가?
  - 전달되지 않는다. ProductsViewModel #1, #2, ... 가 삭제되도 ProductRepository가 삭제되어야 한다는 것을 instanceMap은 모른다.
- 현재 instanceMap 한 장에 동일한 DependencyKey로 ProductRepository #1과 #2를 동시에 저장할 수 있는가?
  - 안된다. 현재 instanceMap의 Key는 modelClass 타입과, Qualifier 어노테이션 타입을 가지고 있으며, 각 ProductRepository 객체가 다르다는 것을 구분할 기준이 없다.
- ProductsViewModel #1의 onCleared()가 호출되어 `ProductRepository + null` 키가 삭제되었을 때 ProductsViewModel #2가 사용중인 Repository와 구분해서 #1만 제거할 수 있는가?
  - 안된다. ProductRepository의 key가 동일하게 생성된다. #1이 제거된다면 #2에서도 동일한 객체를 사용하고 있기 때문에 구분해서 지운다는 개념보다 현재는 ProductRepository 객체를 지운다.
- ProductsViewModel #1이 소멸했을 떄 instanceMap 전체를 비우면 어떻게 될까?
  - CartProductDao와 ShoppingDatabase 객체들이 instanceMap에서 지워지게 되고, CartRepository를 호출하는 과정에서 연결될 DAO가 없어지기 때문에 장바구니 화면을 열었을 떄 앱이 꺼지게 될 것이다.
- 현재 DependencyKey는 구현제는 구별할 수 있지만, 동일한 타입의 객체가 서로 다른 생명주기에 속한다는 사실은 구별할 수 없다. 따라서 객체를 제거할 때 특정 ViewModel의 인스턴스인지만 정확히 선택하기 어렵다.

| 저장소 | 무엇을 저장하는가?                                                                                                | 삭제하면 다시 만들 수 있는가?                                                                                                  |
|---|-------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------|
| `providerMap` | 인터페이스로 추상화가 되어있어, 객체를 구분해야하는 클래스 타입을 저장한다.                                       | 다시 만들 수 없다. providerMap은 앱 시작할 때 선언하며, instanceMap과 다르게 관리하기 때문에 삭제 기능이 필요 없다고 생각힜다. |
| `instanceMap` | 객체를 만들 때 필요한 인스턴스들을 저장한다. 예를 들면 ProductRepository, CartRepository, CartProdutDao등이 있다. | 삭제하면 다시 만들 수 없다. 현재까지는 생명주기를 고려하지 않았기에, 삭제 기능이 필요하다 생각하지 않았다.                     |

| 대상 | 어느 Map에 처음 등록되는가? | 제거 후 컨테이너가 다시 만들 방법을 아는가? | 원하는 생명주기      |
|---|-----------------------------|--------------------------------------------:|----------------------|
| `ShoppingDatabase` | instanceMap                 |                                      모른다 | Applicaton 생명주기  |
| `CartProductDao` | instanceMap                 |                                      모른다 | Application 생명주기 |
| `CartRepository` 바인딩 | providerMap                 |                                      모른다 | Application 생명주기 |
| 생성된 `DefaultCartRepository` | instanceMap                 |                                      모른다 | Application 생명주기 |
| `ProductRepository` | instanceMap                 |                                      모른다 | ViewModel 생명주기   |

- 스코프가 종료될 때 제거해야 하는 것은 "그 방법으로 만들어 보관 중인 객체다."

| 대상 | Map에서 객체만 제거하고 다시 요청했을 때 재생성 가능한가? | 재생성에 필요한 정보         |
|---|----------------------------------------------------------:|------------------------------|
| `ShoppingDatabase` |                                                불가능하다 | Database.build()             |
| `CartProductDao` |                                                불가능하다 | Database.createDao()         |
| `DefaultCartRepository` |                                                불가능하다 | CartProductDao, @DefaultCart |
| `ProductRepository` |                                                          불가능하다 | 없음                         |

- providerMap은 생성된 객체가 아니라 구현체를 생성할 방법을 저장한다.
- 현재 instanceMap은 외부에서 등록된 ShoppingDatabase, CartProductDao와 컨테이너가 생성한 Repository를 함께 저장하고 있다.
- 생명주기마다 객체를 따로 제거해야 한다면, 서로 다른 성격과 생명주기의 객체를 Map 하나에 함께 넣어두는 것이 어떤 어려움을 만들까?
  - 지워야 할 때마다 instanceMap을 전부 탐색해봐야한다는 불편함이 있을 것 같다.

- 앱 범위 상자
  - ShoppingDatabase
  - CartProductDao
  - DefaultCartRepository
- ViewModel #1 범위 상자
  - ProductRepository #1
- ViewModel #2 범위 상자
  - ProductRepository #2

- ViewModel #1 범위 상자를 버리면 ProductRepository #1도 함께 사라져야 한다.
- 이 때 앱 범위 상자에 있는 객체가 영향을 받으면 안된다.
- ViewModel #2 범위 상자가 계속 살아 있다면 그 안의 객체는 유지돼야 한다.
- 객체를 만드는 연결 정보인 providerMap은 여러 범위에서 공통으로 참고할 수 있어야 한다.
- ViewModel 범위에서 CartRepository를 요청하였을 때, 그 객체의 생명주기가 앱 범위라면 앱 범위 상자에서 찾아야 한다.

- ViewModel #1에서 ProductRepository 요청
  - ViewModel #1 상자를 확인해야 한다.
  - 없다면 ViewModel #1 상자에 생성해서 저장해야 한다.
- ViewModel #1이 ProductRepository 재요청
  - ViewModel #1 상자를 확인한다
  - ProductRepository #1 객체가 있으면 반환되고, 없으면 재생성을 해야한다.
- ViewModel #2가 ProductRepository 요청
  - ViewModel #2 상자를 확인해야 한다.
- ViewModel #1이 CartRepository 요청
  - 앱 상자를 확인해야 한다.
- ViewModel #2가 CartRepository 요청
  - 앱 상자를 확인해야 한다.
  - 두 요청의 결과는 같은 객체다.
- 컨테이너는 ProductRepository가 어느 스코프 종류에 속하는지 언제 알아야 할까?
  - 객체를 요청할 때 알아야 할 것 같다.
- ViewModel #1 범위 상자와 ViewModel #2 범위 상자를 구분하려면 어떤 성질의 식별 정보가 필요할까?
  - ViewModel마다 고유한 식별자가 있나? 잘 모르곘다.
- 현재의 searchInstance(dependencyKey)에는 다음 중 어떤 정보가 빠져 있나?
  - 어느 범위에서 요청했는가?, 요청한 객체는 어느 종류의 범위에 보관돼야 하는가?
- ViewModel #1 범위에서 CartRepository를 요청했을 때, 컨테이너가 앱 범위 상자로 이동하려면 두 범위 사이에 어떤 관계를 알고 있어야 할까?
  - 어떤 생명주기가 더 큰지를 알고 있어야 할 것 같다.

## 어떻게 구현하지?

- 각 객체를 만들 때 스코프와 관련된 것도 Key로 넣어줘야 하나?
  - 해당 스코프가 끝났음을 인지하면, 객체를 지우고.. 이런 식???
- 스코프 종류가 늘어날 때 컨테이너 내부에 when 분기가 늘어나지 않도록 설계를 고민한다?
  - 음........ 스코프가 뭐길래 구조가 무너지나....
- 근데, 현재는 알아서 해주지 않나? 필요할 때 생성자를 구현한다.
  - CartRepository는 ProductViewModel이 호출될 때 생성된다.
  - 이 때 생성된 CartRepository는 ViewModel과 함께 생성되었지만, ProductViewModel이 사라져도 사라지지 않는다.
    - 이걸 이제, CartRepository의 스코프를 수정하라는건가?
    - 내가 직접 언제 생성되고, 언제 없애는지를 명시를 해버리면, 분리하는 의미가 있나???
- ViewModel 또는 스크린이 시작하면 그 때 필요한 객체를 다 불러오고,
  - ViewModel이 바뀌거나, 스크린이 바뀔 때 객체의 모음을 다 지우고 다시 불러온다?
    - 그러면 이제 생명주기에 맞게 가져올 수 있는거잖아.
    - 앱 생명주기들은 따로 관리한다면, 더 큰 생명주기 중에서 찾아서 가져오면 되니까 뷰모델 생명주기 목록 내에서 지워지는 것은 괜찮은거 아닌가?

- 각 ViewModel 생명주기의 주체가 자신에게 대응하는 객체 모음 하나만 보관한다.
  - V1이 종료됐음은 V1을 사용하는 ViewModel의 onCleared()가 호출되었을 떄 알 수 있다.
  - V1 종료 시 객체 모음을 제거하기 쉽다. 왜냐하면 V1은 하나의 ViewModel에서만 사용하는 객체의 모음이기에 지워졌을 때 객체의 모음을 제거하면 되기 떄문이다.
  - 제거를 깜빡했을 때 누수가 발생할 가능성은 없다. 왜냐하면 V1을 사용하는 주체인 ViewModel이 제거되었을 때 V1의 객체들은 사용되는 곳이 없기 때문에 GC에서 제거 대상이 될 것이라고 생각한다.
  - 앱 범위 객체에 접근할 수 있도록 해야겠지? 안그러면 CartRepository같은 앱 범위 객체를 생성해줘야 하기 때문이다.
  - 알 필요가 없다. 안드로이드에서 생명주기가 끝났을 때 제거에 대한 호출만 해주면, 특정 범위가 종료됐다는 사실만 전달받으면 되기 떄문이다.

- ProductsViewModel #1이 ViewModelStore에서 제거된 뒤, Application에서 V1까지 도달하는 참조 경로가 남아있지 않다.
  -ViewModelStore에서 제거된다면 V1도 없어지기 때문이다. 만약 V1을 DiManager에서 관리하고 있다면, V1이 남아있을 수 있지만, 그럼에도 도달하는 참조경로는 남아있지 않는다.
- 앱 범위 A가 V1을 참조해야 할 이유는 없다. 하지만 반대로 V1은 A를 참조해야 한다. 더 큰 생명주기를 가진 객체를 불러오기 위해서는 새로 생성하는 게 아닌 참조를 해야하기 때문이다.
- 안전하다고 생각한다.
- onClared()라는 Android의 ViewModel을 제거하는 로직은 UI 계층에 있어야 한다.
- 순수 JVM DI 코어는 onCleared()를 알지 않아도 된다.

- Android 계층은 ViewModel이 종료되었다는 사실을 관찰하고, 순수 JVM DI 코어에는 ViewModel이 종료됐다는 일반적인 의미만 전달한다.

| Android 사건 | Android 계층이 기억해야 할 것      | 순수 JVM 코어에 전달할 일반적인 의미                           |
|---|------------------------------------|----------------------------------------------------------------|
| ProductsViewModel #1 최초 생성 | ProductsViewModel #1의 생성 시점   | ProductsViewModel이 생성됐다.                                  |
| 기존 ProductsViewModel #1 재사용 | ProductsViewModel #1이 존재하는가? | 전달 안해도 되지 않나? 이미 생성된 객체를 다시 사용하는거니까. |
| ProductsViewModel #1 `onCleared()` | ProductsViewModel #1의 소멸 시점 | ProductsViewModel이 소멸됐다.                                  |

- 그걸 모르겠다. DiViewModelFactory는 ViewModel이 생성되는 순간을 알지 못한다고 생각한다. 내가 전달해주는 생성 순간에 대한 값이 없으니까.
- 마찬가지로 onCleared()가 호출된 순간도 모른다. 내가 전달하는게 없으니까.
- ViewModel 자신은 자신이 onCleared()가 호출된 것을 알 수 있는건 아닌거 같고, 외부에서 이 범위에 onCleared()가 실행됐다.라는 신호를 보내서 아는게 아닌가? 라는 생각이다.
- 몰라도 된다고 생각한다. 해당 타입을 가지고 있는거지, 활용하는 것은 Android라고 생각한다. 
- 둘의 종료 시점을 아는 주체가 필요할 것이다.

- ViewModelProvider.Factory.create()는 ViewModelStore에서 ViewModel이 존재하지 않을 때 호출된다.
- 이미 생성된 ViewModel을 재사용할떄는 ViewModelStore에서 가져와서 사용하기 때문에 Factory가 다시 호출되지 않는다.
- ViewModel.onCleared()는 ViewModelProvider가 호출한다.
- ViewModel에 종료 시 함꼐 정리할 객체나 작업을 등록할 수 있는 AndroidX기능은 ViewModel.onCleared()를 직접 오버라이드하여 전달하는 것과 AutoClosable을 ViewModel의 상위 생성자에 전달하는 방법이 있다.
- override를 하면 ViewModel을 만든 직후 연결이 되겠지?

Factory는 ViewModel의 생성 시점은 알지만 종료 시점은 직접 알지 못한다. 반대로 ViewModel은 프레임워크로부터 생명주기 콜백을 ㅂ다는다. 따라서 생성 시 만든 스코프와 종료 시 정리할 스코프를 전달할 방법이 필요하다.

| 판단 기준 | `onCleared()` 오버라이드                  | 생성자 `AutoCloseable` | 생성 후 `addCloseable()` |
|---|-------------------------------------------|------------------------|--------------------------|
| 모든 ViewModel 코드를 수정해야 하는가? | 예                                        | 아니요                 | 아니요                   |
| Factory가 범용적으로 적용할 수 있는가? | 아니요. 각각의 ViewModel에 적용해야 한다. | 예                     | 아니요                   |
| ViewModel이 DI 스코프 타입을 알아야 하는가? | 예                                        | 아니요                 | 아니요                   |
| 스코프가 ViewModel과 함께 자동 정리되는가? | 아니요                                    | 예                     | 예                       |
| 새로운 ViewModel을 추가할 때 별도 종료 코드가 필요한가? | 아니요                                    | 아니요                 | 아니요                   |

- 자동으로 실행된다.
- 필요하다.
- 전혀 수정하지 않는 것은 아니다. ViewModel을 구현할 때 scope를 전달해줘야 한다.
- 직후에 addClosable()을 호출할 수 있는지는 모르겠다.

- ViewModel을 먼저 생성한 다음 V1을 만들었을 때 ProductRepository를 V1에 저장할 수 있으냐의 유무는 구현 방식에 따라 다를거 같다.
  - ViewModel을 생성할 때 사용된 파라미터들을 어딘가에 저장하고, V1에 저장한 파라미터들을 연결하면 되지 않나? 라는 생각이 들었다.
- fieldInject()가 ProductRepository를 요청하는 순간, V1의 존재는 몰라도 된다고 생각한다. V1은 ProductRepository 객체의 존재를 알고 있어야 하지만, 반대는 몰라도 된다고 생각한다.
- V1이 AutoClosable 역할을 한다면 닫힐 때 상위 스코프에 대한 참조를 제거해야겠지?
- 참조해야한다.
- D -> A -> B -> C
- Factory가 V1을 만든다고 해도, 현재 fieldInject()에게 “이번 주입은 V1 범위에서 수행하라”는 사실을 전달할 방법이 있는가? 질문에 대한 답을 모르겠다.

- scope의 종류를 어떻게 구분할 것인가?
  - Enum class
  - Annotation
  - Interface

- DI 모듈은 구체적인 스코프 종류를 직접 정의하지 않고 스코프 종류를 표현하는 인터페이스만 제공한다. 사용하는 앱은 해당 인터페이스를 구현해 필요한 스코프 종류를 정의한다. DI 모듈은 그 종류의 구체적인 의미를 알지 못한 채, 의존성에 지정된 종류와 현재 스코프의 종류를 비교해 인스턴스 저장 위치를 결정한다.

---

## 4단계 생명주기 설계 정리

### 목표

의존성을 생명주기에 따라 서로 다른 스코프에서 관리한다.

| 의존성 | 소유 스코프 |
|---|---|
| `ShoppingDatabase` | Application |
| `CartProductDao` | Application |
| `CartRepository` | Application |
| `ProductRepository` | ViewModel |
| `DateFormatter` | Screen |

같은 스코프에서는 동일한 객체를 재사용하고, 스코프가 다르면 별도의 객체를 사용한다.

```text
V1에서 ProductRepository 재요청 → 같은 객체
V1과 V2에서 ProductRepository 요청 → 서로 다른 객체
V1과 V2에서 CartRepository 요청 → 같은 Application 객체
```

### 스코프 종류

`harodi`에는 구체적인 Android 스코프를 선언하지 않고, 스코프 종류를 표현하는 추상화만 제공한다.

```kotlin
interface ScopeKind
```

Application, ViewModel, Screen과 같은 실제 종류는 앱 모듈에서 정의한다. 앱에서 enum이 `ScopeKind`를 구현하는 방식을 고려하고 있다.

`harodi`는 각 종류의 Android 의미를 알지 않고, 같은 종류인지만 비교한다.

### 스코프 인스턴스

같은 ViewModel 종류라도 V1과 V2는 서로 다른 생명주기이므로 고유한 `ScopeKey`로 구별한다.

```kotlin
data class ScopeKey(
    val parentKey: ScopeKey?,
    val uuid: UUID = UUID.randomUUID(),
    val scopeKind: ScopeKind,
)
```

- `scopeKind`: 스코프 종류
- `uuid`: 같은 종류의 서로 다른 스코프 구별
- `parentKey`: 더 긴 생명주기의 스코프 탐색

예상하는 관계는 다음과 같다.

```text
Application A
├─ ViewModel V1
│  └─ Screen S1
└─ ViewModel V2
   └─ Screen S2
```

### 스코프별 인스턴스 저장

기존에는 하나의 `instanceMap`에 모든 객체를 보관했지만, 4단계에서는 스코프마다 별도의 인스턴스 Map을 갖도록 변경한다.

```kotlin
MutableMap<ScopeKey, MutableMap<DependencyKey, Any>>
```

객체 생성 규칙은 여러 스코프가 공통으로 사용하지만, 생성된 객체는 자신의 생명주기에 맞는 스코프에 저장한다.

### 의존성 탐색

각 의존성에는 자신을 소유할 목표 `ScopeKind`가 필요하다.

```text
CartRepository    → Application
ProductRepository → ViewModel
DateFormatter     → Screen
```

의존성을 요청하면 현재 스코프부터 부모 방향으로 이동하며 목표 종류와 일치하는 스코프를 찾는다.

```text
S1에서 ProductRepository 요청

S1(Screen)       → 불일치
V1(ViewModel)    → 일치
V1 저장소에서 조회하거나 생성
```

```text
S1에서 CartRepository 요청

S1(Screen)       → 불일치
V1(ViewModel)    → 불일치
A(Application)   → 일치
A 저장소에서 조회하거나 생성
```

### 생명주기 연결

`harodi`는 순수 JVM 모듈이므로 Android의 생명주기를 직접 알지 않는다.

Android 계층이 실제 생명주기 사건을 감지해 DI 코어에 일반적인 스코프 생성·종료로 전달한다.

```text
ViewModel 생성 → ViewModel ScopeKey 생성
ViewModel 종료 → 해당 ScopeKey 제거

화면 진입 → Screen ScopeKey 생성
화면 종료 → 해당 ScopeKey 제거
```

ViewModel의 종료는 `AutoCloseable`이나 `addCloseable()` 등을 통해 DI 스코프 종료와 연결하는 방법을 검토하고 있다.

### 스코프 종료

`DiManager`가 중앙의 `scopeMap`을 보관하므로, ViewModel이나 화면이 사라져도 해당 저장소가 자동으로 GC되지는 않는다.

스코프 종료 시 정확한 `ScopeKey`의 저장소를 제거해야 한다.

```text
V1 종료
→ V1 저장소와 ProductRepository #1 제거
→ V2 저장소 유지
→ Application 저장소 유지
→ 객체 생성 규칙 유지
```

### 남은 결정

- 의존성과 목표 `ScopeKind`를 어떻게 연결할지
    - 별도 스코프 정책 등록
    - Provider 등록 정보에 포함
    - 애노테이션 사용
- 스코프가 지정되지 않은 의존성을 어떻게 처리할지
- 현재 `ScopeKey`를 주입과 재귀 생성 과정에 어떻게 전달할지
- 부모 스코프 종료 시 자식 스코프 처리 방법
- `ScopeKey`의 동등성 기준
- ViewModel 및 Navigation 생명주기와 실제 스코프 종료를 연결하는 방법



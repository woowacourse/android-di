# android-di

## 구현 기능
자동 의존성 주입 모듈을 구현했습니다. 라이브러리로 만들고 싶지만 코드 리뷰를 위해 일단 모듈로 구현했습니다. 현재 기능은 아래와 같습니다.

1. `SheathApplication` 추상 클래스를 상속하도록 하면 앱 인스턴스 생성 시 특정 인터페이스를 구현한 클래스의 인스턴스를 모두 싱글톤 컨테이너에 저장합니다. 클래스 간의 의존성에 따라 순서대로 인스턴스를 생성하기 위해 위상 정렬 알고리즘을 이용했습니다.
2. `Component`, `Repository` 인터페이스가 있습니다. `Repository`는 단지 특정 클래스가 리포지토리 패턴을 따른다는 것을 명시하기 위한 인터페이스입니다. 기능적으로는 `Component`든 `Repository`든 상관없이 싱글톤 컨테이너에 저장됩니다.
   ```kotlin
   class ProductRepositoryImpl(private val productDao: ProductDao) : ProductRepository, Repository {
       ...
   }

   class ProductDao : Component {
       ...
   }
   ```
   이렇게 코드를 작성하면 다른 컴포넌트가 `ProductRepository` 클래스에 의존한다면 싱글톤 컨테이너에서 `ProductRepositoryImpl` 인스턴스를 찾아서 주입합니다.
   그리고 `ProductRepositoryImpl`를 생성하기 위해 필요한 `ProductDao` 객체 또한 `Component` 인터페이스를 구현하기 때문에 싱글톤 컨테이너에서 찾아서 주입됩니다.

   만약 어떤 클래스의 종속 항목에 해당하는 클래스의 싱글톤 컴포넌트가 여러 개라면 어떤 컴포넌트를 주입해야 할 지 모호하기 때문에 에러가 발생합니다. 아직 한정자 기능은 구현되지 않았습니다.
   어떤 클래스의 종속 항목에 해당하는 클래스의 싱글톤 컴포넌트가 정의되지 않았어도 에러가 발생합니다.
   클래스 간의 종속 사이클이 있어도 에러가 발생합니다.
   이러한 에러는 런타임에 발생합니다.
   
  
4. `Activity` 클래스가 `AndroidEntryPoint` 추상 클래스를 상속한다면 `AndroidEntryPoint`에서 제공하는 `viewModels()` 메소드를 사용할 수 있습니다.
   ```kotlin
    class MainActivity : AndroidEntryPoint() {

        private val viewModel: MainViewModel by viewModels()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(binding.root)
    
            setupBinding()
            setupToolbar()
            setupView()
        }
        ...
    }
   ```
   기존에 사용하던 `viewModels()` 메서드를 사용하면 자동으로 해당 뷰 모델 클래스를 생성하기 위한 종속 항목들을 싱글톤 컨테이너에서 찾아서 주입 후 `ViewModel` 객체를 생성합니다.
   참고로 `AndroidEntryPoint` 클래스는 `AppCompatActivity` 클래스를 상속합니다.

### 세 줄 요약
* **메인 `Application` 클래스가 `SheathApplication` 클래스를 상속하기**, 
* **싱글톤으로 관리할 객체들에 `Component` 혹은 `Component`의 자식 인터페이스를 붙이기**, 
* **뷰모델의 종속 항목을 자동으로 주입하기 위해 `Activity` 클래스에 `AndroidEntryPoint` 상속하기**

## 부족한 점
클래스를 새로 만들고 다시 에뮬레이터를 실행하면 새로 생긴 클래스를 인식하지 못할 때가 있습니다. 이럴 경우 에뮬레이터에서 앱을 지웠다가 다시 실행하면 됩니다.

## 추가 정보
Dagger(단검), Hilt(칼자루) 뜻을 고려하여 Sheath(칼집)이라는 이름으로 지었습니다.

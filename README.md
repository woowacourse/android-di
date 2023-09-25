# android-di

## 구현 기능

자동 의존성 주입 모듈을 구현했습니다. 현재 기능은 아래와 같습니다.

### 1. 의존성 관리 프레임워크 실행
`SheathApplication.run()` 메서드에 `context`를 인자로 넣고 실행하면 설정된 의존성 관리 대상을 모두 스캔합니다.

스캔 후 모든 Sheath 컴포넌트를 위상 정렬 알고리즘을 이용해 의존성에 따라 정렬합니다.

정렬한 뒤 순서대로 인스턴스를 생성합니다.

만약 컴포넌트 간에 의존 사이클이 존재한다면 프레임워크가 실행된 후 에러가 발생합니다.

만약 어떤 컴포넌트가 종속 항목을 주입 받을 수 없다면 프레임워크가 실행된 후 에러가 발생합니다.

만약 어떤 컴포넌트의 종속 항목이 모호하다면 프레임워크가 실행된 후 에러가 발생합니다.

생성된 인스턴스는 모두 컨테이너에 저장됩니다.

이후 특정 컴포넌트의 인스턴스를 받으려 할 때 그 컴포넌트가 싱글톤으로 설정되어 있지 않거나 새로운 인스턴스를 받길 원하면 매번 새로운 인스턴스를 받고 그렇지 않다면 미리 생성된 인스턴스를 받습니다.

### 2. Sheath 컴포넌트
Sheath 컴포넌트란 프레임워크가 실행 된 후 의존성 관리 대상을 말합니다.

의존성 관리 대상은 클래스 또는 함수에 Component 혹은 Component가 붙은 애노테이션을 붙임으로써 설정됩니다.

인스턴스 생성 방식은 Inject 애노테이션이 붙어있다면 그 생성자, 그렇지 않다면 주 생성자로 인스턴스를 생성합니다.

이후 Inject 애노테이션이 붙은 종속 항목이 있다면 인스턴스 생성 뒤 주입됩니다.

함수로 의존성 관리 대상을 추가할 경우, 인스턴스 생성 방식은 해당 함수를 실행함으로써 생성됩니다. 즉, 인스턴스를 생성하는 로직을 함수에 구현해야 합니다.

함수에 매개 변수가 존재한다면 필요한 종속 항목을 주입하여 실행합니다.

### 3. 애노테이션

#### 1. Prototype
이 애노테이션을 의존성 관리 대상에 붙이면 그 클래스의 인스턴스가 다른 컴포넌트에 주입될 때마다 새로운 인스턴스를 주입합니다.

이 애노테이션이 붙어있지 않은 컴포넌트는 항상 같은 인스턴스가 사용됩니다.

#### 2. Component
이 애노테이션을 붙인 클래스나 함수는 의존성 관리 대상이 됩니다.

만약 추상 클래스에 이 애노테이션을 붙였다면 스캔 대상에서 제외됩니다.

인터페이스에 붙였다면 프레임워크가 실행 된 후 에러가 발생합니다.

이 애노테이션이 붙은 함수가 선언된 클래스에 Module 애노테이션이 붙지 않았다면 스캔 대상에서 제외됩니다.

#### 3. Repository
이 애노테이션은 Component 애노테이션을 붙인 것과 똑같이 취급됩니다. 단지 리포지토리 패턴을 따르는 클래스를 표현하기 위해 붙일 수 있습니다.

#### 4. SheathViewModel
이 애노테이션은 Component 애노테이션과 Prototype 애노테이션을 붙인 것과 똑같이 취급됩니다. 뷰 모델 클래스에 붙이는 것이 좋습니다.

이 애노테이션이 붙은 뷰 모델은 `ComponentActivity` 클래스에서 `viewModels()`를 이용해 주입 받을 수 있습니다.

#### 5. Module
이 애노테이션은 object 클래스에 붙일 수 있습니다.

object 클래스가 아닌 클래스에 붙어있다면 프레임워크가 실행 된 후 에러가 발생합니다.

#### 6. Inject
이 애노테이션은 클래스에 Component를 붙인 의존성 관리 대상의 생성자, 메서드, 프로퍼티에 붙이면 동작합니다.

기본적으로 클래스에 Component를 붙인 의존성 관리 대상은 주 생성자로 종속 항목이 주입됩니다.

주 생성자가 아닌 다른 방식으로 종속 항목을 주입하기 위해 이 애노테이션을 사용할 수 있습니다.

여러 생성자가 존재할 때 종속 항목을 주입 받기 위한 생성자에 이 애노테이션을 붙이면 해당 생성자로 종속 항목을 주입받습니다.

여러 생성자에 이 애노테이션을 붙이면 프레임워크가 실행된 후 에러가 발생합니다.

특정 프로퍼티에 자동으로 종속 항목을 주입 받고 싶다면 이 애노테이션을 붙이면 됩니다.

특정 메서드로 종속 항목을 주입 받고 싶다면 이 애노테이션을 붙이면 해당 메서드를 실행하는 데 필요한 종속 항목을 인자로 주입 후 실행합니다.

만약 여러 프로퍼티, 메서드에 이 애노테이션을 붙였다면 모든 종속 항목들을 자동으로 주입합니다.

#### 7. Qualifier
이 애노테이션은 종속 항목 주입 대상에 붙일 수 있습니다.

이 애노테이션은 종속 항목 타입의 서브 클래스를 설정함으로써 여러 클래스 중 한정할 수 있습니다.

```kotlin
@SheathViewModel
class MainViewModel(
   @Qualifier(InMemoryProductRepository::class)
   private val productRepository: ProductRepository
) : ViewModel() {
   ...
}

@Repository
class DatabaseProductRepository : ProductRepository {
   ...
}
```
이렇게 되어있을 경우 MainViewModel이 받길 원하는 컴포넌트의 클래스는 `InMemoryProductRepository`이기 때문에, MainViewModel은 DatabaseProductRepository에 의존하지 않게 됩니다.

프로퍼티와 메서드의 매개변수에 동일하게 붙일 수 있습니다.

만약 어떤 클래스의 자식 클래스가 여러 개 의존성 관리 대상으로 등록 되었다면, 그 클래스에 의존하는 클래스는 한정자를 설정해야 합니다.

그렇지 않다면 종속 항목이 모호하므로 프레임워크가 실행된 후 에러가 발생합니다.

#### 8. NewInstance
이 애노테이션은 종속 항목 주입 대상에 붙일 수 있습니다.

종속 항목 주입 대상에 붙이면 항상 새로운 인스턴스를 주입 받습니다.

## 사용 예시

### Activity에 뷰 모델 주입

```kotlin
class MainActivity : AppCompatActivity() {

   private val viewModel: MainViewModel by viewModels()

   private val dateFormatter: DateFormatter by inject()

   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

@Component
class DateFormatter(context: Context)

@SheathViewModel
class MainViewModel(
   private val productRepository: ProductRepository,
   @Qualifier(DatabaseCartRepository::class)
   private val cartRepository: CartRepository,
) : ViewModel()

interface ProductRepository

@Prototype
@Repository
class DefaultProductRepository : ProductRepository

interface CartRepository

@Repository
class InMemoryCartRepository : CartRepository

@Repository
class DatabaseCartRepository(@NewInstance private val cartProductDao) : CartRepository

@Module
object AppModule {
   @Component
   fun getCartProductDao(context: Context): CartProductDao = CartProductDao(context)
}

class CartProductDao(context: Context)
```
위와 같이 코드를 작성하면 `MainActivity`는 viewModels 확장 함수를 이용해 `MainViewModel` 클래스의 인스턴스를 주입 받습니다.

`MainActivity`는 `inject` 메서드를 이용해 `DateFormatter` 클래스의 인스턴스를 주입 받을 수 있습니다.

`MainViewModel` 클래스는 생성될 때마다 새로운 `DefaultProductRepository` 클래스의 인스턴스를 주입 받습니다. `DefaultProductRepository` 클래스에 `@Prototype`이 붙어있기 때문입니다.

`MainViewModel` 클래스는 생성될 때 `DatabaseCartRepository` 클래스의 인스턴스를 주입 받습니다. 한정자가 설정되어 있기 때문입니다.

`DatabaseCartRepository` 클래스는 생성될 때마다 새로운 `CartProductDao` 클래스의 인스턴스를 주입 받습니다. 주 생성자의 매개 변수에 `@NewInstance`가 붙어 있기 때문입니다.

`CartProductDao`를 생성하는 함수의 매개 변수의 타입으로 `Context`가 정의되어 있습니다. `Context` 클래스에 의존하는 클래스나 함수는 `SheathApplication`을 실행할 때 받은 `Context` 객체를 주입 받습니다.

## 부족한 점

클래스를 새로 만들고 다시 에뮬레이터를 실행하면 새로 생긴 클래스를 인식하지 못할 때가 있습니다. 이럴 경우 에뮬레이터에서 앱을 지웠다가 다시 실행하면 됩니다.

## 추가 정보

Dagger(단검), Hilt(칼자루) 뜻을 고려하여 Sheath(칼집)이라는 이름으로 지었습니다.

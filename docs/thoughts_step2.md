# 내 생각 저장소

- 의존성 주입이 필요한 필드와 그렇지 않은 필드를 구분할 수 없다...?
  - 의존성 주입이 필요한 필드는 애노테이션을 활용해서 주입하고,
  - 그렇지 않다면 직접 주입하나?

- 애노테이션을 활용해서 뭔가 찾고, 마커 역할을 할 수 있다는 것을 인지했다. 

```text
// ViewModel
class ProductsViewModel(...) : ViewModel() {

    lateinit var myObject: MyObject
}

// 만들어진 뒤에 꽂는다. 컨테이너가 이 일을 대신하게 만드는 것이 이번 단계다.
viewModel.myObject = MyObject()
```

- 수동 주입의 예시다. 일단 인스턴스를 만들어놓고, 사용하기 전에 주입이 필요한 필드에 의존성을 주입한다.
  - 만약 의존성을 주입하기 전에 인스턴스를 실행한다면 예외가 발생할 것이다.
- 그렇다면? 저 var myObject 앞에 @MyInject같은 애노테이션을 만들어서
  - @MyInject var myObject: MyObject로 선언하고
  - @MyInject인 것을 찾아서 해당하는 것에는 의존성을 주입해주는 방식???
- 그러면 의존성 주입이 필요한 필드는 뭘까?
  - 반대로 의존성 주입이 필요하지 않은 필드는 뭘까?

- DAO 생성은 어떻게 해???
  - Database도 자동으로 생성할 수 있나?
    - 만약 아니라면, 생성을 해놓고 어떻게 쓰지? 내가 아는 방법은 클래스의 생성자를 찾아서 생성하는 것 밖에 모른다.
    - 이미 존재하는 객체를 찾아와서 .cartProductDao()를 실행할 수 있나?

## 현재 상황

- 변경된 시점에서 실행하면 앱이 터진다.
  - 왜일까 생각해봤을 때, DiContainer에서 인터페이스 구현을 인지하지 못해서 그런 것 같다.
  - 내가 ProductsViewModel이나 CartViewModel에서 필요로 하는 장바구니 저장소는 CartRepository다.
    - 하지만 CartRepository는 인터페이스이고, 실제로 데이터를 관리하는 것은 구현체인 DefaultCartRepository다.
    - 여기서 문제가 발생했다고 이해했다.
- java.lang.IllegalArgumentException: 생성자를 찾을 수 없습니다. interface woowacourse.shopping.data.CartProductDao
  - CartProductDao를 생성하지 못한다.

```text
CartViewModel → CartRepository → CartProductDao → Room Database
```

- CartViewModel을 만들기 위해서는 CartRepository가 필요하고, CartRepository를 만들기 위해서는 CartProductDao가 필요하다...

## 구현 아이디어

```text
class CartViewModel : ViewModel() {
    @MyInject lateinit var cartRepository: CartRepository
}
```
- 전달받는 repository를 파라미터가 아닌 프로퍼티로 바꾸는 것이다.
  - 그렇게 되면 생성자를 만들 수 있다.
  - 사용하기 전에 cartRepository 의존성을 주입해준다면? 끝????
- @MyInject 애노테이션을 가지고 있다면? 의존성 주입을 해준다.
- 그러면 더해서 CartProductDao를 프로퍼티로 만들고, CartRepository에서 lateinit var dao: CartProductDao로 선언하면 안되나?
  - 이렇게 생각한 이유는 사실 잘 모르겠다.... 그냥 이렇게도 되지 않나? 라는 아이디어다.
- 그러면 어떤 의존성을 주입해주고, 의존성 주입을 해주는 기준은 무엇인가?
  - 인스턴스가 자동 생성이 가능한가?

- application에서 db와 dao를 초기화 한 후, objectMap에 주입해주면, ViewModel이 받는 CartRepository의 타입이 DefaultCartRepository일 경우 제대로 동작한다.
  - 하지만 DefaultCartRepository -> CartRepository로 바꾼 경우에 interface로 바뀌게 되면서 생성자를 찾을 수 없다고 한다.
  - 내가 어떻게 DefaultCartRepository 구현체를 쓴다는 것을 알 수 있을까?
  - 애노테이션을 사용해 "나 "DefaultCartViewModel을 쓸거야!"하고 명시를 해주면, 애노테이션을 보고 내가 어떤 생성자를 만들지 알 수 있나? ㅋㅋ

- 바인딩이 뭐야?
  - 바인딩이란 인터페이스를 생성하는 것이 아니라 인터페이스 타입이 요청됐을 때 대신 제공할 실제 객체 또는 생성 방법을 전달하는 것이다.
    - 예를 들면 database.cartProductDao() 라던가 RepositoryImpl 등이다.

```kotlin
fun createCartProductDao(): CartProductDao = searchObject(ShoppingDatabase::class.java).cartProductDao()

fun createCartRepository(): CartRepository = DefaultCartRepository(
    dao = createCartProductDao()
)

// 객체를 탐색한다.
    fun <T : Any> searchObject(modelClass: Class<T>): T {
        if (hasObject(modelClass)) {
            return objectsMap[modelClass] as? T ?: throw IllegalArgumentException("객체를 찾을 수 없습니다.")
        } else {
            if (modelClass == CartRepository::class.java) {
                val instance = createCartRepository() as T
                objectsMap[modelClass] = instance
                return instance
            }
            val instance = createObject(modelClass)
            objectsMap[modelClass] = instance
            return instance
        }
    }
```

- 이런 식으로 의존성을 받도록 해봤다. 이 상황에서 내가 CartRepository를 받을 때를 직접 넣어줘야 한다.
- 하지만 이는 내가 CartRepository를 사용할 때 DefaultCartRepository를 사용한다는 것을 명시해줘야 하며,
  - 의존성이 늘어날 때마다 내가 직접 조건을 추가해줘야 한다.

- 내 생각에는요 인터페이스의 구현체는 개발자가 직접 명시해줘야 한다.
  - 왜냐하면 코드는 내가 무슨 구현체를 쓸지 모른다. 그렇기 때문에 이 규칙은 개발자가 직접 명시해줘야 한다고 생각했다.
  - 그래서 Application에서 내가 Repository interface의 구현체를 무엇을 쓸지 명시해주는 방식으로 구현했다.
  - 그러면 왜 DAO는 Provider에 제공하는 방식으로 구현하지 않았어요?
    - DAO는 클래스 타입을 통해 생성자를 만드는 방식이 아닌 Database를 통해 만들어지는 객체이기 때문에, Repository와는 성격이 다른 생성 방식이 필요하다고 생각했다.

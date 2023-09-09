package woowacourse.shopping.di

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.annotation.Inject
import woowacourse.shopping.data.annotation.Qualifier
import woowacourse.shopping.repository.ProductRepository
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

interface Fake

class Crew(
    @Inject val name: String,
    val nickName: String = "Krrong",
)

class InjectorTest {
    @Test(expected = NullPointerException::class)
    fun `주입하려는 클래스의 주생성자가 없는 경우 NullPointerException을 발생시킨다`() {
        // when & then
        Injector.inject<Fake>(Fake::class)
    }

    @Test
    fun `inject 메서드를 통해 ProductRepository 인스턴스를 생성할 수 있다`() {
        // when
        val repository = Injector.inject<ProductRepository>(DefaultProductRepository::class)

        // then
        assertEquals(repository::class, DefaultProductRepository::class)
    }

    @Test
    fun `Container에 String이 있다면 inject 메서드로 Crew를 인스턴스화 수 있다`() {
        // given
        Container.addInstance(
            String::class,
            "강석진",
        )

        // when
        val crew = Injector.inject<Crew>(Crew::class)

        // then
        assertEquals(crew.name, "강석진")
        assertEquals(crew.nickName, "Krrong")
    }

    @Test
    fun `알루미늄, 플라스틱 하우징을 Container inject를 통해 추가하면 Qualifier를 통해 인스턴스를 얻을 수 있다`() {
        // given & when
        Container.addInstance(
            Housing::class,
            Injector.inject(Aluminum::class),
        )
        Container.addInstance(
            Housing::class,
            Injector.inject(Plastic::class),
        )

        val aluminumHousing = Container.getInstance(Aluminum::class.findAnnotation<Qualifier>()!!)
        val plasticHousing = Container.getInstance(Plastic::class.findAnnotation<Qualifier>()!!)

        assertNotNull(aluminumHousing)
        assertNotNull(plasticHousing)
    }

    @Test
    fun `알루미늄, 플라스틱 하우징을 Container inject를 통해 추가하면 Container의 annotationMap의 크기는 2이다`() {
        // given
        Container.addInstance(
            Housing::class,
            Injector.inject(Aluminum::class),
        )
        Container.addInstance(
            Housing::class,
            Injector.inject(Plastic::class),
        )

        // annotationMap 필드를 가져오기 위해 접근성을 변경
        val annotationMapField = Container::class.memberProperties.find { it.name == "annotationMap" }
        annotationMapField?.isAccessible = true

        // when : annotationMap 필드의 값을 가져옴
        val annotationMap = annotationMapField?.getter?.call() as Map<*, *>

        // then
        assertEquals(annotationMap.size, 2)
    }

    @Test
    fun `as`() {
    }
}

interface Housing

@Qualifier("Aluminum")
class Aluminum : Housing

@Qualifier("Plastic")
class Plastic : Housing

interface Switch

@Qualifier("Blue")
class BlueSwitch : Switch

@Qualifier("Red")
class RedSwitch : Switch

interface KeyCap

@Qualifier("Oem")
class Oem : KeyCap

@Qualifier("Xda")
class Xda : KeyCap

class KeyBoard(
    @Inject private val housing: Housing,
    @Inject private val switch: Switch,
    @Inject private val keyCap: KeyCap,
)

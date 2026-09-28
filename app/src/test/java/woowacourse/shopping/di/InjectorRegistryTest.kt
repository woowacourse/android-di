package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class InjectorRegistryTest {
    private val injector = Injector()

    @Test
    fun `인터페이스부터 외부 객체까지 의존성을 재귀적으로 해결한다`() {
        val database = Database()
        injector.registerSingleton(Database::class) { database }
        injector.registerSingleton(Dao::class) { create(Database::class).dao }
        injector.registerSingleton(Repository::class) { create(DefaultRepository::class) }

        val target = injector.create(Target::class)
        val repository = target.repository as DefaultRepository

        assertThat(repository.dao).isSameAs(database.dao)
        assertThat(injector.create(Repository::class)).isSameAs(repository)
    }

    @Test
    fun `등록한 생성 함수는 최초 요청 때 한 번만 호출한다`() {
        var creationCount = 0
        injector.registerSingleton(Database::class) {
            creationCount++
            Database()
        }
        assertThat(creationCount).isZero()

        val first = injector.create(Database::class)
        val second = injector.create(Database::class)

        assertThat(first).isSameAs(second)
        assertThat(creationCount).isEqualTo(1)
    }

    @Test
    fun `싱글톤으로 등록하지 않은 일반 객체는 요청마다 생성한다`() {
        assertThat(injector.create(Database::class)).isNotSameAs(injector.create(Database::class))
    }

    @Test
    fun `컨테이너가 다르면 등록한 싱글톤을 공유하지 않는다`() {
        val other = Injector()
        injector.registerSingleton(Database::class) { Database() }
        other.registerSingleton(Database::class) { Database() }

        assertThat(injector.create(Database::class)).isNotSameAs(other.create(Database::class))
    }

    @Test
    fun `생성자 순환 의존성은 경로가 포함된 오류로 알린다`() {
        assertThatThrownBy { injector.create(ConstructorA::class) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("ConstructorA -> ConstructorB -> ConstructorA")
    }

    @Test
    fun `필드 순환 의존성은 경로가 포함된 오류로 알린다`() {
        assertThatThrownBy { injector.create(FieldA::class) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("FieldA -> FieldB -> FieldA")
    }

    @Test
    fun `등록된 생성 함수의 순환 의존성도 감지한다`() {
        injector.registerSingleton(Repository::class) { create(Repository::class) }

        assertThatThrownBy { injector.create(Repository::class) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("Repository -> Repository")
    }

    @Test
    fun `생성에 실패한 객체는 캐시하지 않고 다음 요청에서 다시 생성한다`() {
        var attempts = 0
        injector.registerSingleton(Database::class) {
            check(++attempts > 1) { "생성 실패" }
            Database()
        }

        assertThatThrownBy { injector.create(Database::class) }.hasMessage("생성 실패")
        val database = injector.create(Database::class)

        assertThat(injector.create(Database::class)).isSameAs(database)
        assertThat(attempts).isEqualTo(2)
    }

    @Test
    fun `등록하지 않은 인터페이스는 생성 방법이 필요함을 알린다`() {
        assertThatThrownBy { injector.create(Repository::class) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("생성 방법을 등록해야 합니다: Repository")
    }

    @Test
    fun `이미 등록한 의존성을 실수로 덮어쓰지 않는다`() {
        injector.registerSingleton(Database::class) { Database() }

        assertThatThrownBy { injector.registerSingleton(Database::class) { Database() } }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("이미 등록되거나 생성된 의존성")
    }

    interface Dao

    class Database {
        val dao: Dao = object : Dao {}
    }

    interface Repository

    class DefaultRepository(
        val dao: Dao,
    ) : Repository

    class Target {
        @Inject
        lateinit var repository: Repository
    }

    class ConstructorA(
        val dependency: ConstructorB,
    )

    class ConstructorB(
        val dependency: ConstructorA,
    )

    class FieldA {
        @Inject
        lateinit var dependency: FieldB
    }

    class FieldB {
        @Inject
        lateinit var dependency: FieldA
    }
}

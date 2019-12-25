package me.nikolyukin

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import kotlin.random.Random

internal class CreateUserLoginNameTest {

    private lateinit var generator: Random

    @BeforeEach
    fun initDriver() {

        generator = Random(0)
        driver.get(usersUrl)
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 5, 10, 20])
    fun testRandomShortValidLogin(len: Int) {
        val login = generateRandomStringFrom(len, validCharacter, generator)
        usersPage.createUserElement().createUser(login)
        assertTrue(usersPage.hasUser(login))
    }

    @ParameterizedTest
    @ValueSource(ints = [100, 1000])
    fun testLongValidLogin(len: Int) {
        val longLogin = buildString {
            for (i in 0..len) {
                append('O')
            }
            append("_MOJA_OBORONA")
        }
        usersPage.createUserElement().createUser(longLogin)
        assertTrue(usersPage.hasUser("O".repeat(50)))
    }

    @Test
    fun testEmptyLogin() {
        assertThrows(BulbException::class.java) { usersPage.createUserElement().createUser("") }
    }

    @Test
    fun testMultylanguageLogins() {
        val login = """юѢΩï¿ÔÈÃøツ汉"""
        usersPage.createUserElement().createUser(login)
        assertTrue(usersPage.hasUser(login))
    }

    @Test
    @Disabled("user creates but has wrong name in the users table")
    fun testUnicodeLogin() {
        val login = """🤔"""
        usersPage.createUserElement().createUser(login)
        assertTrue(usersPage.hasUser(login))
    }

    @ParameterizedTest
    @ValueSource(chars = ['/', '<', '>', ' '])
    fun testLoginWithForbiddenCharacters(forbiddenChar: Char) {
        val login = generateRandomStringFrom(5, validCharacter, generator) + forbiddenChar + generateRandomStringFrom(5,
            validCharacter, generator)
        assertThrows(LoginSeverityException::class.java) { usersPage.createUserElement().createUser(login) }

    }

    @Test
    fun testUsedLogin() {
        val login = generateRandomStringFrom(6, validCharacter, generator)
        usersPage.createUserElement().createUser(login)
        assertTrue(usersPage.hasUser(login))
        assertThrows(LoginSeverityException::class.java) { usersPage.createUserElement().createUser(login)}
    }

    @Test
    fun testCaseInsensitiveLogin() {
        val commonPart = generateRandomStringFrom(6, validCharacter, generator)
        val login1 = "a$commonPart"
        val login2 = "A$commonPart"
        usersPage.createUserElement().createUser(login1)
        assertTrue(usersPage.hasUser(login1))
        assertThrows(LoginSeverityException::class.java) { usersPage.createUserElement().createUser(login2)}
    }

    @ParameterizedTest
    @ValueSource(chars = [0.toChar(), '\r'])
    fun testLoginWithSpecialSymbol(specialChar: Char) {
        val commonPart = generateRandomStringFrom(3, validCharacter, generator)
        val loginWithSpecialChar = "$commonPart!$specialChar!$commonPart"
        val loginWithoutSpecialChar = "$commonPart!!$commonPart"
        usersPage.createUserElement().createUser(loginWithSpecialChar)
        assertTrue(usersPage.hasUser(loginWithoutSpecialChar))
        assertThrows(LoginSeverityException::class.java) { usersPage.createUserElement().createUser(loginWithoutSpecialChar) }
    }

    @AfterEach
    fun deleteAllNewUsers() {
        driver.get(usersUrl)
        usersPage.deleteNewUsers()
    }

    companion object {
        private const val pathToGeckoDriver = """C:\Users\micha\Documents\spbau\include\geckodriver.exe"""
        private val latinAlphabet = generateSequence('a') { it.inc() }.takeWhile { it <= 'z' }.joinToString("")
        private val validCharacter = latinAlphabet + "0123456789" + """~!@#_-\*+=^"""

        @BeforeAll
        @JvmStatic
        fun initSystem() {
            System.setProperty("webdriver.gecko.driver", pathToGeckoDriver)
            driver = FirefoxDriver()
            driver.get(baseUrl)
            homePage = LoginPage(driver).connectAsRoot()
            usersPage = homePage.goToUsersPage()

        }

        @AfterAll
        @JvmStatic
        fun closeDriver() {
            driver.quit()
        }

        private fun generateRandomStringFrom(len: Int, charList: String, gen: Random): String {
            return generateSequence { gen.nextInt(charList.length) }.map { charList[it] }.take(len).joinToString("")
        }

        private lateinit var driver: WebDriver
        private lateinit var homePage: HomePage
        private lateinit var usersPage: UsersPage
        private const val baseUrl = "http://localhost:8080"
        private const val usersUrl = "http://localhost:8080/users"
    }

}
package me.nikolyukin

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import java.lang.RuntimeException

class CreateUserElement(driver: WebDriver) : Page(driver) {

    @FindBy(id = "id_l.U.cr.login")
    private lateinit var loginInput: WebElement

    @FindBy(id = "id_l.U.cr.password")
    private lateinit var passwordInput: WebElement

    @FindBy(id = "id_l.U.cr.confirmPassword")
    private lateinit var confirmPasswordInput: WebElement

    @FindBy(id = "id_l.U.cr.createUserOk")
    private lateinit var okButton: WebElement

    fun createUser(login: String, password: String = "1111") {
        loginInput.sendKeys(login)
        passwordInput.sendKeys(password)
        confirmPasswordInput.sendKeys(password)
//        Thread.sleep(3000)
        okButton.click()

        if (driver.currentUrl.contains("editUser")) {
            goBack()
            return
        }

        val severityErr = driver.findElements(By.cssSelector("li[class='errorSeverity']"))
        if (severityErr.size > 0) {
            throw LoginSeverityException()
        }

        val bulbErr = driver.findElements(By.cssSelector("div[class='error-bulb2']"))
        if (bulbErr.size > 0) {
            throw BulbException()
        }

        throw UserCreateException()
    }
}

open class UserCreateException() : RuntimeException()
class LoginSeverityException() : UserCreateException()
class BulbException() : UserCreateException()

package me.nikolyukin

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy

class LoginPage(driver: WebDriver) : Page(driver) {

    @FindBy(id = "id_l.L.login")
    private lateinit var loginInput: WebElement

    @FindBy(id = "id_l.L.password")
    private lateinit var passwordInput: WebElement

    @FindBy(id = "id_l.L.loginButton")
    private lateinit var loginButton: WebElement

    fun connectAsRoot(): HomePage {
        loginInput.sendKeys("root")
        passwordInput.sendKeys("root")
        loginButton.click()
        return HomePage(driver)
    }
}
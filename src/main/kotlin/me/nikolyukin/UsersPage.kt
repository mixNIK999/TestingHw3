package me.nikolyukin

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions

class UsersPage(driver: WebDriver) : Page(driver) {

    @FindBy(id = "id_l.U.createNewUser")
    private lateinit var createUserButton: WebElement

    @FindBy(xpath = NEW_USERS_XPATH)
    private lateinit var newUsersDeleteButtons: MutableList<WebElement>

    fun createUserElement(): CreateUserElement {
        createUserButton.click()
        return CreateUserElement(driver)
    }

    fun hasUser(name: String): Boolean {
        return driver
            .findElements(By.xpath("//a[@cn='l.U.usersList.UserLogin.editUser' and text()='$name']"))
            .isNotEmpty()
    }

    fun deleteNewUsers() {
        while (true) {
            val list = newUsersDeleteButtons.toList()
            list.firstOrNull()?.let {
                it.click()
                driver.switchTo().alert().accept()
                driver.navigate().refresh()
            } ?: return
        }
    }
    companion object {
        private const val NEW_USERS_XPATH = "//a[text()='New Users']/../../td/a[@cn='l.U.usersList.deleteUser']"
    }
}

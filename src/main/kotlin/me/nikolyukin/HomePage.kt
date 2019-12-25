package me.nikolyukin

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy

class HomePage(driver: WebDriver) : Page(driver) {

    @FindBy(css = "span[class='ring-menu__item__i ring-font-icon ring-font-icon_cog']")
    private lateinit var optionsButton: WebElement

    @FindBy(xpath = "//a[@class='ring-dropdown__item ring-link' and contains(., 'Users')]")
    private lateinit var userRef: WebElement


    fun goToUsersPage(): UsersPage {
        optionsButton.click()
        userRef.click()
        return UsersPage(driver)
    }
}
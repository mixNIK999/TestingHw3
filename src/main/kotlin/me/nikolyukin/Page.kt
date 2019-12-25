package me.nikolyukin

import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.WebDriverWait

open class Page(protected val driver: WebDriver) {

    protected val wait = WebDriverWait(driver, 5)

    init {
        PageFactory.initElements(driver, this)
    }

    fun goBack() {
        driver.navigate().back()
    }
}

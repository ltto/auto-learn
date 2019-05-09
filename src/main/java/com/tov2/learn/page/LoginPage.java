package com.tov2.learn.page;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


@Slf4j
public class LoginPage {
    private WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public SubjectListPage nextPage() {
        login();
        log.info("登录成功");
        return new SubjectListPage(driver);
    }

    private void login() {
        String userName = System.getProperty("userName");
        String password = System.getProperty("password");
        driver.get("http://cecysu.jxjy.chaoxing.com/login");
        log.info("进入登录页面 账号:{},密码:{}", userName, password);
        driver.findElement(By.id("userName")).sendKeys(userName);
        driver.findElement(By.id("passWord")).sendKeys(password);
        try {
            doLogin();
        } catch (Exception ignored) {
        }
    }

    private void doLogin() {
        WebElement verifyCode = driver.findElement(By.id("verifyCode"));
        verifyCode.click();
        log.info("输入验证码!");

        while (verifyCode.getAttribute("value").trim().length() != 4) {
        }
        driver.findElement(By.cssSelector("#loginForm > table > tbody > tr:nth-child(4) > td:nth-child(2) > input")).click();
        while (!driver.getCurrentUrl().contains("i.mooc.chaoxing.com")) {
            String loginMsg = driver.findElement(By.id("loginMsg")).getText();
            if ("验证码错误，请重新输入".equalsIgnoreCase(loginMsg.trim())) {
                log.info("验证码错误!");
                doLogin();
            }
        }
    }
}

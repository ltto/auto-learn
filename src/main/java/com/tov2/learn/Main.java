package com.tov2.learn;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final String CSS_SELECTOR_JAVA_WEB = "body > div.wrap870 > div.bxCourse > div:nth-child(4) > a.fr.topBtn";
    private static final String CSS_SELECTOR_MULTIMEDIA = "body > div.wrap870 > div.bxCourse > div:nth-child(6) > a.fr.topBtn";

    private static WebDriver driver = new ChromeDriver();

    public static void main(String[] args) {
        driver.get("http://cecysu.jxjy.chaoxing.com/login");

        login();
        String level = System.getProperty("level");
        if ("1".equalsIgnoreCase(level)) {
            selectLevel(CSS_SELECTOR_JAVA_WEB);
        } else {
            selectLevel(CSS_SELECTOR_MULTIMEDIA);
        }
        openLevelList();
    }

    private static void selectLevel(String levelCSSSelector) {
        driver.switchTo().frame(driver.findElement(By.id("frame_content")));
        driver.findElement(By.cssSelector(levelCSSSelector)).click();
        Set<String> windowHandles = driver.getWindowHandles();
        windowHandles.forEach(h -> {
            if (!driver.getWindowHandle().equalsIgnoreCase(h)) {
                driver.switchTo().window(h);
            }
        });
    }

    private static void login() {
        String userName = System.getProperty("userName");
        String password = System.getProperty("password");
        driver.findElement(By.id("userName")).sendKeys(userName);
        driver.findElement(By.id("passWord")).sendKeys(password);
        try {
            doLogin();
        } catch (Exception ignored) {
        }
    }

    private static void doLogin() {
        WebElement verifyCode = driver.findElement(By.id("verifyCode"));
        verifyCode.click();
        while (verifyCode.getAttribute("value").trim().length() != 4) {

        }
        driver.findElement(By.cssSelector("#loginForm > table > tbody > tr:nth-child(4) > td:nth-child(2) > input")).click();

        while (!driver.getCurrentUrl().contains("i.mooc.chaoxing.com")) {
            String loginMsg = driver.findElement(By.id("loginMsg")).getText();
            if ("验证码错误，请重新输入".equalsIgnoreCase(loginMsg.trim())) {
                doLogin();
            }
        }
    }


    private static void openLevelList() {
        List<WebElement> units = driver.findElements(By.cssSelector(".units"));
        int unitsSize = units.size();
        for (int i = 1; i <= unitsSize; i++) {
            String unitsSelector = String.format(".units:nth-of-type(%d)", i);
            WebElement unitElement = driver.findElement(By.cssSelector(unitsSelector));
            int levelSize = unitElement.findElements(By.cssSelector(".leveltwo")).size();
            for (int j = 1; j <= levelSize; j++) {
                WebElement levelTwoCell = driver.findElement(By.cssSelector(unitsSelector + " " + String.format(".leveltwo:nth-of-type(%d)", j)));
                String levelStatus = levelTwoCell.findElement(By.cssSelector("h3>.icon>em")).getAttribute("class");
                if ("orange".equalsIgnoreCase(levelStatus)) {
                    levelTwoCell.findElement(By.cssSelector("h3>.articlename>a")).click();
                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    switchLevelTag();
                }
            }
        }
    }

    private static void toVideoFrame() {
        driver.switchTo().defaultContent();
        driver.switchTo().frame(driver.findElement(By.cssSelector("#iframe")));
        driver.switchTo().frame(driver.findElement(By.cssSelector("#ext-gen1039 > iframe")));
    }

    private static void toVideoDetails() {
        driver.switchTo().frame(driver.findElement(By.cssSelector("#iframe")));
    }

    private static void switchLevelTag() {
        List<WebElement> tags = driver.findElements(By.cssSelector("#mainid > div.tabtags > span"));
        int size = tags.size();
        for (int i = 0; i < size; i++) {
            WebElement tag = driver.findElement(By.cssSelector(String.format("#mainid > div.tabtags > span:nth-of-type(%d)", i + 1)));
            if (!tag.getAttribute("id").startsWith("dct")) break;
            if (!tag.getAttribute("class").contains("currents")) {
                tag.click();
            }
            toVideoDetails();
            if (driver.findElement(By.cssSelector("#ext-gen1039")).getAttribute("class").contains("ans-job-finished")) {
                driver.switchTo().defaultContent();
                continue;
            }
            doLevel();
        }
        if (size == 0) {
            doLevel();
        }
        nextLevel();
    }

    private static void doLevel() {
        toVideoFrame();
        try {
            TimeUnit.SECONDS.sleep(2);
            driver.findElement(By.cssSelector("#video > button")).click();
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ignored) {
        }
        String playTime = driver.findElement(By.cssSelector("#video > div.vjs-control-bar > div.vjs-current-time.vjs-time-control.vjs-control > span.vjs-current-time-display")).getText();
        String totalTime;
        do {
            totalTime = driver.findElement(By.cssSelector("#video > div.vjs-control-bar > div.vjs-duration.vjs-time-control.vjs-control > span.vjs-duration-display")).getText();
        } while ("0:00".equals(totalTime.trim()) || "00:00".equals(totalTime.trim()));
        new Actions(driver).moveToElement(driver.findElement(By.cssSelector("#video")));
        while (!(playTime.equals(totalTime) && !"".equals(playTime.trim()))) {
            driver.findElement(By.cssSelector("#video_html5_api")).sendKeys(Keys.UP);
            WebElement playBtn = driver.findElement(By.xpath("//*[@id=\"video\"]/div[4]/button[1]"));
            String playStatus = playBtn.getAttribute("class");
            if (playStatus.contains("vjs-paused")) playBtn.click();
            new Actions(driver).moveToElement(driver.findElement(By.cssSelector("#video")));
            playTime = driver.findElement(By.cssSelector("#video > div.vjs-control-bar > div.vjs-current-time.vjs-time-control.vjs-control > span.vjs-current-time-display")).getText();
        }
    }

    private static void nextLevel() {
        driver.navigate().back();
        driver.navigate().refresh();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
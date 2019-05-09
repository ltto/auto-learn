package com.tov2.learn.page;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.concurrent.TimeUnit;

@Slf4j
public class CourseDetailsPage {
    private WebDriver driver;

    public CourseDetailsPage(WebDriver driver) {
        log.info("进入听课页面");
        this.driver = driver;
    }

    public void doLearn() {
        String alert = null;
        try {
            alert = driver.findElement(By.cssSelector("form[action*=processVerify]")).getText();
        } catch (Exception ignored) {
        }
        if (alert != null) {
            log.error("听课异常:{}", alert);
        } else {
            switchLevelTag();
        }
    }

    private void switchLevelTag() {
        int tagSize = 0;
        try {
            tagSize = driver.findElements(By.cssSelector("#mainid > div.tabtags > span")).size();
        } catch (Exception ignored) {
        }
        log.info("这几课有{}个选项卡", tagSize);
        if (tagSize == 0) {
            doPlayTry();
        }
        for (int i = 0; i < tagSize; i++) {
            WebElement tag = driver.findElement(By.cssSelector(String.format("#mainid > div.tabtags > span:nth-of-type(%d)", i + 1)));
            if (!tag.getAttribute("id").startsWith("dct")) break;
            if (!tag.getAttribute("class").contains("currents")) {
                tag.click();
            }
            if (!tag.getText().contains("视频")) {
                continue;
            }
            toVideoDetails();
            if (driver.findElement(By.cssSelector("#ext-gen1039")).getAttribute("class").contains("ans-job-finished")) {
                driver.switchTo().defaultContent();
                continue;
            }
            doPlayTry();
        }
    }

    private void toVideoDetails() {
        log.info("定位#iframe");
        driver.switchTo().frame(driver.findElement(By.cssSelector("#iframe")));
    }

    private void doPlayTry() {
        try {
            doPlay();
        } catch (Exception e) {
            log.error("听课失败 这一节可能没视频 错误原因:{}", e.getMessage());
        }
    }

    private void doPlay() {
        toVideoFrame();
        try {
            TimeUnit.SECONDS.sleep(2);
            log.info("点击播放按钮");
            driver.findElement(By.cssSelector("#video > button")).click();
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ignored) {
        }
        String playTime = driver.findElement(By.cssSelector("#video > div.vjs-control-bar > div.vjs-current-time.vjs-time-control.vjs-control > span.vjs-current-time-display")).getText();
        String totalTime;
        do {
            totalTime = driver.findElement(By.cssSelector("#video > div.vjs-control-bar > div.vjs-duration.vjs-time-control.vjs-control > span.vjs-duration-display")).getText();
        } while ("0:00".equals(totalTime.trim()) || "00:00".equals(totalTime.trim()) || "".equals(totalTime.trim()));
        log.info("获取视频总时长{}", totalTime);
        log.info("设置鼠标移开仍然播放");
        new Actions(driver).moveToElement(driver.findElement(By.cssSelector("#video")));
        while (!(playTime.equals(totalTime) && !"".equals(playTime.trim()))) {
            driver.findElement(By.cssSelector("#video_html5_api")).sendKeys(Keys.UP);
            WebElement playBtn = driver.findElement(By.xpath("//*[@id=\"video\"]/div[4]/button[1]"));
            String playStatus = playBtn.getAttribute("class");
            if (playStatus.contains("vjs-paused")) playBtn.click();
            new Actions(driver).moveToElement(driver.findElement(By.cssSelector("#video")));
            playTime = driver.findElement(By.cssSelector("#video > div.vjs-control-bar > div.vjs-current-time.vjs-time-control.vjs-control > span.vjs-current-time-display")).getText();
            System.out.print("\r 当前播放时间" + playTime);
        }
        log.info("视频播放{}, 播放完毕", playTime);
    }

    private void toVideoFrame() {
        driver.switchTo().defaultContent();
        driver.switchTo().frame(driver.findElement(By.cssSelector("#iframe")));
        driver.switchTo().frame(driver.findElement(By.cssSelector("#ext-gen1039 > iframe")));
    }
}

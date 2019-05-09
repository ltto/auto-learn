package com.tov2.learn.page;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

@Slf4j
public class CourseListPage {
    private WebDriver driver;

    public CourseListPage(WebDriver driver) {
        this.driver = driver;
    }

    public void learnCourse() {
        log.info("开始听课");
        List<WebElement> units = driver.findElements(By.cssSelector(".units"));
        int unitsSize = units.size();
        log.info("共 {} 章!", units.size());
        for (int i = 1; i <= unitsSize; i++) {
            String unitsSelector = String.format(".units:nth-of-type(%d)", i);
            WebElement unitElement = driver.findElement(By.cssSelector(unitsSelector));
            String unitName = unitElement.findElement(By.cssSelector("h2>a")).getText();
            int levelSize = unitElement.findElements(By.cssSelector(".clearfix")).size();
            log.info("{}, 共{}节", unitName, levelSize);
            for (int j = 1; j <= levelSize; j++) {
                WebElement levelTwoCell = driver.findElement(By.cssSelector(unitsSelector + " " + String.format(".clearfix:nth-of-type(%d)", j)));
                String levelName = levelTwoCell.findElement(By.cssSelector(".articlename")).getText();
                log.info("请听 {}", levelName);
                String levelStatus = levelTwoCell.findElement(By.cssSelector(".icon>em")).getAttribute("class");
                if ("orange".equalsIgnoreCase(levelStatus)) {
                    levelTwoCell.findElement(By.cssSelector(".articlename>a")).click();
                    CourseDetailsPage courseDetailsPage = new CourseDetailsPage(driver);
                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    courseDetailsPage.doLearn();
                    nextCourse();
                } else {
                    log.info("这节不用听 打扰了");
                }
                log.info("**************");
            }
        }
    }

    private void nextCourse() {
        driver.navigate().back();
        driver.navigate().refresh();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

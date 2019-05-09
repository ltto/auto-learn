package com.tov2.learn.page;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

@Slf4j
public class SubjectListPage {
    private WebDriver driver;

    public SubjectListPage(WebDriver driver) {
        log.info("进入科目选择页面!");
        this.driver = driver;
    }

    public CourseListPage nextPage() {
        selectCourse();
        return new CourseListPage(driver);
    }

    private void selectCourse() {
        driver.switchTo().frame(driver.findElement(By.id("frame_content")));
        List<WebElement> subjectList = driver.findElements(By.cssSelector(".courseDetail .topBtn"));
        List<WebElement> list = driver.findElements(By.cssSelector(".bxCourse .clearfix"));
        int size = list.size();
        System.out.println(":::输入序号:::选择课程:::");
        for (int i = 0; i < size; i++) {
            System.out.println(i + "---" + list.get(i).findElement(By.cssSelector(".detailRIght h3 a")).getText());
        }
        Scanner scanner = new Scanner(System.in);
        String s = null;
        int index = 0;
        do {
            s = scanner.nextLine();
            try {
                index = Integer.parseInt(s);
            } catch (Exception ignored) {
                s = null;
            }
        } while (StringUtils.isBlank(s));

        log.info("当前选择的是第{}--{}", index, list.get(index).findElement(By.cssSelector(".detailRIght h3 a")).getText());
        WebElement subjectBut = subjectList.get(index);
        log.info("进入听课页面");
        subjectBut.click();
        Set<String> windowHandles = driver.getWindowHandles();
        windowHandles.forEach(h -> {
            if (!driver.getWindowHandle().equalsIgnoreCase(h)) {
                driver.switchTo().window(h);
            }
        });
    }
}

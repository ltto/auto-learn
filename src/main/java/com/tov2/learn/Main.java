package com.tov2.learn;

import com.tov2.learn.page.CourseListPage;
import com.tov2.learn.page.LoginPage;
import com.tov2.learn.page.SubjectListPage;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;

@Slf4j
public class Main {


    public static void main(String[] args) throws IOException {
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--disable-dev-shm-usage", "--no-sandbox", "--headless", "--disable-gpu", "--window-size=2048,1536", "--ignore-certificate-errors", "--silent");
        WebDriver driver = new ChromeDriver(options);
        try {
            LoginPage loginPage = new LoginPage(driver);
            SubjectListPage subjectListPage = loginPage.nextPage();
            CourseListPage courseListPage = subjectListPage.nextPage();
            courseListPage.learnCourse();
        } catch (Exception e) {
            log.info("听课中断{}", e.getMessage());
            e.printStackTrace();
            System.in.read();
            System.in.read();
            System.in.read();
        }

    }

}
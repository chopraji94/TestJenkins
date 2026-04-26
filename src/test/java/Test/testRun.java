package Test;

import base.TestBase;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class testRun extends TestBase {

    @Test(priority = 0)
    public void start(){
        driver.get("https://practicetestautomation.com/practice-test-login/");
    }

    @Test(priority = 1)
    public void login(){
        driver.findElement(By.cssSelector("input#username")).sendKeys("student");
        driver.findElement(By.cssSelector("input#password")).sendKeys("Password123");
        driver.findElement(By.cssSelector("button#submit")).click();
    }

    @Test(priority = 2)
    public void test(){
        String text = driver.findElement(By.xpath("//h1[text()='Logged In Successfully']")).getText();
        Assert.assertTrue(text.equals("Logged In Sucssfully"));
    }
}

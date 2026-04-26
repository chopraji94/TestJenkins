package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

public class TestBase {

    public WebDriver driver;
    public ExtentReports extent;
    String path = System.getProperty("user.dir")+"/src/Report/";
    String fullpath;

    private ThreadLocal<ExtentTest> parent = new ThreadLocal<>();
    private ThreadLocal<ExtentTest> child = new ThreadLocal<>();

    @BeforeSuite
    public void setupExtent(){
        fullpath = path+"TestRepo_"+System.currentTimeMillis()+".html";
        ExtentSparkReporter reporter = new ExtentSparkReporter(fullpath);
        reporter.config().setDocumentTitle("Test Doc Title");

        extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Pranav","Windows");

    }

    @BeforeClass
    public void setUp(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        ExtentTest parentTest = extent.createTest(getClass().getSimpleName());
        parent.set(parentTest);
    }

    @BeforeMethod
    public void beforeMethod(Method method){
        ExtentTest test = parent.get().createNode(method.getName());
        child.set(test);
    }

    @AfterMethod
    public void afterMethod(ITestResult result) throws IOException, InterruptedException {
        if(result.getStatus() == ITestResult.FAILURE){
            child.get().fail("The test is failed");

            Thread.sleep(10000);
            File source = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            String destination = System.getProperty("user.dir") + "/screenshots/" + "New" + System.currentTimeMillis() + ".png";
            File finalDestination = new File(destination);
            Thread.sleep(10000);
            FileUtils.copyFile(source,finalDestination);
            Thread.sleep(10000);
            child.get().addScreenCaptureFromPath(destination);

        }
        else if(result.getStatus() == ITestResult.SUCCESS){
            child.get().pass("The test is passed");
        }
        else if(result.getStatus() == ITestResult.SKIP){
            child.get().skip("The test is skiped");
        }
    }

    @AfterClass
    public void afterClass(){
        driver.quit();
    }

    @AfterSuite
    public void afterSuite(){
        extent.flush();
        System.out.println(fullpath);
    }
}

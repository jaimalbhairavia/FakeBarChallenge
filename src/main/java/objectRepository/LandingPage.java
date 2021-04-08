package objectRepository;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class LandingPage {

    private static WebElement element = null;

    //Left Bowl - cell 1
    By leftBowlCell1 = By.id("left_0");

    //Right bowl - cell1
    By rightBowlCell1 = By.id("right_0");

    //reset button
    By resetButton = By.xpath("//button[contains(text(),'Reset')]");

    //weight button
    By weighButton = By.id("weigh");

    //weighing
    By weighings = By.tagName("li");

    //measureresults
    By measureResults = By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[2]/button[1]");

    //goldbars
    By goldbars = By.xpath("//body/div[@id='root']/div[1]/div[2]//button");


    public WebElement getLeftFirstCell(WebDriver driver){
        element = driver.findElement(leftBowlCell1);
        return element;
    }

    public WebElement getRightFirstCell(WebDriver driver){
        element = driver.findElement(rightBowlCell1);
        return element;
    }

    public WebElement getResetButton(WebDriver driver){
        element = driver.findElement(resetButton);
        return element;
    }

    public WebElement getWeighButton(WebDriver driver){
        element = driver.findElement(weighButton);
        return element;
    }

    public List<WebElement> getWeighings(WebDriver driver){
        List<WebElement> elements = driver.findElements(weighings);
        return elements;
    }

    public WebElement getMeasureResults(WebDriver driver){
        element = driver.findElement(measureResults);
        return element;
    }

    public List<WebElement> getGoldBars(WebDriver driver){
        List<WebElement> bars = driver.findElements(goldbars);
        return bars;
    }
}

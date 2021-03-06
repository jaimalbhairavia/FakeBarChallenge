import io.github.bonigarcia.wdm.WebDriverManager;
import objectRepository.LandingPage;
import org.openqa.selenium.Alert;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class FakeBar {

    private WebDriver driver;
    private LandingPage landingPage;
    private Alert alert;


    @BeforeTest
    public void setup(){
        WebDriverManager.chromedriver().setup();
        landingPage = new LandingPage();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://ec2-54-208-152-154.compute-1.amazonaws.com/");
//        driver.manage().window().fullscreen();

        //Assertion to check if the uRL is reached and is correct
//        Assert.assertEquals(driver.getCurrentUrl(), "http://ec2-54-208-152-154.compute-1.amazonaws.com/");
    }

    @Test
    public void test_01_findFakeBar(){
        try{
            int[] numberOfBars = new int[]{0,1,2,3,4,5,6,7,8};
            int fakeCandidate1 = 0, fakeCandidate2 =0;
            int i=0, j=numberOfBars.length-1;
            int fakeBar=Integer.MIN_VALUE;
            String measureResult;
            Set<Integer> goodBars = new HashSet<>();

              while(true){
                  measureResult = enterNumberAndClickWeigh(i, j, numberOfBars);

                //when bars are same
                if (measureResult.contains("=")) {
                    goodBars.add(numberOfBars[i]);
                    goodBars.add(numberOfBars[j]);
                    i++;
                    j--;

                    //when 4th bar is the only remaining, it is the fake bar
                    if(i == j){
                        fakeBar = i;
                        clickFakeBarAndPrintWeighings(fakeBar);
                        break;
                    }

                }

                //when bars are different
                else if (measureResult.contains(">") || measureResult.contains("<") && goodBars.isEmpty()){
                     /*this will only execute if the first iteration of new bar comparision results in '>' or '<'
                     fake candidates are 0 and 8 */
                    fakeCandidate1 = numberOfBars[i];
                    fakeCandidate2 = numberOfBars[j];
                    j--;
                    measureResult = enterNumberAndClickWeigh(fakeCandidate1, j, numberOfBars);
                    fakeBar = decideFakeBar(measureResult, fakeCandidate1, fakeCandidate2);
                    clickFakeBarAndPrintWeighings(fakeBar);
                    break;
                    }

                    //this will only execute if the code is in the second or more iteration of new bar comparision
                else{
                    fakeCandidate1 = numberOfBars[i];
                    fakeCandidate2 = numberOfBars[j];
                    measureResult = enterNumberAndClickWeigh(goodBars.iterator().next(), fakeCandidate1, numberOfBars);
                    fakeBar = decideFakeBar(measureResult, fakeCandidate1, fakeCandidate2);
                    clickFakeBarAndPrintWeighings(fakeBar);
                    break;
                    }
                }

            }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    //Method to click on reset, fill out bowl grid, click on weigh
    private String enterNumberAndClickWeigh(int i, int j, int[] array){
        try {
            landingPage.getResetButton(driver).click();
            landingPage.getLeftFirstCell(driver).sendKeys(String.valueOf(array[i]));
            landingPage.getRightFirstCell(driver).sendKeys(String.valueOf(array[j]));
            landingPage.getWeighButton(driver).click();
            driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        return landingPage.getMeasureResults(driver).getText();
    }


    //method decides the fake bar from the candidates
    private int decideFakeBar(String result, int fakeCandidate1, int fakeCandidate2){
        int fakeBar;
        if(result.contains(">") || result.contains("<")){
            fakeBar=fakeCandidate1;
        }
        else{
            fakeBar = fakeCandidate2;
        }
        return fakeBar;

    }

    //method clicks the fake bar on the UI and prints the weighing's
    private void clickFakeBarAndPrintWeighings(int fakeBar){
        try{
            landingPage.getGoldBars(driver).get(fakeBar).click();
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

            alert = driver.switchTo().alert();
            String alertMessage = alert.getText();
            System.out.println(alertMessage);
            Assert.assertEquals(alertMessage, "Yay! You find it!");
            alert.accept();

            int numberOfWeighings = landingPage.getWeighings(driver).size();
            System.out.println("The total number of weighing's are: " + numberOfWeighings);
            for(WebElement webElement : landingPage.getWeighings(driver)) {
                System.out.println(webElement.getText());
            }
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    @AfterTest
    public void afterTest(){
        driver.quit();
    }
}

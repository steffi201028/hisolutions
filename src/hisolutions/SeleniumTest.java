package hisolutions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.os.CommandLine;
import geb.Browser;
import geb.Page;
import org.openqa.selenium.remote.*;










public class SeleniumTest {
	
	public static  void main(String[] args) {
		
			String currentURL;
			System.setProperty("webdriver.chrome.driver","C:\\xampp\\htdocs\\hisolutions\\chromedriver.exe");
			
			
			//$(class:'main-menu-item-text', text:"Templates").click();
		    //WebDriver driver = new ChromeDriver();
			DesiredCapabilities  caps = new DesiredCapabilities();
			caps.setJavascriptEnabled(true);
			caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "C:/xampp/htdocs/hisolutions/phantomjs-2.1.1-windows/bin/phantomjs.exe");
			WebDriver driver = new PhantomJSDriver(caps);
			driver.get("http://www-03.ibm.com/software/sla/sladb.nsf/3f1f7af09a76f13f8525702d005422d8/0007278864ef7bd885257e18006862c9?OpenDocument");
			
				
				//PhantomJSDriver webdriver = new PhantomJSDriver("C:\\xampp\\htdocs\\hisolutions\\phantomjs-2.1.1-windows\\bin\\phontomjs.exe");
				
				//driver.get("http://www-03.ibm.com/software/sla/sladb.nsf/3f1f7af09a76f13f8525702d005422d8/0007278864ef7bd885257e18006862c9?OpenDocument");
				
				WebElement element = driver.findElement(By.name("ibm-submit"));
				JavascriptExecutor executor = (JavascriptExecutor)driver;
				executor.executeScript("arguments[0].click();", element);
				currentURL = driver.getCurrentUrl(); 
			    System.out.println(currentURL);
			}
	
	



	}


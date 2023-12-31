package com.cst438;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;

/*
 * This example shows how to use selenium testing using the web driver 
 * with Chrome browser.
 * 
 *  - Buttons, input, and anchor elements are located using XPATH expression.
 *  - onClick( ) method is used with buttons and anchor tags.
 *  - Input fields are located and sendKeys( ) method is used to enter test data.
 *  - Spring Boot JPA is used to initialize, verify and reset the database before
 *      and after testing.
 *      
 *    Make sure that TEST_COURSE_ID is a valid course for TEST_SEMESTER.
 *    
 *    URL is the server on which Node.js is running.
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class EndToEndScheduleTest {

	public static final String CHROME_DRIVER_FILE_LOCATION = "/Users/machavez/Desktop/chromedriver-mac-arm64/chromedriver";

	public static final String URL = "http://localhost:3000";

	public static final String TEST_USER_EMAIL = "tester@csumb.edu";

	public static final String NEW_TEST_USER_EMAIL = "new_email@csumb.edu";

	public static final int TEST_COURSE_ID = 40442; 

	public static final String TEST_SEMESTER = "2021 Fall";

	public static final int SLEEP_DURATION = 1000; // 1 second.


	/*
	 * add course TEST_COURSE_ID to schedule for 2021 Fall semester.
	 */
	
//	@Test
//	public void addCourseTest() throws Exception {
//
//	
//		// set the driver location and start driver
//		//@formatter:off
//		// browser	property name 				Java Driver Class
//		// edge 	webdriver.edge.driver 		EdgeDriver
//		// FireFox 	webdriver.firefox.driver 	FirefoxDriver
//		// IE 		webdriver.ie.driver 		InternetExplorerDriver
//		//@formatter:on
//
//		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
//		WebDriver driver = new ChromeDriver();
//		// Puts an Implicit wait for 10 seconds before throwing exception
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//
//		try {
//
//			driver.get(URL);
//			Thread.sleep(SLEEP_DURATION);
//
//			// select the last of the radio buttons on the list of semesters page.
//			
//			List<WebElement> weList = driver.findElements(By.xpath("//input"));
//			// should be 3 elements in list.  click on last one for 2021 Fall
//			weList.get(2).click();
//
//			// Locate and click "View Schedule" button
//			
//			driver.findElement(By.id("viewSchedule")).click();
//			Thread.sleep(SLEEP_DURATION);
//
//			// Locate and click "Add Course" button which is the first and only button on the page.
//			driver.findElement(By.id("addCourse")).click();
//			Thread.sleep(SLEEP_DURATION);
//
//			// enter course no and click Add button
//			
//			driver.findElement(By.id("courseId")).sendKeys(Integer.toString(TEST_COURSE_ID));
//			driver.findElement(By.id("add")).click();
//			Thread.sleep(SLEEP_DURATION);
//
//			/*
//			* verify that new course shows in schedule.
//			* search for the title of the course in the updated schedule.
//			*/ 
//			
//			WebElement we = driver.findElement(By.xpath("//tr[td='"+TEST_COURSE_ID+"']"));
//			assertNotNull(we, "Test course title not found in schedule after successfully adding the course.");
//			
//			// drop the course
//			WebElement dropButton = we.findElement(By.xpath("//button"));
//			assertNotNull(dropButton);
//			dropButton.click();
//			
//			// the drop course action causes an alert to occur.  
//			WebDriverWait wait = new WebDriverWait(driver, 1);
//            wait.until(ExpectedConditions.alertIsPresent());
//            
//            Alert simpleAlert = driver.switchTo().alert();
//            simpleAlert.accept();
//            
//            // check that course is no longer in the schedule
//            Thread.sleep(SLEEP_DURATION);
//            assertThrows(NoSuchElementException.class, () -> {
//            	driver.findElement(By.xpath("//tr[td='"+TEST_COURSE_ID+"']"));
//            });			
//
//		} catch (Exception ex) {
//			throw ex;
//		} finally {
//			driver.quit();
//		}
//
//	}
	
	/*
	 * add student student_ID to course TEST_COURSE_ID for 2021 Fall semester.
	 */
	@Order(1)
	@Test
	public void addStudentTest() throws Exception {
		// set the driver location and start driver
		//@formatter:off
		// browser	property name 				Java Driver Class
		// edge 	webdriver.edge.driver 		EdgeDriver
		// FireFox 	webdriver.firefox.driver 	FirefoxDriver
		// IE 		webdriver.ie.driver 		InternetExplorerDriver
		//@formatter:on

		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {
			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);

			driver.get(URL + "/admin");
			Thread.sleep(SLEEP_DURATION);

			//Locate and click "Add Student" button
			driver.findElement(By.id("addStudent")).click();
			Thread.sleep(SLEEP_DURATION);

			//enter student email and student name
			driver.findElement(By.id("email")).sendKeys(TEST_USER_EMAIL);
			driver.findElement(By.id("name")).sendKeys("TestStudent");
			driver.findElement(By.id("add")).click();

			//Verify that the new student is in the student list
			WebElement we = driver.findElement(By.xpath("//tr[td='"+TEST_USER_EMAIL+"']"));
			assertNotNull(we, "Test student not found in student list after successfully adding the student.");
			
			
		}catch(Exception ex) {
			throw ex;
		}finally {
			driver.quit();
		}
	}
	
	/*
	 * Update student, that was added in previous test, with new email and name
	 */
	@Order(2)
	@Test
	public void updateStudentTest() throws Exception {
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		try {
			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);

			driver.get(URL + "/admin");
			Thread.sleep(SLEEP_DURATION);

			//Click last "Edit" button in the last row of the student list
			List<WebElement> weList = driver.findElements(By.xpath("//button"));
			weList.get(weList.size()-3).click();
			Thread.sleep(SLEEP_DURATION);

			//Click "Edit Student" button
			driver.findElement(By.id("editStudent")).click();
			Thread.sleep(SLEEP_DURATION);

			//Enter new email and name and status
			driver.findElement(By.id("email")).sendKeys(NEW_TEST_USER_EMAIL);
			driver.findElement(By.id("name")).sendKeys("NewName");
			driver.findElement(By.id("status")).sendKeys("Good");
			Thread.sleep(SLEEP_DURATION);

			driver.findElement(By.id("edit_complete")).click();
			Thread.sleep(SLEEP_DURATION);

			//Verify that the student was updated
			WebElement we = driver.findElement(By.xpath("//tr[td='"+NEW_TEST_USER_EMAIL+"']"));
			assertNotNull(we, "Test student not found in student list after successfully updating the student.");
		}catch(Exception ex) {
			throw ex;
		}finally {
			driver.quit();
		}
		
	}
	
	/*
	 * Delete student
	 */
	@Order(3)
	@Test
	public void deleteStudentTest() throws Exception {
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		try {
			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);

			driver.get(URL + "/admin");
			Thread.sleep(SLEEP_DURATION);

			//Click last "Edit" button in the last row of the student list
			List<WebElement> weList = driver.findElements(By.xpath("//button"));
			weList.get(weList.size()-4).click();
			Thread.sleep(SLEEP_DURATION);
			
			//Click "Ok" to confirm delete
			driver.switchTo().alert().accept();
			Thread.sleep(SLEEP_DURATION);
			
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.alertIsPresent());
          
			Alert simpleAlert = driver.switchTo().alert();
			simpleAlert.accept();
			Thread.sleep(SLEEP_DURATION);
			
			//Verify that the student was deleted
			assertThrows(NoSuchElementException.class, () -> {
				driver.findElement(By.xpath("//tr[td='"+NEW_TEST_USER_EMAIL+"']"));
			});	
			
		}catch(Exception ex) {
			throw ex;
		}finally {
			driver.quit();
		}
		

	}
}

package learn.junit5;

import org.junit.jupiter.api.*;
//All assertions method are there in the below package.
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

//JUnit works on the principle that "No news is good news". We haven't written a test case in the MathUtilsTest class
//We've just printed something to the console. JUnit just looks at the whatever method is having the @Test annotation
//and if that method passes the execution then, it shows success message.

//if you want JUnit to create one instance for all the methods, use @TestInstance(TestInstance.Lifecycle.PER_CLASS)
//this doesn't guarantee the sequential operation of the methods.
// It just creates one instance for the entire class.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Running MathUtilsTestClass")
class MathUtilsTest {

    MathUtils mathUtils;

    TestInfo testInfo;
    TestReporter testReporter;

    //Instead of passing a message directly when the test fails,
    // we should pass a messageSupplier to avoid computational overhead as lambda are decalartive, they'll only execute
    // when the test case fails.

    //Creating a nested class. Note, we've to add the @Nested annotations as well.
    @Nested
    @DisplayName("Add method")
    class AddTest{
        @Test
        @DisplayName("Testing add method for positive")
        void testAddPositive(){
            assertEquals(2,mathUtils.add(1,1),()->"The result should be positive");
        }

        @Test
        @DisplayName("Testing add method for negative")
        void testAddNegative(){
            assertEquals(-2,mathUtils.add(-1,-1),"The result should be negative");
        }
    }

    //if you want JUnit to create one instance for all the methods, use @TestInstance(TestInstance.Lifecycle.PER_CLASS).
    // Note: if you haven't created this annotation then you have to make the BeforeAll method static.
    @BeforeAll
    void beforeAllInit(){
        System.out.println("This needs to run before all init");
    }


    //TestInfo and TestReporter are Java interfaces, one is used for providing info for the test and the other is used
    // for reporting to an external source.
    @BeforeEach
    void init(TestInfo testInfo, TestReporter testReporter){
        this.testInfo = testInfo;
        this.testReporter = testReporter;
        mathUtils = new MathUtils();
        testReporter.publishEntry("Running "+testInfo.getDisplayName()+" with tags "+testInfo.getTags());
    }

    @Test
    @DisplayName("Multiply Method")
    void multiplyTest(){
        /*
        assertAll takes in a bunch of supplier lambdas and executes it one by one,
        if any of the test fails, then assertAll fails.
         */
        assertAll(
                ()->assertEquals(4,mathUtils.multiply(2,2)),
                ()->assertEquals(0,mathUtils.multiply(2,0)),
                ()->assertEquals(-2,mathUtils.multiply(2,-1))
        );
    }



    @Test
    void test(){
        int expected = 2;
        int actual = mathUtils.add(1, 1);
        //Well technically we could have done if (expected == actuals) but we're gonna leverage JUnit's power of reporting
        //whether the test case pass.
        // We'll be using assertion.
        //assertEquals(expected, actual); //Hey JUnit, I'm asserting the expected and actual values are the same.

        assertEquals(expected, actual,"The sum should be 2"); //a message when the test fails.

        //assertArrayEquals(expectedArray, actualArray); Verifies each item in the arrays are equal in the right position

        //assertIterableEquals(expectedIterable, actualIterable);
        // Verifies each item in the iterable are equal in the corresponding positions.

        //https://junit.org/junit5/docs/current/api/org.junit.jupiter.api/org/junit/jupiter/api/Assertions.html
        //(check for more assertions methods)

    }
    //The @Test annotation is required for JUnit to mark this as a testing method.
    @Test
    void testComputerCircleRadius(){
        assertEquals(314.1592653589793, mathUtils.computeCircleArea(10),"Should return right circle area.");
    }

    //The @DisplayName annotation allows you name your test methods.
    @Test
    @DisplayName("Testing testDivide Method")
    void testDivide(){

        assumeTrue(true);
        //assertThrows(ArithmeticException.class,() -> mathUtils1.divide(1, 1));
        assertThrows(ArithmeticException.class,() -> mathUtils.divide(1, 0),"Hi, expecting an arithmetic exception to be thrown.");
        //What assertThrows(expectedType, Supplier executableLambda) does is that, it takes in an exception class that
        // we might think can come as an exception and then it takes an executable lambda where we pass in whatever may
        // throw the exception.

    }

    //Suppose you're on a TDD architecture. TestDrivenDevelopment architecture. Now the whole test class is failing
    // because of one test that you haven't implemented. So here this @disabled annotation helps, which disable the
    // test case intentionally.
    @Test
    @Disabled
    @DisplayName("Test Driven Development. Should not run.")
    void testDisabledIntentionally(){
        fail("This test should be disabled for now.");
    }

    /*
    By writing the annotation RepeatedTest(3), we're instructing JUnit to repeat this test for 3 times.
    Along with that by creating a reference of RepetitionInfo inside our testRepetition method, we're allowing JUnit to
    assign an object to the reference and we can run various methods on that instance.
     */


    @Tag("Important")
    @RepeatedTest(3)
    @DisplayName("Demo-ing @Tag & @RepeatedTest")
    void testRepetition(RepetitionInfo repetitionInfo){
        System.out.println(repetitionInfo.getCurrentRepetition());
        System.out.println(repetitionInfo.getTotalRepetitions());
    }

    //Note tag name shouldn't contain space anywhere in it.
    @Test
    @Tag("No_So_Important")
    @DisplayName("Demo-ing TestInfo")
    void demoTestInfo(){
        //testReporter.publishEntry("Running "+testInfo.getDisplayName()+" with tags "+testInfo.getTags());
        System.out.println("Running "+testInfo.getDisplayName()+" with tags "+testInfo.getTags());
    }

    /*
    1. TestInfo

        TestInfo is an interface in JUnit 5 that provides information about the currently running test. This can
        include the test’s display name, its tags, the test class, and the test method.

    Use Cases

	•	Test Metadata: You can use TestInfo to access metadata about the test, such as its name, class, and method.
	    This is particularly useful for logging or dynamically adjusting test behavior based on this information.
	•	Conditional Logic: You might use the information from TestInfo to run certain logic only when specific tests
	    are being executed.

	2. TestReporter

    TestReporter is another interface that allows you to publish additional information about the test run. This can be
    particularly useful for logging purposes, where you might want to output test-specific information to a report.

    Use Cases

	•	Custom Logging: Use TestReporter to log custom information during the test execution. This can be useful for
	    generating detailed test reports or debugging.
	•	Reporting Test Results: You can use TestReporter to output test results or other relevant information that might
	    be useful for understanding the test’s context or outcome.


	    They both use Dependency Injection.

     */

}

/*
Test life cycle:
Suppose there's a test class with three methods. When we run a test JUnit will run the methods. Note methods don't run
in any particular order but if we want we can mention the order by adding an @Order annotation. However it's not
recommended, we should not define the flow of the test methods inside the test class.
The methods should be independent from each other.

JUnits actually creates a new class instance for every test method it runs.
You shouldn't create any instance variable inside your test class. It causes dependency, and indirectly we're ordering
the methods and infact JUnit 5 doesn't allow it as it creates separate instance for all the separate test methods.
 */

/*
1. @BeforeAll

	•	Purpose: This annotation is used to specify a method that should be run once before all the tests in the class.
	             It’s typically used for expensive setup tasks that are needed only once for the entire test class.
	•	Requirements: The method must be static unless the test class is annotated with @TestInstance(Lifecycle.PER_CLASS).

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ExampleTest {

    @BeforeAll
    static void initAll() {
        System.out.println("Before all tests");
        // Initialize resources shared by all tests
    }

    @Test
    void testOne() {
        System.out.println("Running Test One");
    }

    @Test
    void testTwo() {
        System.out.println("Running Test Two");
    }
}

Output: Before all tests
        Running Test One
        Running Test Two


2. @BeforeEach

	•	Purpose: This annotation is used to specify a method that should be run before each test in the class. It’s used
	    for setup tasks that need to be done before every single test, such as resetting variables or initializing objects.

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExampleTest {

    @BeforeEach
    void init() {
        System.out.println("Before each test");
        // Setup code executed before each test
    }

    @Test
    void testOne() {
        System.out.println("Running Test One");
    }

    @Test
    void testTwo() {
        System.out.println("Running Test Two");
    }
}

Output: Before each test
        Running Test One
        Before each test
        Running Test Two

3. @AfterEach

	•	Purpose: This annotation is used to specify a method that should be run after each test in the class. It’s
	    typically used for cleanup tasks that need to be performed after every single test, such as closing database
	    connections or resetting configurations.

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class ExampleTest {

    @AfterEach
    void tearDown() {
        System.out.println("After each test");
        // Cleanup code executed after each test
    }

    @Test
    void testOne() {
        System.out.println("Running Test One");
    }

    @Test
    void testTwo() {
        System.out.println("Running Test Two");
    }
}

Output: Running Test One
        After each test
        Running Test Two
        After each test

4. @AfterAll

	•	Purpose: This annotation is used to specify a method that should be run once after all the tests in the class.
	    It’s typically used for cleanup tasks that should only be done once, like closing a shared resource or cleaning
	    up data used across tests.
	•	Requirements: The method must be static unless the test class is annotated with @TestInstance(Lifecycle.PER_CLASS).

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

public class ExampleTest {

    @AfterAll
    static void tearDownAll() {
        System.out.println("After all tests");
        // Cleanup code executed after all tests
    }

    @Test
    void testOne() {
        System.out.println("Running Test One");
    }

    @Test
    void testTwo() {
        System.out.println("Running Test Two");
    }
}

Output: Running Test One
        Running Test Two
        After all tests

 */


/*
In JUnit 5, the @BeforeAll and @AfterAll methods are typically required to be static because they are intended to run
before and after all the tests in a test class, respectively. Here’s why they need to be static:

Reason for @BeforeAll and @AfterAll Being static

	1.	Single Invocation:
	    •	The @BeforeAll and @AfterAll methods are designed to be executed once per test class, not once per test
	        instance. Since JUnit creates a new instance of the test class for each test method by default, a non-static
	        method would be associated with a particular instance of the test class. Making these methods static ensures
	        they are not tied to any particular instance and are invoked only once for the entire class.
	2.	No Test Class Instance Available:
	    •	Before any test methods are executed, JUnit hasn’t instantiated the test class yet, so there’s no test class
	        instance available to invoke instance methods. This is why @BeforeAll must be static—it runs before any test
	        instances are created.
	    •	Similarly, after all test methods are executed, the instance may have already been discarded or be in an
	        indeterminate state, so @AfterAll must also be static.
	3.	Consistency Across Tests:
	    •	By making the methods static, JUnit ensures that they are not dependent on the state of any particular test
	        method or instance. This guarantees that the setup or teardown code is consistent and applies to the whole
	        test class.

Example with @TestInstance(Lifecycle.PER_CLASS)

If you want to use instance methods for @BeforeAll and @AfterAll, JUnit 5 provides an option with the
@TestInstance(Lifecycle.PER_CLASS) annotation. This annotation changes the test instance lifecycle so that only one
instance of the test class is created, allowing you to use non-static methods for @BeforeAll and @AfterAll.

 */


/*
Summary of Conditional Execution Annotations

	•	@EnabledOnOs / @DisabledOnOs: Enable or disable tests based on the operating system.
	•	@EnabledOnJre / @DisabledOnJre: Enable or disable tests based on the JRE version.
	•	@EnabledIf / @DisabledIf: Enable or disable tests based on a custom condition.
	•	@EnabledIfSystemProperty / @DisabledIfSystemProperty: Enable or disable tests based on a system property value.
	•	@EnabledIfEnvironmentVariable / @DisabledIfEnvironmentVariable: Enable or disable tests based on an environment
	    variable value.

These annotations help in managing when and under what conditions tests should run, making the test suite more flexible
and adaptable to different environments.
 */


/*
In JUnit 5, **assumptions** are used to conditionally execute tests based on certain conditions or assumptions.
If an assumption fails, the test is skipped (marked as "aborted") rather than failing. This feature is particularly
useful for tests that should only run under specific conditions, such as when certain environment variables are set,
or when a particular operating system is being used.

### Key Assumptions in JUnit 5

1. **`Assumptions.assumeTrue`**
   - **Purpose**: The test is aborted if the provided condition is `false`.
   - **Parameters**:
     - A boolean condition.
     - Optionally, a string message that will be printed if the assumption fails.

   **Example**:
   ```java
   import org.junit.jupiter.api.Assumptions;
   import org.junit.jupiter.api.Test;

   public class AssumptionsTest {

       @Test
       void testOnlyOnWindows() {
           Assumptions.assumeTrue(System.getProperty("os.name").startsWith("Windows"), "Test skipped: Not on Windows");
           System.out.println("This test runs only on Windows");
       }
   }
   ```

   In this example, the test will only execute if the operating system is Windows. Otherwise, it will be skipped.

2. **`Assumptions.assumeFalse`**
   - **Purpose**: The test is aborted if the provided condition is `true`.
   - **Parameters**:
     - A boolean condition.
     - Optionally, a string message that will be printed if the assumption fails.

   **Example**:
   ```java
   import org.junit.jupiter.api.Assumptions;
   import org.junit.jupiter.api.Test;

   public class AssumptionsTest {

       @Test
       void testNotOnMac() {
           Assumptions.assumeFalse(System.getProperty("os.name").startsWith("Mac"), "Test skipped: Running on Mac");
           System.out.println("This test runs only if not on Mac");
       }
   }
   ```

   In this example, the test will be skipped if the operating system is Mac.

3. **`Assumptions.assumingThat`**
   - **Purpose**: Allows a specific block of code to be executed only if a certain condition is `true`. The rest of
   the test will continue to run regardless of the assumption.
   - **Parameters**:
     - A boolean condition.
     - A lambda expression or a block of code to execute if the condition is `true`.

   **Example**:
   ```java
   import org.junit.jupiter.api.Assumptions;
   import org.junit.jupiter.api.Test;

   public class AssumptionsTest {

       @Test
       void testWithAssumption() {
           Assumptions.assumingThat(
               "CI".equals(System.getenv("ENV")),
               () -> {
                   System.out.println("This code runs only if the ENV variable is set to CI");
                   // Additional CI-specific assertions
               });

           System.out.println("This code runs regardless of the ENV variable");
       }
   }
   ```

   In this example, the block of code inside `assumingThat` will only run if the `ENV` environment variable is set to
   `"CI"`. However, the rest of the test will continue to execute regardless of the assumption.

### Why Use Assumptions?

- **Selective Test Execution**: Assumptions allow you to run certain tests only when specific conditions are met,
making your test suite more adaptable to different environments.
- **Avoiding Failures**: If a test requires certain conditions to be met and they aren’t, the test will be skipped
rather than failing. This prevents false negatives in your test results.
- **Environment-Specific Testing**: Assumptions are particularly useful in scenarios where certain tests should only
run in specific environments (e.g., certain OS, JRE version, or environment variables).

### Example Scenario

Consider a test that should only run if a database connection is available:

```java
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

public class DatabaseTest {

    @Test
    void testOnlyIfDatabaseAvailable() {
        boolean isDatabaseAvailable = checkDatabaseConnection();
        Assumptions.assumeTrue(isDatabaseAvailable, "Database not available, skipping test");

        // Database-specific test logic here
        System.out.println("Running test because database is available");
    }

    boolean checkDatabaseConnection() {
        // Simulate a database connection check
        return false; // Assume the database is unavailable
    }
}
```

In this example, the test will be skipped if the `checkDatabaseConnection()` method returns `false`.

### Summary

- **Assumptions in JUnit 5** allow you to conditionally skip tests based on certain conditions.
- Use `assumeTrue` to skip a test if a condition is false, `assumeFalse` to skip if a condition is true, and
`assumingThat` to conditionally execute specific code blocks within a test.
- Assumptions help make your tests more flexible and prevent unnecessary test failures due to unmet conditions.
 */
package io.github.yashar256.looper;

import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.function.Function;


public class ScannerTestCase<T> {
    private static ArrayList<ScannerTestCase<?>> testCases = new ArrayList<>();

    private String mockUserInput;
    private T expectedResult;
    private Function<MenuLooper.Scanner, T> scannerTest;

    private ScannerTestCase(String mockUserInput, T expectedResult, Function<MenuLooper.Scanner, T> scannerTest) {
        this.mockUserInput = mockUserInput;
        this.expectedResult = expectedResult;
        this.scannerTest = scannerTest;
    }

    static <T> void createNewTest(String mockUserInput, T expectedResult, Function<MenuLooper.Scanner, T> scannerTest) {
        testCases.add(new ScannerTestCase<T>(mockUserInput, expectedResult, scannerTest));
    }

    static void runAllTests(MenuLooper.Scanner scanner) {
        // Creates a mock System.in InputStream which supplies expected user input
        StringBuilder mockUserInput = new StringBuilder();
        for (ScannerTestCase<?> testCase : testCases) {
            mockUserInput.append(testCase.getMockUserInput()).append(System.lineSeparator());
        }

        InputStream realSystemIn = System.in;
        System.setIn(new ByteArrayInputStream(mockUserInput.toString().getBytes()));

        for (ScannerTestCase<?> testCase : testCases) {
            Assertions.assertEquals(testCase.getExpectedResult(), testCase.runTest(scanner));
        }

        System.setIn(realSystemIn);
    }

    T runTest(MenuLooper.Scanner scanner) {
        return scannerTest.apply(scanner);
    }

    String getMockUserInput() {
        return mockUserInput;
    }

    T getExpectedResult() {
        return expectedResult;
    }
}

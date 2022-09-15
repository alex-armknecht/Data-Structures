package test.util.financeutils;

import static main.util.financeutils.FinanceUtils.*;
import static org.junit.Assert.*;

import org.junit.*;
import org.junit.rules.Timeout;
import org.junit.runner.Description;
import org.junit.rules.TestWatcher;

public class FinanceUtilsTests {
    
    // =================================================
    // Test Configuration
    // =================================================
    
    // Global timeout to prevent infinite loops from
    // crashing the test suite
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1);
    
    // Each time you pass a test, you get a point! Yay!
    // [!] Requires JUnit 4+ to run
    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        protected void succeeded(Description description) {
            passed++;
        }
    };
    
    // Grade record-keeping
    static int possible = 0, passed = 0;
    
    // The @Before mmethod is run before every @Test
    @Before
    public void init () {
        possible++;
    }
    
    // Used for grading, reports the total number of tests
    // passed over the total possible
    @AfterClass
    public static void gradeReport () {
        System.out.println("============================");
        System.out.println("Tests Complete");
        System.out.println(passed + " / " + possible + " passed!");
        if ((1.0 * passed / possible) >= 0.9) {
            System.out.println("[!] Nice job!"); // Automated acclaim!
        }
        System.out.println("============================");
    }
    
    
    // =================================================
    // Unit Tests
    // =================================================
    // For grading purposes, every method and unit is 
    // weighted equally and totaled for the score.
    // The tests increase in difficulty such that the
    // basics are unlabeled and harder tiers are tagged
    // t0, t1, t2, t3, ... easier -> harder
    //
    // Typically, you cluster tests for each method
    // by particular types of inputs, e.g., empty String
    // inputs vs. single words vs. multiple words (the
    // "zero-one-infinity" rule of testing) or tests for
    // proper error handling. Some of the below may be
    // organized for grading purposes instead.
    
    
    // getEvenRedistribution Tests
    // -------------------------------------------------
    
    @Test
    public void getEvenRedistribution_t0() {
        assertArrayEquals(new int[] {2, 2, 2}, getEvenRedistribution(new int[] {3, 2, 1}));
        assertArrayEquals(new int[] {2, 2, 2}, getEvenRedistribution(new int[] {2, 2, 2}));
        assertArrayEquals(new int[] {2, 2, 2}, getEvenRedistribution(new int[] {1, 2, 3}));
    }
    
    @Test
    public void getEvenRedistribution_t1() {
        assertArrayEquals(new int[] {2, 2, 2, 3}, getEvenRedistribution(new int[] {3, 2, 1, 3}));
        assertArrayEquals(new int[] {2, 2, 3, 3}, getEvenRedistribution(new int[] {3, 2, 2, 3}));
        assertArrayEquals(new int[] {2, 3, 3, 3}, getEvenRedistribution(new int[] {4, 2, 2, 3}));
        assertArrayEquals(new int[] {3, 3, 3, 3}, getEvenRedistribution(new int[] {4, 3, 2, 3}));
    }
    
    @Test
    public void getEvenRedistribution_t2() {
    	assertThrows(IllegalArgumentException.class, ()->getEvenRedistribution(new int[] {-3, -2, -1}));
    }
    // greedyChangemaker Tests
    // -------------------------------------------------
    
    @Test
    public void greedyChangemaker_t0() {
        assertArrayEquals(new int[] {1, 0, 0, 0}, greedyChangemaker(1));
        assertArrayEquals(new int[] {0, 1, 0, 0}, greedyChangemaker(5));
        assertArrayEquals(new int[] {0, 0, 1, 0}, greedyChangemaker(10));
        assertArrayEquals(new int[] {0, 0, 0, 1}, greedyChangemaker(25));
    }
    
    @Test
    public void greedyChangemaker_t1() {
        assertArrayEquals(new int[] {3, 0, 0, 0}, greedyChangemaker(3));
        assertArrayEquals(new int[] {0, 0, 2, 0}, greedyChangemaker(20));
    }
    
    @Test
    public void greedyChangemaker_t2() {
        assertArrayEquals(new int[] {0, 1, 1, 0}, greedyChangemaker(15));
        assertArrayEquals(new int[] {4, 0, 1, 1}, greedyChangemaker(39));
    }
    
    @Test
    public void greedyChangemaker_t3() {
    	assertThrows(IllegalArgumentException.class, ()->greedyChangemaker(-5));
    }
}


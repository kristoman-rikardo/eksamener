package part2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestInstance;
// import org.mockito.internal.runners.JUnit44RunnerImpl;

import sandbox.JavaSetupCheck;

public class DecreasingStackTest {

    private DecreasingStack s1;
    private DecreasingStack s2;
    private DecreasingStack s3; 

    @BeforeEach
    public void setUp() {
        s1 = new DecreasingStack(9);
        s2 = new DecreasingStack(10);
        s3 = new DecreasingStack(11);
    }

    @Test
    public void emptyTest() {
        s1.pop();
        assertTrue(s1.isEmpty());
    }

    @Test
    public void pushTest() {
        s1.push(8);
        s2.push(8);
        s3.push(8);
        assertEquals(s2.peek(), 8);
    }

    public 

    @Test
	void testSample() {
		assertEquals("Gratulerer, Java-oppsettet ditt fungerer!", JavaSetupCheck.helloWorld());
	}
    
}

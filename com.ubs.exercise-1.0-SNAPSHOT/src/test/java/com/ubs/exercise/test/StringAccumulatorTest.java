package com.ubs.exercise.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.ubs.exercise.StringAccumulator;

public class StringAccumulatorTest {

	@Before
	public void setUp() throws Exception {}

	@After
	public void tearDown() throws Exception {}

	@Test
	public void testEmptyNumberString() {
		// Empty number string must sum to zero
		assertEquals(StringAccumulator.add(""), 0);
	}

	@Test
	public void testSingleNumberString() {
		// A single number must sum to itself
		assertEquals(StringAccumulator.add("1"), 1);		
		assertEquals(StringAccumulator.add("100"), 100);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testWordString() {
		// Number count is not greater than delimiter count
		// Word String cannot be parse to numbers
		StringAccumulator.add("abc");
	}
	
	@Test
	public void testNumbersWithDefaultDelimiter() {
		assertEquals(StringAccumulator.add("1\n2,3"), 6);
		assertEquals(StringAccumulator.add("1,2,3,4"), 10);
		assertEquals(StringAccumulator.add("1\n2\n3\n4"), 10);
		assertEquals(StringAccumulator.add("1,2,3\n4"), 10);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testDelimitersWithoutNumbers() {
		StringAccumulator.add(",\n");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNumbersAreLessThanDelimiterCount() {
		StringAccumulator.add("1,\n");
	}

	@Test
	public void testNumbersBiggerThan1000AreIgnored() {
		assertEquals(StringAccumulator.add("1,1001"), 1);
		assertEquals(StringAccumulator.add("1\n1000,10001"), 1001);
		assertEquals(StringAccumulator.add("1000\n2000\n3000\n4000"), 1000);
		assertEquals(StringAccumulator.add("1001\n1002\n1003"), 0);
	}
	
	public void testNumbersWithCustomDelimiter() {
		assertEquals(StringAccumulator.add("//;\n1;2"), 3);
		assertEquals(StringAccumulator.add("//***\n1***2***3"), 6);
		assertEquals(StringAccumulator.add("//*|%\n1*2%3"), 6);		
		assertEquals(StringAccumulator.add("//+\n1+2+3+4"), 10);
		assertEquals(StringAccumulator.add("//+|*\n1+2+3*4"), 10);
		assertEquals(StringAccumulator.add("//+++|***\n1+++2+++3***4"), 10);
		assertEquals(StringAccumulator.add("//+*%\n1+*%2+*%3+*%4"), 10);		
	}


	@Test(expected=Exception.class)
	public void testNumbersWithDelimiterButBadFormat() {
		StringAccumulator.add("//+*%\n1+*%2+*%3*%4");
	}

	@Rule
	public ExpectedException negNumException = ExpectedException.none();
	public void testNegativeNumbers() {
		negNumException.expect(IllegalArgumentException.class);
		negNumException.expectMessage("Negative not allowed: " + -1);
		StringAccumulator.add("-1");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNegativeNumbersWithDelimiter() {
		StringAccumulator.add("//*\n1*2*-1");
	}
	
}

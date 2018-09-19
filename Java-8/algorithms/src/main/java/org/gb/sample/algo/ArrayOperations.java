package org.gb.sample.algo;

public class ArrayOperations {

	public static int[] rotate(int[] numbers, int positions) {
		int[] rotatedNumbers = new int[numbers.length];
		int srcPos = numbers.length - positions;
		System.arraycopy(numbers, srcPos, rotatedNumbers, 0, positions);
		System.arraycopy(numbers, 0, rotatedNumbers, positions, numbers.length - positions);
		return rotatedNumbers;
	}
}

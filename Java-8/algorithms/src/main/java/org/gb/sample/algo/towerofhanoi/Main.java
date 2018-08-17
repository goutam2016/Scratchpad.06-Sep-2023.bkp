package org.gb.sample.algo.towerofhanoi;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

	public static void main(String[] args) {
		Deque<String> stack = new LinkedBlockingDeque<>(5);
		stack.offerFirst("1");
		stack.offerFirst("2");
		stack.offerFirst("3");
		stack.offerFirst("4");
		stack.offerFirst("5");
		//stack.clear();
		//stack.offerFirst("F");
		//stack.offerFirst("G");

		String item = "";

		/*while (item != null) {
			item = stack.pollFirst();
			System.out.println(item + ", size: " + stack.size());
		}*/
		List<String> elements = new ArrayList<>();
		elements.add("A");
		elements.add("B");
		elements.add("C");
		elements.add("D");
		elements.add("E");
		System.out.println(String.format("items: %s", stack.stream().collect(Collectors.toList())));
		System.out.println(Stream.iterate(0, i -> i+1).limit(10).collect(Collectors.toList()));
	}

}

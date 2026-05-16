package part2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EmptyStackException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DecreasingStacks is a class that manages an ordered list of {@link DecreasingStack} instances
 * None of the stacks are allowed to be empty.
 */
public class DecreasingStacks {

	private List<DecreasingStack> stacks = new ArrayList<>();
	
	/**
	 * @return true if all stacks (if any) are empty
	 */
	public boolean isEmpty() {
		if (this.stacks.isEmpty()) return true; // ambigous if empty should return true or false
		return this.stacks.stream().filter(s -> s.isEmpty()).count() == this.stacks.size(); // if all elements are empyt, the filtered list is as long as before
	}

	/**
	 * Pushes the provided element onto the first stack that accepts it.
	 * If there are no such stacks, add a new DecreasingStack to end of stack list,
	 * that is initialized with the element.
	 *
	 * @param element the element to push
	 */
	public void push(final int element) {
		for (DecreasingStack stack : this.stacks) {
			if (stack.push(element)) return; // we're done if we push the element
		}
		this.stacks.add(new DecreasingStack(element));
	}

	/**
	 * @return newline-separated string of stacks
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		this.stacks.stream().forEach(s -> sb.append(s.toString() + "\n"));
		return sb.toString();
	}

	/**
	 * Remove and return the smallest element across all stacks.
	 *
	 * @return (and remove) the element from the stacks that is smalles
	 * @throws an appropriate subclass of RuntimeException if no element can be popped
	 */
	public int pop() {
		if (isEmpty()) throw new EmptyStackException();
		return this.stacks.stream() // make a stream of the 2d list
		.filter(s -> !s.isEmpty()) // just because the stacks is not empty does not mean every stack is not
		.min(Comparator.comparingInt(DecreasingStack::peek)) // finding smallest element, only need to check the upper ones
		.orElseThrow(EmptyStackException::new) // throw exception if anything is wrong
		.pop(); // pop the actual found element
	}

	/**
	 * @return a List with the elements in increasing order.
	 * If there are no elements, return an empty list.
	 * The elements are also removed from this DecreasingStacks.
	 */
	public List<Integer> popAll() {
		List<Integer> list = this.stacks.stream()
		.flatMap(DecreasingStack::stream)
		.sorted()
		.collect(Collectors.toList()); // made a homemade stream for the stack for it to work
		this.stacks.removeAll(this.stacks);
		return list;
	}

	// for your own use

	public static void main(final String[] args) {
		final DecreasingStacks stacks = new DecreasingStacks();
		List.of(5,3,8,2,1,4,4,7,6).forEach(stacks::push);
		System.out.println(stacks.toString());
		// Should print
		// [5, 3, 2, 1]
		// [8, 4]
		// [4]
		// [7, 6]

		System.out.println(stacks.popAll());
		// Should print
		// [1, 2, 3, 4, 4, 5, 6, 7, 8]
		System.out.println(stacks.popAll());
	}
}


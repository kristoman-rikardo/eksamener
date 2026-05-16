package part1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a certain order of post that must be visited.
 * The posts corresponds to a sequence of legs.
 * Leg N will be from post N to post N + 1
 */
public class Route implements Iterable<Post> {
	private final List<Post> posts;

	/**
	 * Initializes this route with the provided posts.
	 *
	 * @param posts the posts in-order
	 */
	public Route(final Iterator<Post> posts) {
		if (posts == null) throw new IllegalArgumentException();
		this.posts = new ArrayList<>();
		posts.forEachRemaining(this.posts::add);
	}

	/**
	 * Initializes this route with the provided posts.
	 *
	 * @param posts the posts in-order
	 */
	public Route(final Iterable<Post> posts) {
		if (posts == null) throw new IllegalArgumentException();
		this.posts = new ArrayList<>();
		for (Post p : posts) this.posts.add(p);
	}

	/**
	 * Initializes this route with the provided posts.
	 *
	 * @param posts the posts in-order
	 */
	public Route(final Collection<Post> posts) {
		if (posts == null) throw new IllegalArgumentException();
		this.posts = new ArrayList<>(posts);
	}

	/**
	 * Gets the number of legs.
	 *
	 * @return the number of legs
	 */
	public int getLegCount() {
		return posts.size() - 1;
	}

	/**
	 * Gets the specific leg in the sequence of legs.
	 *
	 * @param num the number in the sequence
	 * @return the leg with the specified number
	 */
	public Leg getLeg(final int num) {
		if (num < 0 || num >= posts.size()) throw new IllegalArgumentException("Leg not within the route");
		return new Leg(posts.get(num), posts.get(num + 1));
	}

	/**
	 * Gets an iterator that returns the posts in sequence.
	 */
	@Override
	public Iterator<Post> iterator() {
		return new ArrayList<>(posts).iterator();
	}

	/**
	 * Computes the total distance of this route, as the sum of the leg distances.
	 *
	 * @return the total distance of this route
	 */
	public double distance() {
		return distance(this);
	}

	/**
	 * Computes the sum of the distances between the provided posts,
	 * if visited in-order.
	 *
	 * @return the total distance of this sequence of posts
	 */
	public static double distance(final Iterable<Post> posts) {
		return distance(posts.iterator());
	}

	/**
	 * Computes the sum of the distances between the provided posts,
	 * if visited in-order.
	 *
	 * @return the total distance of this sequence of posts
	 */
	public static double distance(final Iterator<Post> posts) {
		double distance = 0.0;
		if (!posts.hasNext()) return distance;
		Post former = posts.next();
		while (posts.hasNext()) {
			Post next = posts.next();
			distance += former.distance(next);
			former = next;
		}
		return distance;
	}

	public boolean containsPost(Post post) {
		if (post == null) return false;
		return (this.posts.contains(post));
	}

	public Post getPost(int i) {
		if (i < 0 || i >= this.posts.size()) throw new IllegalArgumentException();
		return this.posts.get(i);
	}
}

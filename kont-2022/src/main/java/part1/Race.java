package part1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javafx.geometry.Pos;

public class Race implements Iterable<Post> {
	private Post startPost;
	private Post finishPost;
	private List<Post> posts = new ArrayList<>();
	// TODO: necessary fields and initialisation

	/**
	 * Initializes this race with the provided start and finish post.
	 *
	 * @param startPost
	 * @param finishPost
	 * @throws IllegalArgumentException if one or both of the arguments are null
	 */
	public Race(final Post startPost, final Post finishPost) {
		if (startPost == null || finishPost == null) throw new IllegalArgumentException();
		this.startPost = startPost;
		this.finishPost = finishPost;
		this.posts.addAll(List.of(startPost, finishPost));
	}

	/**
	 * Initializes this race with start and finish post being the same post.
	 *
	 * @param startFinishPost
	 * @throws IllegalArgumentException if the argument is null
	 */
	public Race(final Post startFinishPost) {
		if (startPost == null || finishPost == null) throw new IllegalArgumentException();
		this.startPost = startFinishPost;
		this.finishPost = startFinishPost;
		this.posts.addAll(List.of(startPost, finishPost));
	}

	/**
	 * @return the start post
	 */
	public Post getStartPost() {
		return this.startPost;
	}

	/**
	 * @return the finish post
	 */
	public Post getFinishPost() {
		return this.finishPost;
	}

	/**
	 * @return the number of posts, including start and finish post
	 */
	public int getPostCount() {
		return this.posts.size();
	}

	/**
	 * @param num
	 * @return the post with index num, start post is index 0 and finish post comes last
	 */
	public Post getPost(final int num) {
		if (num < 0 || num >= getPostCount()) throw new NullPointerException();
		return this.posts.get(num);
	}

	/**
	 * @return all the posts in the order of index, i.e. start first, finish last
	 */
	public Post[] getPosts() {
		Post[] postArray = this.posts.toArray(new Post[0]);
		return postArray;
	}

	/**
	 * @return an iterator that returns all the posts in index order
	 */
	@Override
	public Iterator<Post> iterator() {
		return this.posts.iterator();
	}

	/**
	 * Removes the provided post.
	 *
	 * @param post
	 * @throws IllegalArgumentException if this provided post isn't one of this race's intermediate posts
	 */
	public void removePost(final Post post) {
		if (post == null || this.posts.indexOf(post) < 1 || this.posts.indexOf(post) >= getPostCount() - 1) throw new IllegalArgumentException();
		this.posts.remove(post);
	}

	/**
	 * Finds all posts in this race within a certain distance from the provided reference point (coordinate pair)
	 *
	 * @param east the east coordinate of the reference point
	 * @param north the north coordinate of the reference point
	 * @param distance the maximum distance for the returned points
	 * @return an iterator that returns all posts within a certain distance from the reference point
	 */
	public Iterator<Post> findPostsNearby(final double east, final double north, final double distance) {
		return this.posts.stream().filter(p -> p.distance(east, north) <= distance).collect(Collectors.toList()).iterator();
	}

	/**
	 * The smallest allowed distance between two posts in a race.
	 */
	public final static double distanceEpsilon = 20.0;

	/**
	 * Adds a post at a specific point given by east and north coordinates
	 *
	 * @param east
	 * @param north
	 * @return the added post
	 * @throws IllegalArgumentException if the post is too close to another post (see distanceEpsilon)
	 */
	public Post addPost(final double east, final double north) {
		if (findPostsNearby(east, north, Race.distanceEpsilon).hasNext()) throw new IllegalArgumentException();
		Post post = new Post(east, north);
		this.posts.add(getPostCount() - 1, post); // list initiated with to elems, no risk for Index OOB
		return post;
	}

	/**
	 * Sets the post numbers to the corresponding index.
	 * I.e. if you add three posts, start will have number 0,
	 * the first added will have postNum=1, the second postNum=2,
	 * the third postNum=3 and the finish post postNum=4.
	 */
	public void assignPostNums() {
		for (int i = 0; i < getPostCount(); i++) {
			getPost(i).setPostNum(i);
		}
	}

	//

	public static RouteFactory getMaxDistanceRouteFactory(final double maxDistance) {
		return new MaxDistanceRouteFactory(maxDistance);
	}

	// for own testing

	public static void main(final String[] args) {
		final var start = new Post(0.0, 0.0);
		final var finish = new Post(3.0, 4.0);
		final var race = new Race(start, finish);
		race.addPost(15.0, 42.0);
		race.assignPostNums();
		// should print three posts, with the one at 0.0,0.0 first,
		// then the one at 15.0,42.0 and finally the one at 3.0,4.0
		System.out.println(List.of(race.getPosts()));
	}
}

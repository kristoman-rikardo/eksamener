package part1;

/**
 * Represents the part of a race going from one post to another.
 * One of them can be the starting or ending post of the race.
 */
public class Leg {
	private Post startPost;
	private Post endPost;
	// TODO: necessary fields and initialisation

	/**
	 * Initializes this Leg with the provided start and end post.
	 *
	 * @param startPost the starting post
	 * @param endPost the ending post
	 */
	public Leg(final Post startPost, final Post endPost) {
		if (startPost == null || endPost == null) throw new IllegalArgumentException();
		this.startPost = startPost;
		this.endPost = endPost;
	}

	/**
	 * Returns the string representation of this leg.
	 * Includes the number of the start and end posts, as well as the distance between them.
	 */
	@Override
	public String toString() {
		if (this.startPost.getPostNum() == -1 && this.endPost.getPostNum() == -1) 
			return "No postNums assigned. Distance: " + this.startPost.distance(endPost) + " meters.";
		else if (this.startPost.getPostNum() == -1) 
			return "Start no postNum assigned. endPost: " + this.endPost.getPostNum() + ". Distance: " + this.startPost.distance(endPost) + " meters.";
		else if (this.endPost.getPostNum() == -1) 
			return "End no postNum assigned. startPost: " + this.startPost.getPostNum() + ". Distance: " + this.startPost.distance(endPost) + " meters.";
		else {
			return "endPost: " + endPost.getPostNum() + ". startPost: " + this.startPost.getPostNum() + ". Distance: " + this.startPost.distance(endPost) + " meters.";
		}
	}

	/**
	 * @return the starting Post of this leg
	 */
	public Post getStartPost() {
		return this.startPost;
	}

	/**
	 * @return the end post of this leg
	 */
	public Post getEndPost() {
		return this.endPost;
	}

	// TODO: methods including getters og setters, that you find necessary and useful

	/**
	 * Computes the distance between the start and end posts.
	 *
	 * @return the distance between the start and end posts
	 */
	public double distance() {
		return this.startPost.distance(this.endPost);
	}
}

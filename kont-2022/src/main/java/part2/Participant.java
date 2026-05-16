package part2;

import part1.Post;
import part1.Route;

/**
 * Represents a race participant running a certain route
 * through the race's posts.
 */
public class Participant {

	// TODO: necessary fields and initialisation
	private String name;
	private Route route;
	private int currentPost;
	/**
	 * Initializes this participant with the provided name.
	 *
	 * @param name
	 */
	public Participant(final String name) {
		if (name == null) throw new IllegalArgumentException();
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setRoute(final Route route) {
		if (route == null) throw new IllegalArgumentException();
		if (this.route != null) throw new IllegalStateException(); // route already set
		this.route = route;
	}

	public Route getRoute() {
		return this.route;
	}

	public boolean isFinished() {
		return (currentPost >= this.route.getLegCount());
	}

	public void setProgression(int progression) {
		if (progression < 0 || isFinished() || this.currentPost + progression > this.route.getLegCount()) throw new IllegalArgumentException();
		if (!isFinished()) this.currentPost += progression;
	}

	public Post getCurrentPost() {
		return this.route.getLeg(currentPost).getStartPost();
	}

	public int getCurrentProgression() {
		return this.currentPost;
	}

	

	// TODO: methods including getters og setters, that you find necessary and useful
}

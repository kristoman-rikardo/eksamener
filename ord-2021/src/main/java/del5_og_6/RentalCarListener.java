package del5_og_6;

import java.util.HashMap;
import java.util.Map;

/* RentalCar listeners listens to changes in Status for all userNames.*/
public class RentalCarListener implements StatusListener {
	private Map<String, Integer> discounts = new HashMap<>();
	// TODO - Add any needed fields here

	@Override
	/**
	 * Method that should be called when a given userName has updated its status.
	 */
	public void statusChanged(String username, String oldStatus, String newStatus) {
		if (newStatus.equals("Gold")) { // only need to check if new status is gold, and set to 0 if not
			this.discounts.put(username, 100);
		}
		else {
			this.discounts.put(username, 0); // override and set to 0, if the new status is not Gold
		}
	}

	/**
	 * Get's the discount of a user. Should be a 100 if the user currently has Gold
	 * status, otherwise should be 0.
	 * 
	 * @param username The username of the user
	 * 
	 * @return The discount the user qualifies for.
	 */
	public int getDiscount(String username) {
		if (username == null || !this.discounts.containsKey(username)) return 0;
		return this.discounts.get(username);
	}
}

package del1;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VaccineTrial {
	private List<VaccineTrialVolunteer> volunteers = new ArrayList<>();
	// Add any needed fields here

	/**
	 * Adds a new VaccineTrialVolunteer to the trial
	 * 
	 * @param id      The id of the volunteer
	 * 
	 * @param placebo Whether the volunteer was given a placebo, or the actual
	 *                vaccine
	 */
	public void addVolunteer(String id, boolean placebo) { 
		VaccineTrialVolunteer volunteer = new VaccineTrialVolunteer(id, placebo); // the constructur of the volunteer does the error check
		if (this.volunteers.stream().anyMatch(v -> v.getId().equals(id))) throw new IllegalStateException("Volunteer already added");
		this.volunteers.add(volunteer);
	}

	/**
	 * Returns whether the vaccine's effectiveness rate is higher than the provided
	 * limit. The effectiveness of the vaccine is calculated as follows: 
	 * 
	 * 1- (number of people that received the vaccine and got sick/
	 *         number of people that got sick)
	 * 
	 * If there is no sick people, the vaccine is not effective
	 * 
	 * @param limit A limit to compare against
	 * 
	 * @throws IllegalArgumentException If limit is not between (including) 0 and 1.
	 * 
	 * @return Whether the vaccine effectiveness rate is higher than the limit
	 */
	public boolean isMoreEffectiveThanLimit(double limit) {
		if (limit < 0 || limit > 1) throw new IllegalArgumentException();
		double gotSickAll = (double) this.volunteers.stream().filter(v -> v.gotSick()).count();
		double gotSickVaccine = (double) this.volunteers.stream().filter(v -> !v.isPlacebo()).filter(v -> v.gotSick()).count();
		double effectivnes = 1 - (gotSickVaccine / gotSickAll);
		return effectivnes > limit;
	}

	/**
	 * Updates the sick state of a VaccineTrialVolunteer
	 * 
	 * @param id The id of the volunteer to set sick.
	 * @throws IllegalArgumentException if there is no volunteer with the given id
	 */
	public void setSick(String id) { // assume setSick only means to set the patient to become sick, not the other way around
		if (getVolunteer(id) == null) throw new IllegalArgumentException();
		getVolunteer(id).setGotSick(true);
	}

	/**
	 * Get's the volunteer with the given ID
	 * 
	 * @param id The id of the volunteer to set sick.
	 * 
	 * @return The vaccine trial volunteer with the given ID. If the ID is not valid
	 *         for any volunteer, return null
	 */
	public VaccineTrialVolunteer getVolunteer(String id) {
		Optional<VaccineTrialVolunteer> volunteer = this.volunteers.stream().filter(v -> v.getId().equals(id)).findAny();
		if (volunteer.isEmpty()) return null;
		return volunteer.get();
	}

	public static void main(String[] args) {
		VaccineTrial trial = new VaccineTrial();
		trial.addVolunteer("1", false);
		trial.addVolunteer("2", false);
		trial.addVolunteer("3", true);
		trial.addVolunteer("4", true);
		trial.setSick("4");
		// Should now be true
		System.out.println(trial.isMoreEffectiveThanLimit(0.5));

	}
}

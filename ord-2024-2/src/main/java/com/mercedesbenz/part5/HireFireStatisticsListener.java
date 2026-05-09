package com.mercedesbenz.part5;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.tdt4100.part2.Person;
import no.ntnu.tdt4100.part2.Role;
import no.ntnu.tdt4100.part5.EmploymentStatus;
import no.ntnu.tdt4100.part5.IEmployementListener;

/**
 * This class should implement the {@link IEmployementListener} interface
 * @see HireFireStatisticsListenerTests
 */
public class HireFireStatisticsListener implements IEmployementListener {
    private List<Role> hires = new ArrayList<>();
    /**
     * Returns the number of hired people for the given role since this listener was added.
     * 
     * Note that the same person can be hired multiple times, each time will contribute to this count.
     * 
     * @param role the {@link Role}
     * @return the number of hired people for the given role
     */
    public int getNumberOfHiredPeople(Role role) {
        if (hires == null) return 0;
        return this.hires.stream().filter(r -> r.equals(role)).toList().size();
    }

       /**
     * Triggers when a person changes employement status.
     * If person is <code>null</code>, does nothing.
     * 
     * Note that this method can be invoked multiple times for the same person,
     * i.e. when the person is hired, fired, rehired, refired, etc...
     * 
     * @param person the person
     * @param employmentStatus the employement status
     */
    public void trigger(Person person, EmploymentStatus employmentStatus) {
        if (person == null) return;
        if (employmentStatus == EmploymentStatus.HIRED) this.hires.add(person.role());
    }
}

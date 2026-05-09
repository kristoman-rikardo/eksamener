package com.mercedesbenz.part3;

import java.time.ZonedDateTime;

import no.ntnu.tdt4100.part3.AssemblyStep;

/**
 * Welding the chassis is a production step in the production line.
 * 
 * <p>Implement this class according to the behavioural requirements below.</p>
 * 
 * Behavioural requirements:
 * <ul>
 * <li>This class must be a subclass of the {@link no.ntnu.tdt4100.part3.AssemblyStep} class.</li>
 * <li>It must be possible to instantiate the class outside its defined package</li>
 * <li>The isAutomated() method in its superclass should return <code>true</code></li>
 * <li>Its constructor should only have 1 parameter - stepId of type {@link String}</li>
 * <li>its assemble() method must obey the rules set out in the base class</li>
 * <li>the getStepDuration() method of its superclass should return a Duration object with
 * at least 100ms after its execute() method has been invoked</li>
 * 
 * @see no.ntnu.tdt4100.part3.AssemblyStep
 * @see no.ntnu.tdt4100.part3.AssemblyStep#isAutomated()
 * @see no.ntnu.tdt4100.part3.AssemblyStep#assemble()
 * @see no.ntnu.tdt4100.part3.AssemblyStep#simulateTimePassing()
 */
// TODO Implement the AssemblePistonsStep class here according to JavaDoc   
public class AssemblePistonsStep extends AssemblyStep {

    public AssemblePistonsStep(String stepId) {
        super(stepId, true);
    }

    /**
     * This method represents the assembly.
     * When assemble is called, the assembly step is actually executed in the assembly line.
     * Examples of assembly steps can be - Assemble engine block, assemble piston, 
     * assemble crankshaft, assemble timing belt, etc.
     * 
     * When the assembly starts, the startTime must be set to {@link java.time.ZonedDateTime#now()}.
     * When the assembly execution is finished, its endTime must also be set.
     * 
     * When this is done, the factory can create statistics how long each step take to execute.
     * Ie can installation of a motor take an hour when there are many different steps involved.
     * 
     * Behavioural requirements after this method is called
     * <ul>
     * <li>The assembly step should have a set start time and an end time according to the above description.</li>
     * <li>The endTime can not be set in the future</li>
     * <li>Every assembly step takes at least 100ms to execute. Therefore, the start time must be before the end time by at least 100ms. Tip: 
     * Use {@link AssemblyStep#simulateTimePassing()} in the implementation to simulate that performing the step takes some time.</li>
     * </ul>
     * 
     * @see AssemblyStep#startTime
     * @see AssemblyStep#endTime
     * @see AssemblyStep#simulateTimePassing()
     */

    public void assemble() {
        super.startTime = ZonedDateTime.now();
        simulateTimePassing();
        super.endTime = ZonedDateTime.now();
    }
 
}

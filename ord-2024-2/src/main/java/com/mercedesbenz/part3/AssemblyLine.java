package com.mercedesbenz.part3;

import no.ntnu.tdt4100.part3.AssemblyStep;

/**
 * Represents a motor assembly line.
 * 
 * A motor is assembled in a specific sequence. 
 * The assembly line consists of a sequence of assembly steps
 * that are chained to the first assembly step.
 * -----------------------       ---------       ---------
 * | firstAssemblyStep   | ----> | step2 | ----> | step3 | ----> ...
 * -----------------------       ---------       --------- 
 * 
 * @see AssemblyLine#firstAssemblyStep
 * @see AssemblyLineTests
 */
public class AssemblyLine {
    // Do not modify the below field. No additional fields needs to be added
    AssemblyStep firstAssemblyStep; // TODO Use this field to implement a chain of steps

    /**
     * Adds a assembly step as a successor to the last step of the assembly line.
     * 
     * Behavioural requirements: 
     * <ul>
     * <li>If the assembly line is empty, the step is added as the first step.
     * Otherwise, the step is added as the successor of the last step in the assembly line.</li>
     * <li>It should not be possible to add a step already added
     * ie it is not possible to add a step with the same step ID twice</li>
     * 
     * @param step the assembly step to add
     * @throws IllegalArgumentException if the step is already added or the step is <code>null</code>
     * 
     * @see AssemblyLine#firstAssemblyStep
     * @see AssemblyStep#setNext(AssemblyStep)
     * @see AssemblyLine#isStepAdded(AssemblyStep)
     */
    public void addAssemblyStep(AssemblyStep step) {
        if (step == null || isStepAdded(step)) throw new IllegalArgumentException();
        if (firstAssemblyStep == null) {
            this.firstAssemblyStep = step;
        }
        else {
            AssemblyStep thisStep = this.firstAssemblyStep; // start from the first of the list
            while (thisStep.getNext() != null) { // walk our way down to the last, stop at the end
                thisStep = thisStep.getNext();
            }
            thisStep.setNext(step); // add to the end, where the former element were null
        }
        // TODO Implement the method to behave according to JavaDoc
    }

    /**
     * Checks if a assembly step is already added to the assembly line.
     * The step is uniquely identified by its step ID.
     * 
     * @param step
     * @return true if the step is already added, false otherwise
     * 
     * @see AssemblyStep#getStepId()
     * @see AssemblyStep#equals(Object)
     */
    public boolean isStepAdded(AssemblyStep step) {
        if (step == null) throw new IllegalArgumentException();
        if (this.firstAssemblyStep == null) return false; // if i\not instantiated yet
        if (this.firstAssemblyStep.equals(step)) return true;
        AssemblyStep thisStep = this.firstAssemblyStep; // start from the first of the list
        while (thisStep.getNext() != null) { // walk our way down to the last, stop at the end
            thisStep = thisStep.getNext(); // already checked first elem, so we can skip to second
            if (thisStep.equals(step)) return true;
        }
        return false; // been through the entire list with no duplicates
        // TODO Implement the method to behave according to JavaDoc
    }


    /**
     * Execute all assembly steps in the assembly line sequentially.
     *
     * @see AssemblyStep#assemble()
     */
    public void assembleAll() {
        AssemblyStep thisStep = this.firstAssemblyStep; // start from the first of the list
        while (thisStep.getNext() != null) { // walk our way down to the last, stop at the end
            thisStep.assemble();
            thisStep = thisStep.getNext(); // skip to nest
        }
        thisStep.assemble(); // assemble last one
        // TODO Implement the method to behave according to JavaDoc
    }
}

package com.bmw.manufacturing.part3;

import no.ntnu.tdt4100.part3.ProductionStep;

/**
 * Represents a car production line.
 * 
 * A production line consists of a sequence of production steps
 * that are chained to the first production step.
 * -----------------------       ---------       ---------
 * | firstProductionStep | ----> | step2 | ----> | step3 | ----> ...
 * -----------------------       ---------       --------- 
 * 
 * @see ProductionLine#firstProductionStep
 * @see ProductionLineTests
 */
@SuppressWarnings("unused")
public class ProductionLine {
    private ProductionStep firstProductionStep; // TODO Use this field to implement a chain of steps
    // No additional fields needs to be added

    /**
     * Adds a production step as a successor to the last step of the production line.
     * 
     * Behavioural requirements: 
     * <ul>
     * <li>If the production line is empty, the step is added as the first step.
     * Otherwise, the step is added as the successor of the last step in the production line.</li>
     * <li>It should not be possible to add a step already added
     * ie it is not possible to add a step with the same step ID twice</li>
     * 
     * @param step the production step to add
     * @throws IllegalArgumentException if the step is already added
     * 
     * @see ProductionLine#firstProductionStep
     * @see ProductionStep#setSuccessor(ProductionStep)
     * @see ProductionLine#isStepAdded(ProductionStep)
     */
    public void addProductionStep(ProductionStep step) {
        if (step == null || isStepAdded(step)) throw new IllegalArgumentException(); // if step is null or already added
        if (firstProductionStep == null) this.firstProductionStep = step; // if there is not any steps, we add our step as the first step
        else {
            ProductionStep thisStep = firstProductionStep; 
            while (thisStep.getSuccessor() != null) { // iterate until we're at the end of the linked list
                thisStep = thisStep.getSuccessor(); // move one step further down
            }
            thisStep.setSuccessor(step); // set the step as successor of the formerly last step
        }
    }

    /**
     * Checks if a production step is already added to the production line.
     * The step is uniquely identified by its step ID.
     * 
     * @param step
     * @return true if the step is already added, false otherwise
     * 
     * @see ProductionStep#getStepId()
     * @see ProductionStep#equals(Object)
     */
    boolean isStepAdded(ProductionStep step) {
        ProductionStep thisStep = this.firstProductionStep;
        while (thisStep != null) { // while we're not at the end of the list
            if (thisStep.equals(step)) {
                return true; // return if our current step is equal to the one we're checking for
            }
            thisStep = thisStep.getSuccessor(); // go to next step
        }
        return false; // if we did not find an equal entry, it is not added yet
    }

    /**
     * Executes all production steps in the production line sequentially.
     *
     * @see ProductionStep#execute()
     */
    public void executeAll() {
        ProductionStep thisStep = this.firstProductionStep;
        while (thisStep != null) {
            thisStep.execute();
            thisStep = thisStep.getSuccessor(); // go to next step
        }
    }
}

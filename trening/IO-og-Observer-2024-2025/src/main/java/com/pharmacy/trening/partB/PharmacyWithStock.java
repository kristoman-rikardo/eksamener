package com.pharmacy.trening.partB;

import java.util.List;

import no.ntnu.tdt4100.pharmacy.AlreadyDispensedException;
import no.ntnu.tdt4100.pharmacy.DispensableUnit;
import no.ntnu.tdt4100.pharmacy.IHasStockManager;
import no.ntnu.tdt4100.pharmacy.IPharmacy;
import no.ntnu.tdt4100.pharmacy.IStockManager;
import no.ntnu.tdt4100.pharmacy.InsufficientStockException;
import no.ntnu.tdt4100.pharmacy.Medication;

// TODO: Add any necessary imports here

/**
 * A pharmacy that wraps a delegate {@link IPharmacy} and adds inventory management
 * via an {@link IStockManager}.
 *
 * <p>This class uses the <em>delegation</em> pattern: it implements {@link IPharmacy}
 * by forwarding the relevant method calls to the wrapped delegate, while adding
 * dispensing logic that interacts with the stock manager.</p>
 *
 * @see IPharmacy
 * @see IHasStockManager
 * @see PharmacyWithStockTests
 */
public class PharmacyWithStock implements IPharmacy, IHasStockManager {

    // TODO: Add necessary fields here

    /**
     * Constructs a {@code PharmacyWithStock}.
     *
     * @param delegate     the underlying pharmacy whose methods are delegated to;
     *                     must not be {@code null}
     * @param stockManager the stock manager for this pharmacy; must not be {@code null}
     * @throws IllegalArgumentException if either argument is {@code null}
     */
    public PharmacyWithStock(IPharmacy delegate, IStockManager stockManager) {
        // TODO: Implement constructor
    }

    /**
     * Returns the ISO country code of this pharmacy.
     * This method must delegate to the underlying {@link IPharmacy#getIsoCode()}.
     *
     * @return the ISO country code
     */
    @Override
    public String getIsoCode() {
        return null; // dummy return
    }

    /**
     * Returns the list of medications licensed at this pharmacy.
     * This method must delegate to the underlying {@link IPharmacy#getMedications()}.
     *
     * @return the list of medications
     */
    @Override
    public List<Medication> getMedications() {
        return null; // dummy return
    }

    /**
     * Returns the number of staff at this pharmacy.
     * This method must delegate to the underlying {@link IPharmacy#getNumberOfStaff()}.
     *
     * @return the number of staff
     */
    @Override
    public int getNumberOfStaff() {
        return 0; // dummy return
    }

    /**
     * Returns the number of prescription units successfully dispensed at
     * <em>this</em> pharmacy since it was created.
     *
     * @return the number of dispensed units
     */
    @Override
    public int getNumberOfDispensed() {
        return 0; // dummy return
    }

    /**
     * Returns the {@link IStockManager} for this pharmacy.
     *
     * @return the stock manager
     */
    @Override
    public IStockManager getStockManager() {
        return null; // dummy return
    }

    /**
     * Attempts to dispense the given prescription unit.
     *
     * <p>Before dispensing, the following checks are performed in order:</p>
     * <ol>
     *   <li>Whether the prescription identified by {@link DispensableUnit#prescriptionId()}
     *       has already been dispensed at <em>this</em> pharmacy. If so, throw
     *       {@link AlreadyDispensedException}.</li>
     *   <li>Whether the stock manager holds sufficient quantity of every medication
     *       required by the unit. If any medication has insufficient stock,
     *       throw {@link InsufficientStockException} for that medication.
     *       <strong>No stock must be decreased</strong> if this check fails.</li>
     * </ol>
     *
     * <p>If both checks pass, the available quantity of each required medication
     * is decreased in the stock manager, the prescription ID is recorded locally,
     * and the dispensed count is incremented by one.</p>
     *
     * @param unit the prescription unit to dispense; assumed non-null
     * @throws AlreadyDispensedException  if the prescription has already been
     *                                    dispensed at this pharmacy
     * @throws InsufficientStockException if any required medication has insufficient stock
     *
     * @see DispensableUnit#prescriptionId()
     * @see DispensableUnit#medications()
     * @see IStockManager#getAvailableStock(Medication)
     * @see IStockManager#decreaseStock(Medication, int)
     */
    public void dispenseUnit(DispensableUnit unit) {
        // TODO: Implement according to JavaDoc
    }

    /**
     * Processes an incoming shipment of a medication by increasing the stock.
     * If {@code quantity} is zero or negative, this method does nothing.
     *
     * @param medication the medication that arrived
     * @param quantity   the number of units in the shipment
     *
     * @see IStockManager#increaseStock(Medication, int)
     */
    public void receiveShipment(Medication medication, int quantity) {
        // TODO: Implement according to JavaDoc
    }
}

// FASIT — PharmacyWithStock
// Pakk dette i package com.pharmacy.trening.partB; for å kompilere

package com.pharmacy.trening.partB;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import no.ntnu.tdt4100.pharmacy.AlreadyDispensedException;
import no.ntnu.tdt4100.pharmacy.DispensableUnit;
import no.ntnu.tdt4100.pharmacy.IHasStockManager;
import no.ntnu.tdt4100.pharmacy.IPharmacy;
import no.ntnu.tdt4100.pharmacy.IStockManager;
import no.ntnu.tdt4100.pharmacy.InsufficientStockException;
import no.ntnu.tdt4100.pharmacy.Medication;

public class PharmacyWithStock implements IPharmacy, IHasStockManager {

    private final IPharmacy    delegate;
    private final IStockManager stockManager;
    private final Set<String>  dispensedIds = new HashSet<>();
    private int                dispensedCount = 0;

    public PharmacyWithStock(IPharmacy delegate, IStockManager stockManager) {
        if (delegate == null || stockManager == null)
            throw new IllegalArgumentException("Arguments must not be null");
        this.delegate     = delegate;
        this.stockManager = stockManager;
    }

    @Override public String getIsoCode()            { return delegate.getIsoCode(); }
    @Override public List<Medication> getMedications() { return delegate.getMedications(); }
    @Override public int getNumberOfStaff()         { return delegate.getNumberOfStaff(); }
    @Override public int getNumberOfDispensed()     { return dispensedCount; }
    @Override public IStockManager getStockManager(){ return stockManager; }

    public void dispenseUnit(DispensableUnit unit) {
        // Check 1: already dispensed at THIS pharmacy
        if (dispensedIds.contains(unit.prescriptionId())) {
            throw new AlreadyDispensedException(unit,
                    "Prescription " + unit.prescriptionId() + " already dispensed");
        }

        // Check 2: validate ALL medications BEFORE touching any stock (two-pass)
        for (Map.Entry<Medication, Integer> entry : unit.medications().entrySet()) {
            int available = stockManager.getAvailableStock(entry.getKey());
            if (available < entry.getValue()) {
                throw new InsufficientStockException(entry.getKey(),
                        "Insufficient stock for " + entry.getKey().name());
            }
        }

        // Both checks passed — decrease stock for all medications
        for (Map.Entry<Medication, Integer> entry : unit.medications().entrySet()) {
            stockManager.decreaseStock(entry.getKey(), entry.getValue());
        }

        dispensedIds.add(unit.prescriptionId());
        dispensedCount++;
    }

    public void receiveShipment(Medication medication, int quantity) {
        if (quantity <= 0) return;
        stockManager.increaseStock(medication, quantity);
    }
}

/*
 * FELLER:
 *
 * 1. Tofase-validering (two-pass) er kritisk.
 *    Sjekk ALLE medikamenter før du kaller decreaseStock på noen av dem.
 *    Uten dette: hvis medication A har nok men medication B ikke har det,
 *    vil A allerede ha blitt redusert når B kaster InsufficientStockException.
 *    Det bryter atomisitetsgarantien JavaDoc beskriver.
 *    Se test: dispenseUnit_does_not_decrease_any_stock_when_one_medication_is_insufficient.
 *
 * 2. dispensedIds er et HashSet<String> over prescriptionId — sporer lokalt.
 *    Viktig: sporing er PER DETTE APOTEKET, ikke globalt.
 *    Se ord-2024-1 CarFactoryWithInventory — samme mønster.
 *
 * 3. dispensedCount og dispensedIds oppdateres kun ved suksess.
 *    Ikke oppdater dem før begge sjekkene er bestått.
 *
 * 4. receiveShipment: if (quantity <= 0) return; guard er viktig.
 *    Negativ shipment skal ikke redusere lager.
 */

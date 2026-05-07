// FASIT — AutoRefillListener
// Pakk dette i package com.pharmacy.trening.partB; for å kompilere

package com.pharmacy.trening.partB;

import java.util.HashMap;
import java.util.Map;

import no.ntnu.tdt4100.pharmacy.IStockListener;
import no.ntnu.tdt4100.pharmacy.ISupplierService;
import no.ntnu.tdt4100.pharmacy.Medication;

public class AutoRefillListener implements IStockListener {

    private final ISupplierService    supplierService;
    private final Map<Medication, Integer> orderedCounts = new HashMap<>();

    public AutoRefillListener(ISupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Override
    public void execute(Medication medication, int availableQuantity) {
        if (availableQuantity < 5
                && medication.name().toLowerCase().contains("injection")) {
            supplierService.sendOrder(medication, 20);
            orderedCounts.merge(medication, 20, Integer::sum); // accumulate
        }
    }

    public int getOrderedCount(Medication medication) {
        return orderedCounts.getOrDefault(medication, 0);
    }
}

/*
 * FELLER:
 *
 * 1. Case-insensitivitet: bruk .toLowerCase().contains("injection"), ikke bare .contains().
 *    Medikamentnavnet "Insulin INJECTION" skal også trigge ordren.
 *
 * 2. Terskelen er STRENGT MINDRE ENN 5 (< 5), ikke <= 5.
 *    Stock=5 skal IKKE utløse ordre. Vanlig off-by-one-feil.
 *
 * 3. getOrderedCount bruker getOrDefault(medication, 0) — aldri null.
 *    Uten dette: NPE ved unboxing hvis medication aldri er bestilt.
 *
 * 4. orderedCounts.merge(medication, 20, Integer::sum) akkumulerer riktig.
 *    Alternativt: orderedCounts.put(medication, getOrderedCount(medication) + 20);
 *    Begge er korrekte; merge() er mer idiomatisk Java.
 */

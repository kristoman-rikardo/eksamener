// FASIT — DrugStockManager
// Pakk dette i package com.pharmacy.trening.partB; for å kompilere

package com.pharmacy.trening.partB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ntnu.tdt4100.pharmacy.IStockListener;
import no.ntnu.tdt4100.pharmacy.IStockManager;
import no.ntnu.tdt4100.pharmacy.InsufficientStockException;
import no.ntnu.tdt4100.pharmacy.Medication;

public class DrugStockManager implements IStockManager {

    private Map<Medication, Integer> stock     = new HashMap<>();
    private List<IStockListener>     listeners = new ArrayList<>();

    @Override
    public void increaseStock(Medication medication, int quantity) {
        if (quantity <= 0) return;
        stock.merge(medication, quantity, Integer::sum); // elegant Map merge
    }

    @Override
    public void decreaseStock(Medication medication, int quantity) {
        int current = getAvailableStock(medication); // returns 0 for unknown key
        if (current < quantity) {
            throw new InsufficientStockException(medication,
                    "Insufficient stock for " + medication.name());
        }
        stock.put(medication, current - quantity);
        notifyListeners(medication, current - quantity); // notify AFTER update
    }

    private void notifyListeners(Medication medication, int newQuantity) {
        for (IStockListener listener : listeners) {
            listener.execute(medication, newQuantity);
        }
    }

    @Override
    public void addListener(IStockListener listener) {
        if (listeners.contains(listener)) return; // no duplicates
        listeners.add(listener);
    }

    @Override
    public void removeListener(IStockListener listener) {
        listeners.remove(listener); // no-op if not present
    }

    @Override
    public int getAvailableStock(Medication medication) {
        return stock.getOrDefault(medication, 0); // 0 for unknown — never NPE
    }

    @Override
    public int getListenerCount() {
        return listeners.size();
    }
}

/*
 * FELLER:
 *
 * 1. getAvailableStock: stock.get(medication) returnerer null for ukjent nøkkel.
 *    Auto-unboxing av null → NullPointerException. Bruk getOrDefault(key, 0).
 *    Se ord-2024-1 WarehouseInventoryManager — samme feil var til stede.
 *
 * 2. decreaseStock: kast InsufficientStockException FØR du endrer map-verdien.
 *    Kast unntaket, ikke decreaser og deretter kast. Rekkefølgen er kritisk.
 *
 * 3. notifyListeners kalles ETTER oppdateringen. Lytteren skal se den nye verdien,
 *    ikke den gamle.
 *
 * 4. addListener: sjekk for duplikater med contains(). Uten dette kan samme lytter
 *    trigge to ganger per endring.
 *
 * 5. Map.merge(key, value, Integer::sum) er en elegant måte å håndtere
 *    "sett ny verdi, eller legg til eksisterende" — alternativt:
 *    stock.put(medication, stock.getOrDefault(medication, 0) + quantity);
 */

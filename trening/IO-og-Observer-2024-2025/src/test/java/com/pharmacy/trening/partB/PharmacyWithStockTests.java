package com.pharmacy.trening.partB;

import no.ntnu.tdt4100.pharmacy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PharmacyWithStockTests {

    Medication paracetamol;
    Medication insulin;

    IPharmacy delegate;
    DrugStockManager stockManager;
    PharmacyWithStock pharmacy;

    @BeforeEach
    void setup() {
        paracetamol = new Medication(1, "Paracetamol", "GSK", 500.0);
        insulin     = new Medication(4, "Insulin injection", "NovoNordisk", 100.0);

        delegate = new IPharmacy() {
            public String getIsoCode()            { return "NOR"; }
            public List<Medication> getMedications() { return List.of(paracetamol, insulin); }
            public int getNumberOfStaff()         { return 12; }
            public int getNumberOfDispensed()     { return 0; }
        };

        stockManager = new DrugStockManager();
        pharmacy = new PharmacyWithStock(delegate, stockManager);
    }

    // --- constructor ---

    @Test
    void constructor_throws_for_null_delegate() {
        assertThrows(IllegalArgumentException.class,
                () -> new PharmacyWithStock(null, stockManager));
    }

    @Test
    void constructor_throws_for_null_stockManager() {
        assertThrows(IllegalArgumentException.class,
                () -> new PharmacyWithStock(delegate, null));
    }

    // --- delegation ---

    @Test
    void getIsoCode_delegates_to_delegate() {
        assertEquals("NOR", pharmacy.getIsoCode());
    }

    @Test
    void getMedications_delegates_to_delegate() {
        assertEquals(2, pharmacy.getMedications().size());
    }

    @Test
    void getNumberOfStaff_delegates_to_delegate() {
        assertEquals(12, pharmacy.getNumberOfStaff());
    }

    @Test
    void getStockManager_returns_the_stock_manager() {
        assertSame(stockManager, pharmacy.getStockManager());
    }

    // --- dispenseUnit ---

    @Test
    void dispenseUnit_decreases_stock_for_all_medications_in_unit() {
        stockManager.increaseStock(paracetamol, 10);
        stockManager.increaseStock(insulin, 5);

        DispensableUnit unit = new DispensableUnit("RX-001",
                Map.of(paracetamol, 2, insulin, 1));
        pharmacy.dispenseUnit(unit);

        assertEquals(8, stockManager.getAvailableStock(paracetamol));
        assertEquals(4, stockManager.getAvailableStock(insulin));
    }

    @Test
    void dispenseUnit_increments_dispensed_count() {
        stockManager.increaseStock(paracetamol, 10);
        DispensableUnit unit = new DispensableUnit("RX-001", Map.of(paracetamol, 1));
        pharmacy.dispenseUnit(unit);
        assertEquals(1, pharmacy.getNumberOfDispensed());
    }

    @Test
    void dispenseUnit_throws_AlreadyDispensedException_for_duplicate_prescription() {
        stockManager.increaseStock(paracetamol, 20);
        DispensableUnit unit = new DispensableUnit("RX-001", Map.of(paracetamol, 1));
        pharmacy.dispenseUnit(unit);
        assertThrows(AlreadyDispensedException.class, () -> pharmacy.dispenseUnit(unit));
    }

    @Test
    void dispenseUnit_throws_InsufficientStockException_when_stock_too_low() {
        stockManager.increaseStock(paracetamol, 1);
        DispensableUnit unit = new DispensableUnit("RX-002", Map.of(paracetamol, 5));
        assertThrows(InsufficientStockException.class, () -> pharmacy.dispenseUnit(unit));
    }

    @Test
    void dispenseUnit_does_not_decrease_any_stock_when_one_medication_is_insufficient() {
        // paracetamol has plenty, insulin has too little
        stockManager.increaseStock(paracetamol, 10);
        stockManager.increaseStock(insulin, 1);

        DispensableUnit unit = new DispensableUnit("RX-003",
                Map.of(paracetamol, 2, insulin, 5));
        try {
            pharmacy.dispenseUnit(unit);
        } catch (InsufficientStockException ignored) {}

        // Neither stock should have been touched
        assertEquals(10, stockManager.getAvailableStock(paracetamol));
        assertEquals(1,  stockManager.getAvailableStock(insulin));
    }

    @Test
    void dispenseUnit_does_not_increment_count_on_failure() {
        stockManager.increaseStock(paracetamol, 0);
        DispensableUnit unit = new DispensableUnit("RX-004", Map.of(paracetamol, 5));
        try {
            pharmacy.dispenseUnit(unit);
        } catch (InsufficientStockException ignored) {}
        assertEquals(0, pharmacy.getNumberOfDispensed());
    }

    // --- receiveShipment ---

    @Test
    void receiveShipment_increases_stock() {
        pharmacy.receiveShipment(paracetamol, 20);
        assertEquals(20, stockManager.getAvailableStock(paracetamol));
    }

    @Test
    void receiveShipment_does_nothing_for_zero_quantity() {
        pharmacy.receiveShipment(paracetamol, 0);
        assertEquals(0, stockManager.getAvailableStock(paracetamol));
    }

    @Test
    void receiveShipment_does_nothing_for_negative_quantity() {
        pharmacy.receiveShipment(paracetamol, -10);
        assertEquals(0, stockManager.getAvailableStock(paracetamol));
    }
}

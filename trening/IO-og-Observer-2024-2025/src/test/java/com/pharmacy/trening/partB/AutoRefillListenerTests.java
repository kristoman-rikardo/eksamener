package com.pharmacy.trening.partB;

import no.ntnu.tdt4100.pharmacy.ISupplierService;
import no.ntnu.tdt4100.pharmacy.Medication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AutoRefillListenerTests {

    Medication insulin;          // name contains "injection"
    Medication warfarin;         // name contains "injection"
    Medication paracetamol;      // name does NOT contain "injection"

    // Simple supplier that records calls
    List<String> ordersPlaced;
    ISupplierService trackingService;

    @BeforeEach
    void setup() {
        insulin      = new Medication(4, "Insulin injection", "NovoNordisk", 100.0);
        warfarin     = new Medication(8, "Warfarin injection", "Bayer", 5.0);
        paracetamol  = new Medication(1, "Paracetamol", "GSK", 500.0);

        ordersPlaced = new ArrayList<>();
        trackingService = (med, qty) -> ordersPlaced.add(med.name() + ":" + qty);
    }

    @Test
    void execute_places_order_when_injection_stock_below_5() {
        AutoRefillListener listener = new AutoRefillListener(trackingService);
        listener.execute(insulin, 4); // 4 < 5 → should order
        assertEquals(1, ordersPlaced.size());
        assertEquals("Insulin injection:20", ordersPlaced.get(0));
    }

    @Test
    void execute_does_not_order_when_stock_equals_5() {
        AutoRefillListener listener = new AutoRefillListener(trackingService);
        listener.execute(insulin, 5); // 5 is NOT less than 5
        assertTrue(ordersPlaced.isEmpty());
    }

    @Test
    void execute_does_not_order_when_stock_above_5() {
        AutoRefillListener listener = new AutoRefillListener(trackingService);
        listener.execute(insulin, 10);
        assertTrue(ordersPlaced.isEmpty());
    }

    @Test
    void execute_does_not_order_for_non_injection_medication() {
        AutoRefillListener listener = new AutoRefillListener(trackingService);
        listener.execute(paracetamol, 0); // stock=0 but name has no "injection"
        assertTrue(ordersPlaced.isEmpty());
    }

    @Test
    void execute_is_case_insensitive_for_injection_check() {
        Medication mixedCase = new Medication(99, "Vitamin B12 INJECTION", "Pharma", 50.0);
        AutoRefillListener listener = new AutoRefillListener(trackingService);
        listener.execute(mixedCase, 3);
        assertEquals(1, ordersPlaced.size());
    }

    @Test
    void execute_orders_exactly_20_units_per_trigger() {
        AutoRefillListener listener = new AutoRefillListener(trackingService);
        listener.execute(insulin, 2);
        assertTrue(ordersPlaced.get(0).endsWith(":20"));
    }

    @Test
    void getOrderedCount_returns_zero_for_never_ordered_medication() {
        AutoRefillListener listener = new AutoRefillListener(trackingService);
        assertEquals(0, listener.getOrderedCount(insulin));
    }

    @Test
    void getOrderedCount_returns_20_after_single_order() {
        AutoRefillListener listener = new AutoRefillListener(trackingService);
        listener.execute(insulin, 4);
        assertEquals(20, listener.getOrderedCount(insulin));
    }

    @Test
    void getOrderedCount_accumulates_across_multiple_triggers() {
        AutoRefillListener listener = new AutoRefillListener(trackingService);
        listener.execute(insulin, 4); // triggers
        listener.execute(insulin, 3); // triggers again
        listener.execute(insulin, 5); // does NOT trigger (stock == 5)
        assertEquals(40, listener.getOrderedCount(insulin));
    }

    @Test
    void getOrderedCount_is_independent_per_medication() {
        AutoRefillListener listener = new AutoRefillListener(trackingService);
        listener.execute(insulin, 2);
        listener.execute(warfarin, 1);
        assertEquals(20, listener.getOrderedCount(insulin));
        assertEquals(20, listener.getOrderedCount(warfarin));
    }
}

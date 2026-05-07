package com.pharmacy.trening.partB;

import no.ntnu.tdt4100.pharmacy.IStockListener;
import no.ntnu.tdt4100.pharmacy.InsufficientStockException;
import no.ntnu.tdt4100.pharmacy.Medication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DrugStockManagerTests {

    DrugStockManager manager;
    Medication paracetamol;
    Medication ibuprofen;

    @BeforeEach
    void setup() {
        manager = new DrugStockManager();
        paracetamol = new Medication(1, "Paracetamol", "GSK", 500.0);
        ibuprofen   = new Medication(2, "Ibuprofen", "Nycomed", 400.0);
    }

    // --- increaseStock ---

    @Test
    void increaseStock_adds_to_inventory() {
        manager.increaseStock(paracetamol, 10);
        assertEquals(10, manager.getAvailableStock(paracetamol));
    }

    @Test
    void increaseStock_accumulates_multiple_calls() {
        manager.increaseStock(paracetamol, 10);
        manager.increaseStock(paracetamol, 5);
        assertEquals(15, manager.getAvailableStock(paracetamol));
    }

    @Test
    void increaseStock_does_nothing_for_zero() {
        manager.increaseStock(paracetamol, 10);
        manager.increaseStock(paracetamol, 0);
        assertEquals(10, manager.getAvailableStock(paracetamol));
    }

    @Test
    void increaseStock_does_nothing_for_negative() {
        manager.increaseStock(paracetamol, 10);
        manager.increaseStock(paracetamol, -5);
        assertEquals(10, manager.getAvailableStock(paracetamol));
    }

    // --- decreaseStock ---

    @Test
    void decreaseStock_removes_from_inventory() {
        manager.increaseStock(paracetamol, 10);
        manager.decreaseStock(paracetamol, 3);
        assertEquals(7, manager.getAvailableStock(paracetamol));
    }

    @Test
    void decreaseStock_throws_InsufficientStockException_when_not_enough() {
        manager.increaseStock(paracetamol, 2);
        assertThrows(InsufficientStockException.class,
                () -> manager.decreaseStock(paracetamol, 5));
    }

    @Test
    void decreaseStock_does_not_modify_stock_when_insufficient() {
        manager.increaseStock(paracetamol, 2);
        try {
            manager.decreaseStock(paracetamol, 5);
        } catch (InsufficientStockException ignored) {}
        assertEquals(2, manager.getAvailableStock(paracetamol));
    }

    @Test
    void decreaseStock_notifies_listeners_with_new_quantity() {
        List<Integer> captured = new ArrayList<>();
        manager.addListener((med, qty) -> captured.add(qty));
        manager.increaseStock(paracetamol, 10);
        manager.decreaseStock(paracetamol, 3);
        assertEquals(1, captured.size());
        assertEquals(7, captured.get(0));
    }

    @Test
    void decreaseStock_notifies_after_updating_not_before() {
        List<Integer> captured = new ArrayList<>();
        manager.addListener((med, qty) -> captured.add(manager.getAvailableStock(med)));
        manager.increaseStock(paracetamol, 10);
        manager.decreaseStock(paracetamol, 4);
        // getAvailableStock inside listener should see 6, not 10
        assertEquals(6, captured.get(0));
    }

    // --- addListener / removeListener ---

    @Test
    void addListener_registers_listener() {
        manager.addListener((m, q) -> {});
        assertEquals(1, manager.getListenerCount());
    }

    @Test
    void addListener_does_not_add_duplicate() {
        IStockListener listener = (m, q) -> {};
        manager.addListener(listener);
        manager.addListener(listener);
        assertEquals(1, manager.getListenerCount());
    }

    @Test
    void removeListener_removes_registered_listener() {
        IStockListener listener = (m, q) -> {};
        manager.addListener(listener);
        manager.removeListener(listener);
        assertEquals(0, manager.getListenerCount());
    }

    @Test
    void removeListener_does_nothing_for_unregistered_listener() {
        IStockListener listener = (m, q) -> {};
        assertDoesNotThrow(() -> manager.removeListener(listener));
        assertEquals(0, manager.getListenerCount());
    }

    // --- getAvailableStock ---

    @Test
    void getAvailableStock_returns_zero_for_unknown_medication() {
        assertEquals(0, manager.getAvailableStock(paracetamol));
    }

    @Test
    void getAvailableStock_is_independent_per_medication() {
        manager.increaseStock(paracetamol, 10);
        manager.increaseStock(ibuprofen, 3);
        assertEquals(10, manager.getAvailableStock(paracetamol));
        assertEquals(3, manager.getAvailableStock(ibuprofen));
    }
}

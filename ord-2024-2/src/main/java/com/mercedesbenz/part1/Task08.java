package com.mercedesbenz.part1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.ntnu.tdt4100.part1.AbstractTask08;

public class Task08 extends AbstractTask08 {

    /**
     * Suppose you have a string array of length 10. 
     * Half of the array is populated with the string "FILL", but you don't know which elements.
     * You need to fill the other half with the string "FILL".
     * 
     * Below are some methods.
     * Which method(s) below would you use to fill the remaining half of the array with "FILL"
     */
    public void array_filling() {
        // TODO Uncomment which method(s) fill the remaining half of the array
        
        // A_fillA();
        // B_fillB();
        // C_fillC();
        // D_fillD();
        E_fillE();
        F_fillF();
    }

     public static String[] fillA(String[] arrayToFill) {
        String toFill = "FILL";
        var a = Arrays.asList(arrayToFill);
        a.add(toFill);
        a.add(toFill);
        a.add(toFill);
        a.add(toFill);
        return (String[])a.toArray();
    }

     public static String[] fillB(String[] arrayToFill) {
        String toFill = "FILL";
        arrayToFill[5] = toFill;
        arrayToFill[6] = toFill;
        arrayToFill[7] = toFill;
        arrayToFill[8] = toFill;
        arrayToFill[9] = toFill;

        return arrayToFill;
    }

     public static String[] fillC(String[] arrayToFill) {
        String toFill = "FILL";
        arrayToFill[6] = toFill;
        arrayToFill[7] = toFill;
        arrayToFill[8] = toFill;
        arrayToFill[9] = toFill;
        arrayToFill[10] = toFill;

        return arrayToFill;
    }

     public static String[] fillD(String[] arrayToFill) {
        String toFill = "FILL";
        for(int i = 0; i < arrayToFill.length; i++) {
            if (arrayToFill[i] != null) {
                arrayToFill[i] = toFill;
            }
        }
        return arrayToFill;
    }

     public static String[] fillE(String[] arrayToFill) {
        String toFill = "FILL";
        Arrays.fill(arrayToFill, toFill);
        return arrayToFill;
    }

     public static String[] fillF(String[] arrayToFill) {
        String toFill = "FILL";
        Arrays.setAll(arrayToFill, p -> toFill);
        return arrayToFill;
    }
    
    public static void main(String[] args) {
        // System.out.println(fillA(new String[]{"FILL", null, "FILL", null, "FILL", "FILL", null, "FILL", "3", "null"}));
        // System.out.println(fillB(new String[]{"FILL", null, "FILL", null, "FILL", "FILL", null, "FILL", "3", "null"}).toString());
        // System.out.println(fillC(new String[]{"FILL", null, "FILL", null, "FILL", "FILL", null, "FILL", "3", "null"}));
        // System.out.println(fillD(new String[]{"FILL", null, "FILL", null, "FILL", "FILL", null, "FILL", "3", "null"}).toString());
        System.out.println(new ArrayList<>(List.of(fillD(new String[]{"FILL", null, "FILL", null, "FILL", "FILL", null, "FILL", "3", "null"}))));
        System.out.println(new ArrayList<>(List.of(fillE(new String[]{"FILL", null, "FILL", null, "FILL", "FILL", null, "FILL", "3", "null"}))).toString());
        System.out.println(new ArrayList<>(List.of(fillF(new String[]{"FILL", null, "FILL", null, "FILL", "FILL", null, "FILL", "3", "null"}))).toString());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

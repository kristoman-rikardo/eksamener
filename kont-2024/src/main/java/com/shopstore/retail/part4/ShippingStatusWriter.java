package com.shopstore.retail.part4;

// import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import no.ntnu.tdt4100.IProduct;
import no.ntnu.tdt4100.Shipment;
import no.ntnu.tdt4100.ShipmentUpdate;

public class ShippingStatusWriter {

    /**
     * Writes the details of a {@link no.ntnu.tdt4100.Shipment} to the provided
     * {@link OutputStream}.
     * 
     * The method generates a string representation of the shipment, including its
     * ID, status, contained products, and any number of
     * {@link no.ntnu.tdt4100.ShipmentUpdate} objects contained within.
     * Each attribute is separated by a semicolon, and each section
     * (shipment details, products, updates) is separated by a blank line.
     * The generated string is then written to the provided OutputStream.
     * 
     * The format of the output should be as follows:
     * 
     * <pre>
     *  id;createdDateTime
     *  
     *  updateMessage1;updateDateTime1
     *  updateMessage2;updateDateTime2
     * 
     *  productName1;number in shipment
     *  productName2;number in shipment
     * </pre>
     * 
     * 1;2021-09-01T12:00:00

    Products ordered;2021-09-01T12:00:12
    Products packed;2021-09-02T09:00:00

    Green T-Shirt;3 <product name;number of products in shipment>
    Basic Jeans;2
     * 
     * The file resources/output_example.txt contains an example of how the output
     * should look.
     * 
     * @param shipment     The {@link Shipment} object whose details are to be
     *                     written. You can assume that this object will not be
     *                     null.
     * @param outputStream The {@link OutputStream} to which the shipment details
     *                     are to be written.
     * @throws IOException If an I/O error occurs when writing to the stream.
     * 
     * @see Shipment
     * @see ShipmentUpdate
     * @see IProduct
     * @see OutputStream
     */
    public static void write(Shipment shipment, OutputStream outputStream) throws IOException {
       outputStream.write(buildString(shipment).getBytes());
       outputStream.flush();
    }

    public static String buildString(Shipment shipment) {
        StringBuilder sb = new StringBuilder();
        sb.append(shipment.id() + ";" + shipment.createdDateTime() +"\n\n");
        for (ShipmentUpdate update : shipment.updates()) {
            sb.append(update.updateMessage() + ";" + update.updateDateTime() + "\n");
        }
        for (Map.Entry<IProduct, Integer> entry : shipment.productsContained().entrySet()) {
            sb.append("\n" + entry.getKey().getName() + ";" + entry.getValue());
        }
        return sb.toString();
    }

}

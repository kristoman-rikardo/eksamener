package com.bytebadger.assembly.part5;

// import java.io.FileOutputStream;
// import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

// import com.bytebadger.assembly.part3.CustomComputer;

import no.ntnu.tdt4100.bytebadger.*;

// TODO: Import relevant libraries

public class WriteBuildToFile {

    /**
     * Writes the details of a {@link no.ntnu.tdt4100.bytebadger.ICustomBuild} to the provided
     * {@link OutputStream}.
     * 
     * The method generates a string representation of the custom computer build, including 
     * the customer name and email, a list of computer parts with quantity, name, manufacturer and unit price,
     * and the total price for this build.
     * 
     * Each attribute is separated by a semicolon, and each section
     * (customer info, computer parts, total price) is separated by a blank line.
     * The generated string is then written to the provided OutputStream.
     * 
     * The format of the output should be as follows:
     * 
     * <pre>
     *  customerName;customerEmail
     *  
     *  quantity1;productName1;manufacturer1;unitPrice1
     *  quantity2;productName2;manufacturer2;unitPrice2
     *  quantity3;productName3;manufacturer3;unitPrice3
     * 
     * totalPrice
     * </pre>
     * 
     * The file resources/output_example.txt contains an example of how the output
     * should look.
     * 
     * @param build        The {@link ICustomBuild} object whose details are to be
     *                     written. You can assume that this object will not be
     *                     null.
     * @param outputStream The {@link OutputStream} to which the computer build details
     *                     to be written.
     * @throws IOException If an I/O error occurs when writing to the stream.
     * 
     * @see ICustomBuild
     * @see Customer
     * @see IComputerPart
     * @see ComputerPart
     */

    public static void write(ICustomBuild build, OutputStream outputStream) throws IOException {
        try {
            byte[] outputBytes = buildFile(build).getBytes();
            outputStream.write(outputBytes);
            System.out.println("Successfully wrote to file!");
        }
        catch(IOException e) {
            System.out.println("I/O error occured:" + e);
        }
    }

    public static String buildFile(ICustomBuild build) {
        Customer customer = build.getCustomer();
        String firstLine = customer.name() + ";" + customer.email() + "\n\n"; // two newlines to make the spacing
        StringBuilder sb = new StringBuilder();
        Map<IComputerPart, Integer> parts = build.getParts();
        for (Map.Entry<IComputerPart, Integer> entry : parts.entrySet()) {
            String line = entry.getValue() + ";" + entry.getKey().getProductName() + ";" + entry.getKey().getManufacturer() + ";" + entry.getKey().getPrice() + "\n";
            sb.append(line);
        }
        String lastLine = "\n" + build.getTotalPrice();
        return firstLine + sb.toString() + lastLine;
    }

}

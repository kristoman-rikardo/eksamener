package com.mercedesbenz.part4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

// import com.mercedesbenz.part3.GlobalResearchArchive;

import no.ntnu.tdt4100.part2.Project;
import no.ntnu.tdt4100.part4.LineReadError;
import no.ntnu.tdt4100.part4.ResultSet;

public class ProjectsReader {

    public static final String CSV_SPLIT_REGEX = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

    /**
     * Reads an input stream and produces a result set with a list of unique projects and encountered
     * errors by parsing the stream.
     * The input stream is expected to be a CSV file with the format 
     * as defined in resources/data.csv
     * 
     * Behavioural requirements:
     * <ul>
     * <li>The first line of the file contains header information and should be skipped</li>
     * <li>There might various types of errors in the file, such as missing fields, erronous data etc.</li>
     * <li>If erronous data is encountered (such as empty string), an {@link no.ntnu.tdt4100.part4.LineReadError} 
     * should be added to the result set and the line should be skipped, continuing to read the file from the next line</li>
     * <li>The returned {@link Set} of errors should contain the file line number where the error occured 
     * as key and the Exception message as value</li>
     * <li>There might be duplicates or data in the file, a {@link Set} of unique projects should be returned</li>
     * <li>A projects uniqueness is determined by its {@link no.ntnu.tdt4100.part2.Project#projectId()}</li>
     * <li>If parsed correctly, the returned list should be 294 elements long</li>
     * <li>Each time a duplicate project is encountered, the error message should be "Duplicate project"</li>
     * <li>Some projects have a comma in their names and are quoted with "". 
     * An example of this is seen on line 27 in the data.csv.
     * 
     * There should be no quotes for the project name in the result, 
     * i.e "Programmable motivating,Graphical User Interface" should be Programmable motivating,Graphical User Interface
     * in the result without quotes. You may use the supplied CSV_SPLIT_REGEX to achieve this.
     * 
     * <li>Projects that have commas in their names are quoted with "" in the CSV (example on line 27). 
     * The {@link no.ntnu.tdt4100.part2.Project#name()} in {@link ResultSet} should not contain quotes.
     * You may use the supplied {@link ProjectsReader#CSV_SPLIT_REGEX} to achieve this.</li>
     * </ul>
     * 
     * @param stream the {@link InputStream}
     * @return a {@link no.ntnu.tdt4100.part4.ResultSet} containing a list of unique projects and a map of errors
     * @throws java.io.IOException if an I/O error occurs 
     * 
     * @see String#split(String)
     * @see ProjectsReader#CSV_SPLIT_REGEX
     * @see UUID#fromString(String)
     * @see Double#parseDouble(String)
     * @see ZonedDateTime#parse(CharSequence)
     * @see ProjectsReaderTests
     */
    public static ResultSet readProjects(InputStream stream) throws IOException {
        if (stream == null) throw new IllegalArgumentException();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        Set<Project> projects = new HashSet<>();
        Set<UUID> projectIDs = new HashSet<>();
        Set<LineReadError> errors = new HashSet<>();
        reader.readLine(); // skip header
        int i = 1;
        while ((line = reader.readLine()) != null) {
            i++;
            try {
                String[] parts = line.split(CSV_SPLIT_REGEX); // regex handles the quote issues 
                UUID id = UUID.fromString(parts[0]);
                String name = parts[1].replace('"', ' ').trim();
                Double budgetInMillions = Double.parseDouble(parts[2]);
                LocalDate startDate = LocalDate.parse(parts[3]);
                LocalDate estimatedEndDate = LocalDate.parse(parts[4]);
                if (projectIDs.contains(id)) {
                    errors.add(new LineReadError(i, "Duplicate project"));
                    continue;
                }
                Project project = new Project(id, name, budgetInMillions, startDate, estimatedEndDate);
                projects.add(project);
                projectIDs.add(id); 
            } catch (Exception e) {
                errors.add(new LineReadError(i, e.getMessage()));
            }
        }
        reader.close();
        return new ResultSet(projects, errors);
    }
}

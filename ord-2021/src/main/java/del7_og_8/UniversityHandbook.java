package del7_og_8;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UniversityHandbook {

	private List<Course> courses = new ArrayList<>();

	/**
	 * Reads all the courses from a given input stream. The courses are on this
	 * form: courseName, averageGrade, prerequisite 1, prerequisite 2,
	 * prerequisite 3....
	 * 
	 * See courses.txt in src/main/resources/del7_og_8 for an example file.
	 * 
	 * Calling this method should remove any existing courses from the handbook. // assuming this means clearing the list before adding new inputs
	 * 
	 * A given course can have anything from 0 to unlimited number of prerequisites.
	 * The courses do not necessary come in order. Meaning that a course may appear
	 * in the prerequisite list as a never before seen course. The method should read
	 * in all courses, and set the courseName, averageGrade and prerequisites of all
	 * courses and add the courses to the courses field of this class.
	 * 
	 * A skeleton code to read from file is provided to you but feel free to write
	 * your own code for this.
	 * 
	 * You can assume that all lines from the file will be on the correct format.
	 * 
	 * @param stream InputStream containing the course data
	 */
	public void readFromInputStream(InputStream stream) {
		this.courses.clear();
		try (Scanner scanner = new Scanner(stream)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] details = line.split(",");
				String courseName = details[0];
				double averageGrade = Double.parseDouble(details[1]);
				if (getCourse(courseName) == null) this.courses.add(new Course(courseName, averageGrade)); // add the course we scanned
				else if (getCourse(courseName).getAverageGrade() != averageGrade) getCourse(courseName).setAverageGrade(averageGrade); // if corsename is a prereq added without avg grade
				if (details.length > 2) { // only focus on prerequisits
					List<String> preReqs = new ArrayList<>(List.of(details)).subList(2, details.length);
					for (String preReqName : preReqs) {
						if (getCourse(preReqName) == null) { // never seen this prereq before -> add w/o avg grade and fix in another iteration
							this.courses.add(new Course(preReqName));
						}
						if (!getCourse(courseName).getPrerequisites().contains(getCourse(preReqName))) { // this course does not have the prereq in prereqs
							getCourse(courseName).addPrequisite(getCourse(preReqName));
						}
					}
				}
			}
		}
	}

	/**
	 * Gets the course with the courseName
	 * 
	 * @param courseName The name of the course
	 * 
	 * @return The course with the given name
	 * or null if name could not be found
	 */
	public Course getCourse(String courseName) {
		Optional<Course> courseFound = this.courses.stream().filter(c -> c.getCourseName().equals(courseName)).findAny();
		if (courseFound.isEmpty()) return null;
		return courseFound.get();
	}

	public static void main(String[] args) {
		UniversityHandbook handbook = new UniversityHandbook();
		// Reads inn all the files from the course
		handbook.readFromInputStream(handbook.getClass().getResourceAsStream("courses.txt"));
		System.out.println(handbook.courses);
		System.out.println(handbook.getCourse("TDT4100").getPrerequisites().toString());
		System.out.println(handbook.getCourse("TDT4109").getAverageGrade());
	}

}

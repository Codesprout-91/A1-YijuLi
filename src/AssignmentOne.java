import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Runs a scripted demonstration of the health appointment system.
 *
 * <p>Design note: professionals and booking requests are stored in arrays and
 * processed with loops. Adding another professional type or appointment only
 * requires a new object, not a new block of repeated console code. Keyboard
 * input is intentionally not added because the assessment asks for a scripted
 * main-method demonstration rather than a menu-driven application.</p>
 */
public class AssignmentOne {
    /**
     * Starts the complete appointment-system demonstration.
     *
     * @param args command-line arguments; not used
     */
    public static void main(String[] args) {
        printSection("PART 3 - HEALTH PROFESSIONALS");

        Set<DayOfWeek> workingDays = currentWorkingDays();
        GeneralPractitioner gpOne = new GeneralPractitioner(
                "1001", "Dr Amelia Chen", workingDays, "Room 101");
        GeneralPractitioner gpTwo = new GeneralPractitioner(
                "1002", "Dr Noah Patel", workingDays, "Room 102");
        GeneralPractitioner gpThree = new GeneralPractitioner(
                "1003", "Dr Sofia Rossi", workingDays, "Room 103");
        Nurse nurse = new Nurse(
                "2001", "Emma Wilson", workingDays, "Registered Nurse");
        Dietitian dietitian = new Dietitian(
                "3001", "Olivia Brown", workingDays, "Sports Nutrition");
        Specialist specialist = new Specialist(
                "4001", "Dr Liam Nguyen", workingDays, "Paediatrics");

        // The base type lets the loop process any current or future professional type.
        HealthProfessional[] professionals = {
                gpOne, gpTwo, gpThree, nurse, dietitian, specialist
        };
        displayProfessionals(professionals);

        printSection("POLYMORPHIC PROFESSIONAL BEHAVIOUR");
        for (HealthProfessional professional : professionals) {
            System.out.println("- " + professional.performPrimaryDuty());
        }

        Patient alice = new Patient("Alice Johnson", "0412 345 678");
        Patient ben = new Patient("Ben Carter", "0423 456 789");
        Patient chloe = new Patient("Chloe Martin", "0434 567 890");
        Patient daniel = new Patient("Daniel Kim", "0445 678 901");
        Patient ella = new Patient("Ella Wilson", "0456 789 012");

        AppointmentManager manager = new AppointmentManager();

        printSection("PART 4 - APPOINTMENTS ACROSS PROFESSIONAL TYPES");
        Appointment[] bookingRequests = {
                new Appointment(alice, gpOne, LocalTime.of(9, 0)),
                new Appointment(ben, gpTwo, LocalTime.of(10, 30)),
                new Appointment(chloe, nurse, LocalTime.of(9, 0)),
                new Appointment(daniel, dietitian, LocalTime.of(11, 0)),
                new Appointment(ella, specialist, LocalTime.of(13, 30)),
                new Appointment(alice, gpThree, LocalTime.of(15, 0))
        };

        // A loop makes this easy to extend with more sample bookings.
        for (Appointment booking : bookingRequests) {
            System.out.println(manager.addAppointment(booking));
        }

        printSection("PART 5 - ALL APPOINTMENTS SORTED BY TIME");
        System.out.println(manager.displayAppointments(
                manager.getAllAppointmentsSorted()));

        printSection("FILTER BY HEALTH PROFESSIONAL ID 1001");
        List<Appointment> filteredByProfessional =
                manager.getAppointmentsByProfessional(gpOne.getId());
        System.out.println(manager.displayAppointments(filteredByProfessional));

        printSection("FILTER BY PATIENT PHONE 0412 345 678");
        List<Appointment> filteredByPatient =
                manager.getAppointmentsByPatientPhone(alice.getMobilePhone());
        System.out.println(manager.displayAppointments(filteredByPatient));

        printSection("PART 6 - ERROR CASES");
        Patient duplicatePatient =
                new Patient("Duplicate Request", "0467 890 123");
        System.out.println(manager.addAppointment(new Appointment(
                duplicatePatient,
                gpOne,
                LocalTime.of(9, 0))));
        System.out.println(
                manager.cancelAppointment("9999", LocalTime.of(14, 0)));

        printSection("CANCEL BY PROFESSIONAL ID AND TIME");
        System.out.println(
                manager.cancelAppointment(gpTwo.getId(), LocalTime.of(10, 30)));

        printSection("CANCEL BY PATIENT PHONE");
        System.out.println(
                manager.cancelAppointment(alice.getMobilePhone()));

        printSection("UPDATED APPOINTMENT COLLECTION");
        System.out.println(manager.displayAppointments(
                manager.getAllAppointmentsSorted()));
        System.out.println();
        System.out.println("Active appointments: "
                + manager.getAppointmentCount());
    }

    private static Set<DayOfWeek> currentWorkingDays() {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        // Include today so the scripted demonstration always has valid bookings.
        return EnumSet.of(
                today,
                DayOfWeek.MONDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.FRIDAY);
    }

    private static void displayProfessionals(
            HealthProfessional[] professionals) {
        for (HealthProfessional professional : professionals) {
            System.out.println(professional);
            System.out.println();
        }
    }

    private static void printSection(String title) {
        System.out.println();
        System.out.println("================================================");
        System.out.println(title);
        System.out.println("================================================");
    }
}

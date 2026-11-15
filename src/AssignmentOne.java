import java.util.ArrayList;

/**
 * Runs the scripted demonstration for Assessment 1.
 */
public class AssignmentOne {
    /**
     * Prevents creation of a demonstration utility object.
     */
    private AssignmentOne() {
        // This class is used through its static main method.
    }

    /**
     * Runs the complete appointment-system demonstration.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        ArrayList<String> workingDays = createCurrentWorkingDays();

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

        ArrayList<String> unavailableDays = new ArrayList<String>();
        unavailableDays.add(ClinicSchedule.TUESDAY);
        Specialist unavailableSpecialist = new Specialist(
                "4002",
                "Dr Jordan Lee",
                unavailableDays,
                "Dermatology");

        HealthProfessional[] professionals = {
                gpOne,
                gpTwo,
                gpThree,
                nurse,
                dietitian,
                specialist,
                unavailableSpecialist
        };

        printSection("PART 3 - HEALTH PROFESSIONALS");
        for (int index = 0; index < professionals.length; index++) {
            System.out.println(professionals[index]);
            System.out.println();
        }

        printSection("POLYMORPHIC PROFESSIONAL BEHAVIOUR");
        for (int index = 0; index < professionals.length; index++) {
            System.out.println(
                    "- " + professionals[index].performPrimaryDuty());
        }

        Patient alice = new Patient("Alice Johnson", "0412 345 678");
        Patient ben = new Patient("Ben Carter", "0423 456 789");
        Patient chloe = new Patient("Chloe Martin", "0434 567 890");
        Patient daniel = new Patient("Daniel Kim", "0445 678 901");
        Patient ella = new Patient("Ella Wilson", "0456 789 012");

        AppointmentManager manager = new AppointmentManager();

        printSection("PART 4 AND 5 - ADD APPOINTMENTS");
        Appointment[] bookingRequests = {
                new Appointment(alice, gpOne, new TimeSlot(9, 0)),
                new Appointment(ben, gpTwo, new TimeSlot(10, 30)),
                new Appointment(chloe, nurse, new TimeSlot(9, 0)),
                new Appointment(daniel, dietitian, new TimeSlot(11, 0)),
                new Appointment(ella, specialist, new TimeSlot(13, 30)),
                new Appointment(alice, gpThree, new TimeSlot(15, 0))
        };

        for (int index = 0; index < bookingRequests.length; index++) {
            System.out.println(
                    manager.addAppointment(bookingRequests[index]));
        }

        printSection("PART 5 - ALL APPOINTMENTS SORTED BY TIME");
        System.out.println(manager.displayAppointments(
                manager.getAllAppointmentsSorted()));

        printSection("FILTER BY HEALTH PROFESSIONAL ID 1001");
        System.out.println(manager.displayAppointments(
                manager.getAppointmentsByProfessional(gpOne.getId())));

        printSection("FILTER BY PATIENT PHONE 0412 345 678");
        System.out.println(manager.displayAppointments(
                manager.getAppointmentsByPatientPhone(
                        alice.getMobilePhone())));

        printSection("WARNING - FILTER WITH NO MATCHING APPOINTMENTS");
        System.out.println(manager.displayAppointments(
                manager.getAppointmentsByPatientPhone("0499999999")));

        printSection("PART 6 - ERROR CASES");
        Patient duplicatePatient =
                new Patient("Duplicate Request", "0467 890 123");
        System.out.println(manager.addAppointment(
                new Appointment(
                        duplicatePatient,
                        gpOne,
                        new TimeSlot(9, 0))));
        System.out.println(manager.cancelAppointment(
                "9999",
                new TimeSlot(14, 0)));

        printSection("WARNING - PROFESSIONAL DOES NOT WORK TODAY");
        Patient unavailablePatient =
                new Patient("Unavailable Request", "0467 888 123");
        System.out.println(manager.addAppointment(
                new Appointment(
                        unavailablePatient,
                        unavailableSpecialist,
                        new TimeSlot(12, 0))));

        printSection("CANCEL BY PROFESSIONAL ID AND TIME");
        System.out.println(manager.cancelAppointment(
                gpTwo.getId(),
                new TimeSlot(10, 30)));

        printSection("CANCEL BY PATIENT PHONE");
        System.out.println(manager.cancelAppointment(
                daniel.getMobilePhone()));

        printSection("UPDATED APPOINTMENT COLLECTION");
        System.out.println(manager.displayAppointments(
                manager.getAllAppointmentsSorted()));
        System.out.println();
        System.out.println(
                "Active appointments: " + manager.getAppointmentCount());
    }

    private static ArrayList<String> createCurrentWorkingDays() {
        ArrayList<String> days = new ArrayList<String>();
        days.add(ClinicSchedule.getCurrentDay());
        days.add(ClinicSchedule.MONDAY);
        days.add(ClinicSchedule.WEDNESDAY);
        days.add(ClinicSchedule.FRIDAY);
        return days;
    }

    private static void printSection(String title) {
        System.out.println();
        System.out.println("================================================");
        System.out.println(title);
        System.out.println("================================================");
    }
}

import java.util.ArrayList;

/**
 * Verifies the core contracts required by the assessment.
 */
public class A1ContractTest {
    /**
     * Runs all contract checks.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        checksScheduleContract();
        checksProfessionalContract();
        checksPatientAndAppointmentContract();
        checksManagerContract();
        System.out.println("A1 CONTRACT TESTS PASSED");
    }

    private static void checksScheduleContract() {
        ArrayList<TimeSlot> slots = ClinicSchedule.getDailySlots();
        check(slots.size() == 16, "The schedule must contain 16 slots.");
        check(
                slots.get(0).equals(new TimeSlot(8, 0)),
                "The first slot must be 08:00.");
        check(
                slots.get(slots.size() - 1).equals(new TimeSlot(15, 30)),
                "The last bookable slot must be 15:30.");
        check(
                !ClinicSchedule.isScheduledSlot(new TimeSlot(16, 0)),
                "16:00 must be the closing time.");
        check(
                ClinicSchedule.getCurrentDay().equals(ClinicSchedule.MONDAY),
                "The simulated current day must be Monday.");
    }

    private static void checksProfessionalContract() {
        ArrayList<String> days = new ArrayList<String>();
        days.add("Monday");
        GeneralPractitioner gp = new GeneralPractitioner(
                "1001", "Dr Adams", days, "Room 10");
        GeneralPractitioner sameId = new GeneralPractitioner(
                "1001", "Different Name", days, "Room 99");

        check(gp.worksOn("MONDAY"), "Working days must be normalized.");
        check(gp.equals(sameId), "Professional equality must use the ID.");
        check(
                gp.hashCode() == sameId.hashCode(),
                "Equal professionals must have equal hash codes.");
        check(
                gp.getProfessionalType().equals("General Practitioner"),
                "The professional type must be available polymorphically.");
        check(
                gp.toString().contains("Room 10"),
                "Professional output must contain subtype details.");

        ArrayList<String> returnedDays = gp.getAvailableDays();
        returnedDays.add(ClinicSchedule.SUNDAY);
        check(
                !gp.worksOn(ClinicSchedule.SUNDAY),
                "The working-day getter must return a defensive copy.");

        boolean invalidIdRejected = false;
        try {
            new Nurse("N100", "Invalid", "Registered Nurse");
        } catch (IllegalArgumentException exception) {
            invalidIdRejected = true;
        }
        check(invalidIdRejected, "Non-numeric IDs must be rejected.");
    }

    private static void checksPatientAndAppointmentContract() {
        Patient patient = new Patient("Alex Taylor", "0412 345-678");
        check(
                patient.getMobilePhone().equals("0412345678"),
                "The phone number must be normalized.");

        boolean landlineRejected = false;
        try {
            new Patient("Landline", "0212345678");
        } catch (IllegalArgumentException exception) {
            landlineRejected = true;
        }
        check(landlineRejected, "Non-04 mobile numbers must be rejected.");

        ArrayList<String> monday = new ArrayList<String>();
        monday.add(ClinicSchedule.MONDAY);
        GeneralPractitioner gp = new GeneralPractitioner(
                "1001", "Dr Adams", monday, "Room 10");
        Appointment later = new Appointment(
                patient, gp, new TimeSlot(11, 0));
        Appointment earlier = new Appointment(
                new Patient("Sam Lee", "0498765432"),
                gp,
                new TimeSlot(9, 0));
        check(
                earlier.compareTo(later) < 0,
                "Appointments must sort by time.");

        boolean outsideScheduleRejected = false;
        try {
            new Appointment(
                    patient, gp, new TimeSlot(16, 0));
        } catch (IllegalArgumentException exception) {
            outsideScheduleRejected = true;
        }
        check(
                outsideScheduleRejected,
                "Appointment construction must reject 16:00.");
    }

    private static void checksManagerContract() {
        ArrayList<String> monday = new ArrayList<String>();
        monday.add(ClinicSchedule.MONDAY);

        GeneralPractitioner gpOne = new GeneralPractitioner(
                "1001", "Dr Adams", monday, "Room 10");
        GeneralPractitioner gpTwo = new GeneralPractitioner(
                "1002", "Dr Baker", monday, "Room 20");
        Nurse nurse = new Nurse(
                "2001", "Nurse Brown", monday, "Registered Nurse");
        Patient alice = new Patient("Alice", "0412345678");
        Patient ben = new Patient("Ben", "0498765432");

        AppointmentManager manager = new AppointmentManager();
        check(
                manager.displayAppointments(
                        new ArrayList<Appointment>())
                        .equals("WARNING: No appointments found."),
                "Empty appointment results must use WARNING.");

        check(
                manager.addAppointment(
                        new Appointment(alice, gpOne, new TimeSlot(9, 0)))
                        .startsWith("SUCCESS"),
                "A valid appointment must be added.");
        check(
                manager.addAppointment(
                        new Appointment(ben, gpOne, new TimeSlot(9, 0)))
                        .startsWith("WARNING"),
                "Double booking must be rejected.");
        check(
                manager.addAppointment(
                        new Appointment(
                                alice, gpTwo, new TimeSlot(10, 0)))
                        .startsWith("SUCCESS"),
                "A different professional must be available.");
        check(
                manager.addAppointment(
                        new Appointment(
                                ben, nurse, new TimeSlot(11, 0)))
                        .startsWith("SUCCESS"),
                "A nurse appointment must be accepted.");

        ArrayList<Appointment> all =
                manager.getAllAppointmentsSorted();
        check(all.size() == 3, "Three appointments must be stored.");
        check(
                all.get(0).getAppointmentTime().equals(new TimeSlot(9, 0)),
                "All appointments must be sorted by time.");
        check(
                manager.getAppointmentsByProfessional("1001").size() == 1,
                "Professional filtering must work.");
        check(
                manager.getAppointmentsByPatientPhone("0412 345 678").size() == 2,
                "Phone filtering must normalize spaces.");

        check(
                manager.cancelAppointment("1002", new TimeSlot(10, 0))
                        .startsWith("SUCCESS"),
                "ID-and-time cancellation must work.");
        check(
                manager.cancelAppointment("0498765432")
                        .startsWith("SUCCESS"),
                "Phone-only cancellation must work.");
        check(
                manager.cancelAppointment("0498765432")
                        .startsWith("WARNING"),
                "Missing cancellation must return a warning.");

        ArrayList<String> tuesday = new ArrayList<String>();
        tuesday.add(ClinicSchedule.TUESDAY);
        Specialist unavailable = new Specialist(
                "3001",
                "Dr Unavailable",
                tuesday,
                "Dermatology");
        check(
                manager.addAppointment(
                        new Appointment(
                                new Patient("Casey", "0400111222"),
                                unavailable,
                                new TimeSlot(12, 0)))
                        .contains("does not work"),
                "Professionals unavailable today must be rejected.");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}

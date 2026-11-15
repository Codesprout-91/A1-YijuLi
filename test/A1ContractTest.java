import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

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
        checksHealthProfessionalContract();
        checksPatientAndAppointmentContract();
        checksAppointmentManagerContract();
        checksFlexibleScheduleContract();
        System.out.println("A1 CONTRACT TESTS PASSED");
    }

    private static void checksHealthProfessionalContract() {
        Set<DayOfWeek> days = EnumSet.of(
                DayOfWeek.MONDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.FRIDAY);
        GeneralPractitioner gp = new GeneralPractitioner(
                "1001", "Dr Adams", days, "Room 10");
        GeneralPractitioner sameId = new GeneralPractitioner(
                "1001", "Different Name", days, "Room 99");

        check(gp.equals(sameId), "Professional equality must use the ID.");
        check(
                gp.hashCode() == sameId.hashCode(),
                "Equal professionals must have equal hash codes.");
        check(
                gp.getProfessionalType().equals("General Practitioner"),
                "The GP type must be reported.");
        check(
                gp.toString().contains("Room 10"),
                "Professional output must include subtype details.");
        check(
                gp.performPrimaryDuty().contains("prescribes medication"),
                "Polymorphic behaviour must be available through the base type.");

        boolean unmodifiable = false;
        try {
            gp.getAvailableDays().add(DayOfWeek.SUNDAY);
        } catch (UnsupportedOperationException exception) {
            unmodifiable = true;
        }
        check(unmodifiable, "Available days must be read-only.");

        boolean invalidIdRejected = false;
        try {
            new Nurse("N100", "Invalid ID", "Registered Nurse");
        } catch (IllegalArgumentException exception) {
            invalidIdRejected = true;
        }
        check(invalidIdRejected, "Non-numeric IDs must be rejected.");
    }

    private static void checksPatientAndAppointmentContract() {
        Patient patient = new Patient("Alex Taylor", "0412 345-678");
        check(
                patient.getMobilePhone().equals("0412345678"),
                "Phone numbers must be normalized.");

        Set<DayOfWeek> today = EnumSet.of(LocalDate.now().getDayOfWeek());
        Specialist doctor = new Specialist(
                "4001", "Dr Lee", today, "Cardiology");
        Appointment later = new Appointment(
                patient, doctor, LocalTime.of(11, 0));
        Appointment earlier = new Appointment(
                new Patient("Sam Lee", "0498765432"),
                doctor,
                LocalTime.of(9, 0));

        check(
                earlier.compareTo(later) < 0,
                "Appointments must sort from earliest to latest.");
        check(
                later.toString().contains("Alex Taylor")
                        && later.toString().contains("Dr Lee"),
                "Appointment output must include both associated objects.");

        boolean nullRejected = false;
        try {
            new Appointment(null, doctor, LocalTime.of(9, 0));
        } catch (IllegalArgumentException exception) {
            nullRejected = true;
        }
        check(nullRejected, "Appointments without a patient must be rejected.");
    }

    private static void checksAppointmentManagerContract() {
        Set<DayOfWeek> today = EnumSet.of(LocalDate.now().getDayOfWeek());
        GeneralPractitioner gpOne = new GeneralPractitioner(
                "1001", "Dr Adams", today, "Room 10");
        GeneralPractitioner gpTwo = new GeneralPractitioner(
                "1002", "Dr Baker", today, "Room 20");
        Nurse nurse = new Nurse(
                "2001", "Nurse Brown", today, "Registered Nurse");
        Patient alice = new Patient("Alice", "0412345678");
        Patient ben = new Patient("Ben", "0498765432");

        AppointmentManager manager = new AppointmentManager();
        check(
                manager.displayAppointments(
                        new ArrayList<Appointment>())
                        .startsWith("WARNING"),
                "Empty results must use the warning format.");

        check(
                manager.addAppointment(
                        new Appointment(alice, gpOne, LocalTime.of(9, 0)))
                        .startsWith("SUCCESS"),
                "A valid appointment must be accepted.");
        check(
                manager.addAppointment(
                        new Appointment(alice, gpTwo, LocalTime.of(9, 0)))
                        .startsWith("SUCCESS"),
                "A different professional must be available at the same time.");
        check(
                manager.addAppointment(
                        new Appointment(ben, gpOne, LocalTime.of(9, 0)))
                        .startsWith("WARNING"),
                "Double booking must be rejected.");

        List<Appointment> all = manager.getAllAppointmentsSorted();
        check(all.size() == 2, "Two valid appointments must be stored.");
        check(
                manager.getAppointmentsByProfessional("1001").size() == 1,
                "Professional filtering must work.");
        check(
                manager.getAppointmentsByPatientPhone("0412 345 678").size() == 2,
                "Patient phone filtering must normalize input.");
        check(
                manager.cancelAppointment(
                        "1002", LocalTime.of(9, 0))
                        .startsWith("SUCCESS"),
                "Professional ID and time cancellation must work.");
        check(
                manager.cancelAppointment("0412345678")
                        .startsWith("SUCCESS"),
                "Phone-only cancellation must work.");
        check(
                manager.cancelAppointment("0412345678")
                        .startsWith("WARNING"),
                "Cancelling a missing appointment must return a warning.");

        DayOfWeek tomorrow = LocalDate.now().getDayOfWeek().plus(1);
        Specialist unavailable = new Specialist(
                "3001",
                "Dr Unavailable",
                EnumSet.of(tomorrow),
                "Dermatology");
        check(
                manager.addAppointment(
                        new Appointment(
                                new Patient("Casey", "0400111222"),
                                unavailable,
                                LocalTime.of(12, 0)))
                        .contains("does not work today"),
                "Professionals unavailable today must be rejected.");

        check(
                new Nurse(
                        "2001",
                        "Nurse Brown",
                        today,
                        "Registered Nurse").equals(nurse),
                "Subclass instances with the same ID must compare equal.");
    }

    private static void checksFlexibleScheduleContract() {
        List<LocalTime> generated = AppointmentManager.createSchedule(
                LocalTime.of(9, 0),
                LocalTime.of(17, 0),
                20);
        check(
                generated.size() == 25,
                "A 09:00 to 17:00 schedule at 20-minute intervals must have 25 slots.");
        check(
                generated.get(0).equals(LocalTime.of(9, 0))
                        && generated.get(generated.size() - 1)
                                .equals(LocalTime.of(17, 0)),
                "Generated schedules must include both boundary slots.");

        List<LocalTime> irregular = new ArrayList<LocalTime>();
        irregular.add(LocalTime.of(10, 0));
        irregular.add(LocalTime.of(13, 30));
        AppointmentManager manager = new AppointmentManager(irregular);
        Set<DayOfWeek> today = EnumSet.of(LocalDate.now().getDayOfWeek());
        GeneralPractitioner doctor = new GeneralPractitioner(
                "5001", "Dr Flexible", today, "Room 50");
        check(
                manager.addAppointment(
                        new Appointment(
                                new Patient("Taylor", "0455666777"),
                                doctor,
                                LocalTime.of(13, 30)))
                        .startsWith("SUCCESS"),
                "A custom irregular schedule must accept configured slots.");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}

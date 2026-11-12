import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages all appointments for the clinic.
 *
 * <p>Design note: the daily schedule is stored as a list instead of being
 * repeated in many hard-coded checks. The same methods can therefore work when
 * the clinic changes its hours or when a smaller schedule is used in a test.</p>
 */
public class AppointmentManager {
    private static final LocalTime CLINIC_OPEN = LocalTime.of(8, 0);
    private static final LocalTime CLINIC_CLOSE = LocalTime.of(16, 0);
    private static final int SLOT_INTERVAL_MINUTES = 30;

    private final List<Appointment> appointments;
    private final List<LocalTime> dailySchedule;

    /**
     * Creates a manager with the clinic's default schedule.
     */
    public AppointmentManager() {
        this(createDefaultSchedule());
    }

    /**
     * Creates a manager with a supplied daily schedule. Supplying the schedule
     * allows the clinic to change it without changing appointment management.
     *
     * @param dailySchedule all valid appointment times
     */
    public AppointmentManager(List<LocalTime> dailySchedule) {
        this.appointments = new ArrayList<>();
        // Copy and lock the schedule so another object cannot change clinic rules.
        this.dailySchedule = copyAndValidateSchedule(dailySchedule);
    }

    /**
     * Returns the clinic's standard 08:00 to 16:00 half-hour schedule.
     *
     * @return the default schedule
     */
    public static List<LocalTime> createDefaultSchedule() {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime slot = CLINIC_OPEN;
        // The final 16:00 slot is included because the brief gives that boundary.
        while (!slot.isAfter(CLINIC_CLOSE)) {
            slots.add(slot);
            slot = slot.plusMinutes(SLOT_INTERVAL_MINUTES);
        }
        return slots;
    }

    /**
     * Attempts to add an appointment.
     *
     * @param appointment appointment to add
     * @return a success or warning message
     */
    public String addAppointment(Appointment appointment) {
        if (appointment == null) {
            return "WARNING: An appointment is required.";
        }

        // A valid booking must fit the clinic schedule and the professional's week.
        if (!dailySchedule.contains(appointment.getAppointmentTime())) {
            return "WARNING: The requested time is not an available clinic slot.";
        }

        DayOfWeek today = LocalDate.now().getDayOfWeek();
        if (!appointment.getHealthProfessional()
                .getAvailableDays()
                .contains(today)) {
            return "WARNING: The health professional does not work today.";
        }

        if (isDoubleBooked(appointment)) {
            return "WARNING: The health professional is already booked at "
                    + appointment.getAppointmentTime()
                    + ".";
        }

        appointments.add(appointment);
        return "SUCCESS: Appointment added for "
                + appointment.getPatient().getName()
                + " with "
                + appointment.getHealthProfessional().getName()
                + " at "
                + appointment.getAppointmentTime()
                + ".";
    }

    /**
     * Returns all appointments ordered from earliest to latest.
     *
     * @return a sorted copy of all appointments
     */
    public List<Appointment> getAllAppointmentsSorted() {
        return sortedCopy(appointments);
    }

    /**
     * Returns appointments for a professional ID in time order.
     *
     * @param professionalId professional ID to match
     * @return matching appointments
     */
    public List<Appointment> getAppointmentsByProfessional(
            String professionalId) {
        List<Appointment> matches = new ArrayList<>();
        if (professionalId == null) {
            return matches;
        }

        for (Appointment appointment : appointments) {
            if (appointment.isWithProfessional(professionalId.trim())) {
                matches.add(appointment);
            }
        }
        return sortedCopy(matches);
    }

    /**
     * Returns appointments for a patient phone number in time order.
     *
     * @param mobilePhone patient phone number to match
     * @return matching appointments
     */
    public List<Appointment> getAppointmentsByPatientPhone(
            String mobilePhone) {
        List<Appointment> matches = new ArrayList<>();
        if (mobilePhone == null) {
            return matches;
        }

        String normalisedPhone;
        try {
            // Search accepts the same spaces/hyphens as patient input.
            normalisedPhone = Patient.normaliseMobilePhone(mobilePhone);
        } catch (IllegalArgumentException exception) {
            return matches;
        }

        for (Appointment appointment : appointments) {
            if (appointment.isForPatient(normalisedPhone)) {
                matches.add(appointment);
            }
        }
        return sortedCopy(matches);
    }

    /**
     * Formats an appointment list for console display.
     *
     * @param appointmentsToDisplay appointments to format
     * @return readable appointment output
     */
    public String displayAppointments(
            List<Appointment> appointmentsToDisplay) {
        if (appointmentsToDisplay == null
                || appointmentsToDisplay.isEmpty()) {
            return "No appointments found.";
        }

        StringBuilder output = new StringBuilder();
        List<Appointment> sorted = sortedCopy(appointmentsToDisplay);
        for (int index = 0; index < sorted.size(); index++) {
            if (index > 0) {
                output.append(System.lineSeparator());
            }
            output.append(sorted.get(index));
        }
        return output.toString();
    }

    /**
     * Cancels an appointment by professional ID and appointment time.
     *
     * @param professionalId professional ID
     * @param appointmentTime appointment time
     * @return a success or warning message
     */
    public String cancelAppointment(
            String professionalId,
            LocalTime appointmentTime) {
        if (professionalId == null || appointmentTime == null) {
            return "WARNING: A professional ID and appointment time are required.";
        }

        for (int index = 0; index < appointments.size(); index++) {
            Appointment appointment = appointments.get(index);
            if (appointment.isWithProfessional(professionalId.trim())
                    && appointment.getAppointmentTime()
                            .equals(appointmentTime)) {
                appointments.remove(index);
                return "SUCCESS: Appointment cancelled for professional "
                        + professionalId.trim()
                        + " at "
                        + appointmentTime
                        + ".";
            }
        }
        return "WARNING: No matching appointment was found.";
    }

    /**
     * Cancels the earliest appointment for a patient phone number.
     *
     * @param mobilePhone patient phone number
     * @return a success or warning message
     */
    public String cancelAppointment(String mobilePhone) {
        if (mobilePhone == null) {
            return "WARNING: A patient phone number is required.";
        }

        String normalisedPhone;
        try {
            normalisedPhone = Patient.normaliseMobilePhone(mobilePhone);
        } catch (IllegalArgumentException exception) {
            return "WARNING: The patient phone number is invalid.";
        }

        List<Appointment> matches =
                getAppointmentsByPatientPhone(normalisedPhone);
        if (matches.isEmpty()) {
            return "WARNING: No appointment was found for that phone number.";
        }

        // The list is sorted, so the first match is the patient's earliest booking.
        Appointment earliest = matches.get(0);
        appointments.remove(earliest);
        return "SUCCESS: Cancelled the earliest appointment for "
                + earliest.getPatient().getName()
                + " with "
                + earliest.getHealthProfessional().getName()
                + " at "
                + earliest.getAppointmentTime()
                + ".";
    }

    /**
     * Returns the number of active appointments.
     *
     * @return appointment count
     */
    public int getAppointmentCount() {
        return appointments.size();
    }

    private boolean isDoubleBooked(Appointment candidate) {
        for (Appointment existing : appointments) {
            // A professional is double-booked when both ID and time match.
            boolean sameProfessional = existing.isWithProfessional(
                    candidate.getHealthProfessional().getId());
            boolean sameTime = existing.getAppointmentTime()
                    .equals(candidate.getAppointmentTime());
            if (sameProfessional && sameTime) {
                return true;
            }
        }
        return false;
    }

    private static List<Appointment> sortedCopy(
            List<Appointment> source) {
        // Sort a copy so callers cannot disturb the manager's stored order.
        List<Appointment> copy = new ArrayList<>(source);
        Collections.sort(copy);
        return copy;
    }

    private static List<LocalTime> copyAndValidateSchedule(
            List<LocalTime> suppliedSchedule) {
        if (suppliedSchedule == null || suppliedSchedule.isEmpty()) {
            throw new IllegalArgumentException(
                    "The daily schedule cannot be empty.");
        }

        List<LocalTime> validated = new ArrayList<>();
        for (LocalTime time : suppliedSchedule) {
            if (time == null) {
                throw new IllegalArgumentException(
                        "The daily schedule cannot contain null.");
            }
            if (time.isBefore(CLINIC_OPEN) || time.isAfter(CLINIC_CLOSE)) {
                throw new IllegalArgumentException(
                        "Schedule times must be between 08:00 and 16:00.");
            }
            if (validated.contains(time)) {
                throw new IllegalArgumentException(
                        "The daily schedule cannot contain duplicate times.");
            }
            validated.add(time);
        }

        Collections.sort(validated);
        return Collections.unmodifiableList(validated);
    }
}

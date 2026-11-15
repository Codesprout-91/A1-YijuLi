import java.util.ArrayList;

/**
 * Manages appointments for the simulated current clinic day.
 */
public class AppointmentManager {
    private final ArrayList<Appointment> appointments;

    /**
     * Creates an empty manager for the current clinic day.
     */
    public AppointmentManager() {
        this.appointments = new ArrayList<Appointment>();
    }

    /**
     * Returns the current clinic day.
     *
     * @return simulated current day
     */
    public String getCurrentDay() {
        return ClinicSchedule.getCurrentDay();
    }

    /**
     * Returns the number of active appointments.
     *
     * @return appointment count
     */
    public int getAppointmentCount() {
        return appointments.size();
    }

    /**
     * Attempts to add a complete appointment.
     *
     * @param appointment appointment to add
     * @return success or warning message
     */
    public String addAppointment(Appointment appointment) {
        if (appointment == null) {
            return "WARNING: An appointment is required.";
        }

        HealthProfessional professional =
                appointment.getHealthProfessional();
        if (!professional.worksOn(ClinicSchedule.getCurrentDay())) {
            return "WARNING: "
                    + professional.getName()
                    + " does not work on "
                    + ClinicSchedule.getCurrentDay()
                    + ".";
        }

        if (isDoubleBooked(appointment)) {
            return "WARNING: "
                    + professional.getName()
                    + " is already booked at "
                    + appointment.getAppointmentTime()
                    + ".";
        }

        appointments.add(appointment);
        return "SUCCESS: Appointment added for "
                + appointment.getPatient().getName()
                + " with "
                + professional.getName()
                + " at "
                + appointment.getAppointmentTime()
                + ".";
    }

    /**
     * Returns all appointments ordered by time.
     *
     * @return a sorted copy
     */
    public ArrayList<Appointment> getAllAppointmentsSorted() {
        return sortedCopy(appointments);
    }

    /**
     * Returns appointments for one professional ID.
     *
     * @param professionalId ID to filter by
     * @return matching appointments in time order
     */
    public ArrayList<Appointment> getAppointmentsByProfessional(
            String professionalId) {
        ArrayList<Appointment> matches = new ArrayList<Appointment>();
        if (professionalId == null || professionalId.trim().isEmpty()) {
            return matches;
        }

        String requestedId = professionalId.trim();
        for (int index = 0; index < appointments.size(); index++) {
            Appointment appointment = appointments.get(index);
            if (appointment.isWithProfessional(requestedId)) {
                matches.add(appointment);
            }
        }
        return sortedCopy(matches);
    }

    /**
     * Returns appointments for one patient mobile number.
     *
     * @param mobilePhone number to filter by
     * @return matching appointments in time order
     */
    public ArrayList<Appointment> getAppointmentsByPatientPhone(
            String mobilePhone) {
        ArrayList<Appointment> matches = new ArrayList<Appointment>();
        if (mobilePhone == null) {
            return matches;
        }

        String normalizedPhone;
        try {
            normalizedPhone = Patient.normaliseMobilePhone(mobilePhone);
        } catch (IllegalArgumentException exception) {
            return matches;
        }

        for (int index = 0; index < appointments.size(); index++) {
            Appointment appointment = appointments.get(index);
            if (appointment.isForPatient(normalizedPhone)) {
                matches.add(appointment);
            }
        }
        return sortedCopy(matches);
    }

    /**
     * Formats appointments for console output.
     *
     * @param appointmentsToDisplay appointments to display
     * @return readable output
     */
    public String displayAppointments(
            ArrayList<Appointment> appointmentsToDisplay) {
        if (appointmentsToDisplay == null
                || appointmentsToDisplay.isEmpty()) {
            return "WARNING: No appointments found.";
        }

        ArrayList<Appointment> sorted =
                sortedCopy(appointmentsToDisplay);
        String output = "";
        for (int index = 0; index < sorted.size(); index++) {
            if (index > 0) {
                output += System.lineSeparator();
            }
            output += sorted.get(index);
        }
        return output;
    }

    /**
     * Cancels the appointment for a professional at a specific time.
     *
     * @param professionalId professional ID
     * @param timeSlot appointment time
     * @return success or warning message
     */
    public String cancelAppointment(
            String professionalId,
            TimeSlot timeSlot) {
        if (professionalId == null
                || professionalId.trim().isEmpty()
                || timeSlot == null) {
            return "WARNING: A professional ID and time are required.";
        }

        String requestedId = professionalId.trim();
        for (int index = 0; index < appointments.size(); index++) {
            Appointment appointment = appointments.get(index);
            if (appointment.isWithProfessional(requestedId)
                    && appointment.getAppointmentTime().equals(timeSlot)) {
                appointments.remove(index);
                return "SUCCESS: Appointment cancelled for professional "
                        + requestedId
                        + " at "
                        + timeSlot
                        + ".";
            }
        }
        return "WARNING: No matching appointment was found.";
    }

    /**
     * Cancels a patient's only appointment using the mobile number.
     *
     * <p>If the number belongs to multiple appointments, the operation is
     * rejected because the requested appointment would be ambiguous.</p>
     *
     * @param mobilePhone patient mobile number
     * @return success or warning message
     */
    public String cancelAppointment(String mobilePhone) {
        if (mobilePhone == null) {
            return "WARNING: A patient mobile number is required.";
        }

        ArrayList<Appointment> matches =
                getAppointmentsByPatientPhone(mobilePhone);
        if (matches.isEmpty()) {
            return "WARNING: No appointment was found for that mobile number.";
        }
        if (matches.size() > 1) {
            return "WARNING: Multiple appointments use that mobile number.";
        }

        Appointment appointment = matches.get(0);
        appointments.remove(appointment);
        return "SUCCESS: Appointment cancelled for "
                + appointment.getPatient().getName()
                + " with "
                + appointment.getHealthProfessional().getName()
                + " at "
                + appointment.getAppointmentTime()
                + ".";
    }

    private boolean isDoubleBooked(Appointment candidate) {
        for (int index = 0; index < appointments.size(); index++) {
            Appointment existing = appointments.get(index);
            if (existing.isWithProfessional(
                        candidate.getHealthProfessional().getId())
                    && existing.getAppointmentTime().equals(
                            candidate.getAppointmentTime())) {
                return true;
            }
        }
        return false;
    }

    private static ArrayList<Appointment> sortedCopy(
            ArrayList<Appointment> source) {
        ArrayList<Appointment> copy =
                new ArrayList<Appointment>(source);

        for (int index = 1; index < copy.size(); index++) {
            Appointment current = copy.get(index);
            int position = index - 1;
            while (position >= 0
                    && copy.get(position).compareTo(current) > 0) {
                copy.set(position + 1, copy.get(position));
                position--;
            }
            copy.set(position + 1, current);
        }
        return copy;
    }
}

import java.time.LocalTime;

/**
 * Links a patient to a health professional at a specific appointment time.
 */
public class Appointment implements Comparable<Appointment> {
    private final Patient patient;
    private final HealthProfessional healthProfessional;
    private final LocalTime appointmentTime;

    /**
     * Creates a complete appointment.
     *
     * @param patient patient attending the appointment
     * @param healthProfessional professional providing the service
     * @param appointmentTime appointment start time
     */
    public Appointment(
            Patient patient,
            HealthProfessional healthProfessional,
            LocalTime appointmentTime) {
        if (patient == null) {
            throw new IllegalArgumentException("A patient is required.");
        }
        if (healthProfessional == null) {
            throw new IllegalArgumentException(
                    "A health professional is required.");
        }
        if (appointmentTime == null) {
            throw new IllegalArgumentException(
                    "An appointment time is required.");
        }

        this.patient = patient;
        this.healthProfessional = healthProfessional;
        this.appointmentTime = appointmentTime;
    }

    /**
     * Returns the patient attending the appointment.
     *
     * @return the patient
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Returns the professional providing the service.
     *
     * @return the health professional
     */
    public HealthProfessional getHealthProfessional() {
        return healthProfessional;
    }

    /**
     * Returns the appointment start time.
     *
     * @return the appointment time
     */
    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    /**
     * Returns whether this appointment belongs to a professional ID.
     *
     * @param professionalId ID to compare
     * @return true when the IDs match
     */
    public boolean isWithProfessional(String professionalId) {
        return healthProfessional.getId().equals(professionalId);
    }

    /**
     * Returns whether this appointment belongs to a patient phone number.
     *
     * @param mobilePhone phone number to compare
     * @return true when the phone numbers match
     */
    public boolean isForPatient(String mobilePhone) {
        return patient.getMobilePhone().equals(mobilePhone);
    }

    /**
     * Orders appointments from earliest to latest. The professional ID and
     * patient phone are used as deterministic tie-breakers.
     *
     * @param other appointment to compare
     * @return comparison result
     */
    @Override
    public int compareTo(Appointment other) {
        int timeComparison = appointmentTime.compareTo(other.appointmentTime);
        if (timeComparison != 0) {
            return timeComparison;
        }

        // Tie-breakers keep the order predictable when two appointments share a time.
        int professionalComparison = healthProfessional.getId()
                .compareTo(other.healthProfessional.getId());
        if (professionalComparison != 0) {
            return professionalComparison;
        }
        return patient.getMobilePhone()
                .compareTo(other.patient.getMobilePhone());
    }

    @Override
    public String toString() {
        return appointmentTime
                + " | " + patient
                + " | " + healthProfessional.getProfessionalType()
                + " [" + healthProfessional.getId() + "] "
                + healthProfessional.getName();
    }
}

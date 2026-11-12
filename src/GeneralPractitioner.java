import java.time.DayOfWeek;
import java.util.Set;

/**
 * Represents a general practitioner who provides first-contact medical care.
 */
public class GeneralPractitioner extends HealthProfessional {
    private final String clinicRoom;

    /**
     * Creates a general practitioner with custom working days.
     *
     * @param id numeric professional identifier
     * @param name professional's name
     * @param availableDays days on which the professional works
     * @param clinicRoom room where consultations take place
     */
    public GeneralPractitioner(
            String id,
            String name,
            Set<DayOfWeek> availableDays,
            String clinicRoom) {
        super(id, name, availableDays);
        this.clinicRoom = requireText(clinicRoom, "Clinic room");
    }

    /**
     * Creates a general practitioner with default working days.
     *
     * @param id numeric professional identifier
     * @param name professional's name
     * @param clinicRoom room where consultations take place
     */
    public GeneralPractitioner(
            String id,
            String name,
            String clinicRoom) {
        super(id, name);
        this.clinicRoom = requireText(clinicRoom, "Clinic room");
    }

    @Override
    public String getProfessionalType() {
        return "General Practitioner";
    }

    @Override
    public String performPrimaryDuty() {
        return getName() + " assesses a patient and prescribes medication.";
    }

    @Override
    protected String getTypeSpecificDetails() {
        return "Clinic room: " + clinicRoom;
    }
}

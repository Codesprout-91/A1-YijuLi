import java.time.DayOfWeek;
import java.util.Set;

/**
 * Represents a nurse who supports patient care and clinical procedures.
 */
public class Nurse extends HealthProfessional {
    private final String qualification;

    /**
     * Creates a nurse with custom working days.
     *
     * @param id numeric professional identifier
     * @param name professional's name
     * @param availableDays days on which the professional works
     * @param qualification nursing qualification
     */
    public Nurse(
            String id,
            String name,
            Set<DayOfWeek> availableDays,
            String qualification) {
        super(id, name, availableDays);
        this.qualification = requireText(qualification, "Qualification");
    }

    /**
     * Creates a nurse with default working days.
     *
     * @param id numeric professional identifier
     * @param name professional's name
     * @param qualification nursing qualification
     */
    public Nurse(String id, String name, String qualification) {
        super(id, name);
        this.qualification = requireText(qualification, "Qualification");
    }

    @Override
    public String getProfessionalType() {
        return "Nurse";
    }

    @Override
    public String performPrimaryDuty() {
        return getName() + " takes a blood sample from a patient.";
    }

    @Override
    protected String getTypeSpecificDetails() {
        return "Qualification: " + qualification;
    }
}

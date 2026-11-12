import java.time.DayOfWeek;
import java.util.Set;

/**
 * Represents a medical specialist who focuses on a particular field.
 */
public class Specialist extends HealthProfessional {
    private final String medicalSpecialty;

    /**
     * Creates a specialist with custom working days.
     *
     * @param id numeric professional identifier
     * @param name professional's name
     * @param availableDays days on which the professional works
     * @param medicalSpecialty specialist medical field
     */
    public Specialist(
            String id,
            String name,
            Set<DayOfWeek> availableDays,
            String medicalSpecialty) {
        super(id, name, availableDays);
        this.medicalSpecialty =
                requireText(medicalSpecialty, "Medical specialty");
    }

    /**
     * Creates a specialist with default working days.
     *
     * @param id numeric professional identifier
     * @param name professional's name
     * @param medicalSpecialty specialist medical field
     */
    public Specialist(
            String id,
            String name,
            String medicalSpecialty) {
        super(id, name);
        this.medicalSpecialty =
                requireText(medicalSpecialty, "Medical specialty");
    }

    @Override
    public String getProfessionalType() {
        return "Specialist";
    }

    @Override
    public String performPrimaryDuty() {
        return getName() + " completes a specialist "
                + medicalSpecialty + " assessment.";
    }

    @Override
    protected String getTypeSpecificDetails() {
        return "Medical specialty: " + medicalSpecialty;
    }
}

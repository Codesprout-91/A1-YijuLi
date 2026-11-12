import java.time.DayOfWeek;
import java.util.Set;

/**
 * Represents a dietitian who provides nutrition assessment and advice.
 */
public class Dietitian extends HealthProfessional {
    private final String nutritionSpecialty;

    /**
     * Creates a dietitian with custom working days.
     *
     * @param id numeric professional identifier
     * @param name professional's name
     * @param availableDays days on which the professional works
     * @param nutritionSpecialty area of nutrition practice
     */
    public Dietitian(
            String id,
            String name,
            Set<DayOfWeek> availableDays,
            String nutritionSpecialty) {
        super(id, name, availableDays);
        this.nutritionSpecialty =
                requireText(nutritionSpecialty, "Nutrition specialty");
    }

    /**
     * Creates a dietitian with default working days.
     *
     * @param id numeric professional identifier
     * @param name professional's name
     * @param nutritionSpecialty area of nutrition practice
     */
    public Dietitian(
            String id,
            String name,
            String nutritionSpecialty) {
        super(id, name);
        this.nutritionSpecialty =
                requireText(nutritionSpecialty, "Nutrition specialty");
    }

    @Override
    public String getProfessionalType() {
        return "Dietitian";
    }

    @Override
    public String performPrimaryDuty() {
        return getName() + " prepares a nutrition plan for a patient.";
    }

    @Override
    protected String getTypeSpecificDetails() {
        return "Nutrition specialty: " + nutritionSpecialty;
    }
}

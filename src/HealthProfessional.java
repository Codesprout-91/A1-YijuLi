import java.time.DayOfWeek;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Represents the information and behaviour shared by all health professionals.
 * The class is abstract because every actual professional must have a specialty.
 */
public abstract class HealthProfessional {
    private final String id;
    private final String name;
    private final Set<DayOfWeek> availableDays;

    /**
     * Creates a fully specified health professional.
     *
     * @param id numeric professional identifier
     * @param name professional's name
     * @param availableDays days on which the professional works
     */
    protected HealthProfessional(
            String id,
            String name,
            Set<DayOfWeek> availableDays) {
        this.id = requireNumericId(id);
        this.name = requireText(name, "Name");
        // A copied, unmodifiable set protects the object's internal state.
        this.availableDays = copyAndValidateDays(availableDays);
    }

    /**
     * Creates a professional using the default working days of Monday,
     * Wednesday, and Friday.
     *
     * @param id numeric professional identifier
     * @param name professional's name
     */
    protected HealthProfessional(String id, String name) {
        this(id, name, EnumSet.of(
                DayOfWeek.MONDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.FRIDAY));
    }

    /**
     * Returns the professional's numeric identifier.
     *
     * @return the identifier
     */
    public final String getId() {
        return id;
    }

    /**
     * Returns the professional's name.
     *
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns an unmodifiable set of the professional's working days.
     *
     * @return the available working days
     */
    public final Set<DayOfWeek> getAvailableDays() {
        return availableDays;
    }

    /**
     * Returns a readable name for the professional type.
     *
     * @return the professional type
     */
    public abstract String getProfessionalType();

    /**
     * Performs the main duty associated with this professional type.
     *
     * @return a description of the completed duty
     */
    public abstract String performPrimaryDuty();

    /**
     * Returns the details that are unique to a concrete professional type.
     *
     * @return the type-specific details
     */
    protected abstract String getTypeSpecificDetails();

    @Override
    public final String toString() {
        return getProfessionalType()
                + System.lineSeparator()
                + "  ID: " + id
                + System.lineSeparator()
                + "  Name: " + name
                + System.lineSeparator()
                + "  Available: " + formatAvailableDays()
                + System.lineSeparator()
                + "  " + getTypeSpecificDetails();
    }

    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HealthProfessional)) {
            return false;
        }
        HealthProfessional professional = (HealthProfessional) other;
        // The ID represents the professional's identity across all role types.
        return id.equals(professional.id);
    }

    @Override
    public final int hashCode() {
        return id.hashCode();
    }

    /**
     * Validates text commonly supplied to a health professional object.
     *
     * @param value text to validate
     * @param fieldName name used in an error message
     * @return trimmed text
     */
    protected static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
        }
        return value.trim();
    }

    private static String requireNumericId(String value) {
        String validated = requireText(value, "ID");
        // Restrict IDs to ASCII digits rather than Unicode numeral characters.
        for (int index = 0; index < validated.length(); index++) {
            char character = validated.charAt(index);
            if (character < '0' || character > '9') {
                throw new IllegalArgumentException(
                        "ID must contain numbers only.");
            }
        }
        return validated;
    }

    private static Set<DayOfWeek> copyAndValidateDays(
            Set<DayOfWeek> days) {
        if (days == null || days.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one available day is required.");
        }
        if (days.contains(null)) {
            throw new IllegalArgumentException(
                    "Available days cannot contain null.");
        }
        // EnumSet prevents duplicate days; unmodifiableSet prevents later changes.
        return Collections.unmodifiableSet(EnumSet.copyOf(days));
    }

    private String formatAvailableDays() {
        StringBuilder result = new StringBuilder();
        for (DayOfWeek day : DayOfWeek.values()) {
            if (availableDays.contains(day)) {
                if (result.length() > 0) {
                    result.append(", ");
                }
                result.append(day);
            }
        }
        return result.toString();
    }
}

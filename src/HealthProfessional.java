import java.util.ArrayList;

/**
 * Represents the state and behaviour shared by all health professionals.
 */
public abstract class HealthProfessional implements Comparable<HealthProfessional> {
    private final String id;
    private final String name;
    private final ArrayList<String> availableDays;

    /**
     * Creates a professional with an explicit list of working days.
     *
     * @param id numeric professional identifier
     * @param name professional's name
     * @param availableDays days on which the professional works
     */
    protected HealthProfessional(
            String id,
            String name,
            ArrayList<String> availableDays) {
        this.id = requireNumericId(id);
        this.name = requireText(name, "Name");
        this.availableDays = copyAndValidateDays(availableDays);
    }

    /**
     * Creates a professional who works Monday, Wednesday, and Friday.
     *
     * @param id numeric professional identifier
     * @param name professional's name
     */
    protected HealthProfessional(String id, String name) {
        this(id, name, createMondayWednesdayFriday());
    }

    /**
     * Returns the numeric professional ID.
     *
     * @return the professional ID
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
     * Returns a defensive copy of the working days.
     *
     * @return a new working-day list
     */
    public final ArrayList<String> getAvailableDays() {
        return new ArrayList<String>(availableDays);
    }

    /**
     * Checks whether the professional works on a supplied day.
     *
     * @param day day to check
     * @return true when the professional works that day
     */
    public final boolean worksOn(String day) {
        return availableDays.contains(ClinicSchedule.normalizeDay(day));
    }

    /**
     * Returns the professional type.
     *
     * @return readable type
     */
    public abstract String getProfessionalType();

    /**
     * Performs the professional's primary role.
     *
     * @return duty result
     */
    public abstract String performPrimaryDuty();

    /**
     * Returns subtype-specific details.
     *
     * @return type-specific information
     */
    protected abstract String getTypeSpecificDetails();

    /**
     * Compares professionals by numeric ID.
     *
     * @param other professional to compare with
     * @return ID comparison result
     */
    @Override
    public int compareTo(HealthProfessional other) {
        if (other == null) {
            throw new IllegalArgumentException(
                    "Cannot compare with null.");
        }
        return id.compareTo(other.id);
    }

    /**
     * Compares professionals by ID.
     *
     * @param other object to compare with
     * @return true when both IDs match
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HealthProfessional)) {
            return false;
        }
        HealthProfessional professional = (HealthProfessional) other;
        return id.equals(professional.id);
    }

    /**
     * Returns a hash code consistent with equals.
     *
     * @return ID hash code
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Returns all common and type-specific professional details.
     *
     * @return formatted professional details
     */
    @Override
    public String toString() {
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

    /**
     * Validates and trims required text.
     *
     * @param value text to validate
     * @param fieldName field name used in an error
     * @return trimmed text
     */
    protected static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be blank.");
        }
        return value.trim();
    }

    /**
     * Creates the default Monday-to-Friday working days.
     *
     * @return default working days
     */
    protected static ArrayList<String> createWeekdays() {
        ArrayList<String> days = new ArrayList<String>();
        days.add(ClinicSchedule.MONDAY);
        days.add(ClinicSchedule.TUESDAY);
        days.add(ClinicSchedule.WEDNESDAY);
        days.add(ClinicSchedule.THURSDAY);
        days.add(ClinicSchedule.FRIDAY);
        return days;
    }

    /**
     * Creates a Monday, Wednesday, Friday working-day list.
     *
     * @return Monday, Wednesday, Friday list
     */
    protected static ArrayList<String> createMondayWednesdayFriday() {
        ArrayList<String> days = new ArrayList<String>();
        days.add(ClinicSchedule.MONDAY);
        days.add(ClinicSchedule.WEDNESDAY);
        days.add(ClinicSchedule.FRIDAY);
        return days;
    }

    /**
     * Creates a Monday, Thursday, Friday working-day list.
     *
     * @return Monday, Thursday, Friday list
     */
    protected static ArrayList<String> createMondayThursdayFriday() {
        ArrayList<String> days = new ArrayList<String>();
        days.add(ClinicSchedule.MONDAY);
        days.add(ClinicSchedule.THURSDAY);
        days.add(ClinicSchedule.FRIDAY);
        return days;
    }

    private static String requireNumericId(String value) {
        String validated = requireText(value, "ID");
        for (int index = 0; index < validated.length(); index++) {
            char character = validated.charAt(index);
            if (character < '0' || character > '9') {
                throw new IllegalArgumentException(
                        "ID must contain numbers only.");
            }
        }
        return validated;
    }

    private static ArrayList<String> copyAndValidateDays(
            ArrayList<String> days) {
        if (days == null || days.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one available day is required.");
        }

        ArrayList<String> validated = new ArrayList<String>();
        for (int index = 0; index < days.size(); index++) {
            String normalizedDay =
                    ClinicSchedule.normalizeDay(days.get(index));
            if (!ClinicSchedule.isValidDay(normalizedDay)) {
                throw new IllegalArgumentException(
                        "Invalid available day: " + days.get(index));
            }
            if (!validated.contains(normalizedDay)) {
                validated.add(normalizedDay);
            }
        }
        return validated;
    }

    private String formatAvailableDays() {
        String result = "";
        for (int index = 0; index < availableDays.size(); index++) {
            if (index > 0) {
                result += ", ";
            }
            result += availableDays.get(index);
        }
        return result;
    }
}

/**
 * Represents the patient information required when booking an appointment.
 */
public class Patient {
    private final String name;
    private final String mobilePhone;

    /**
     * Creates a patient with a validated name and mobile number.
     *
     * @param name patient's name
     * @param mobilePhone ten-digit Australian mobile number
     */
    public Patient(String name, String mobilePhone) {
        this.name = validateName(name);
        this.mobilePhone = validateMobilePhone(mobilePhone);
    }

    /**
     * Returns the patient's name.
     *
     * @return the patient name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the normalised mobile phone number.
     *
     * @return the mobile phone number
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * Removes spaces and hyphens from a mobile number and validates it.
     *
     * @param value mobile number entered by a user
     * @return a normal ten-digit mobile number
     */
    public static String normaliseMobilePhone(String value) {
        return validateMobilePhone(value);
    }

    @Override
    public String toString() {
        return name + " (mobile: " + mobilePhone + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Patient)) {
            return false;
        }
        Patient patient = (Patient) other;
        return mobilePhone.equals(patient.mobilePhone);
    }

    @Override
    public int hashCode() {
        return mobilePhone.hashCode();
    }

    private static String validateName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Patient name cannot be blank.");
        }
        return value.trim();
    }

    private static String validateMobilePhone(String value) {
        if (value == null) {
            throw new IllegalArgumentException(
                    "Mobile phone number cannot be blank.");
        }

        StringBuilder digits = new StringBuilder();
        // Ignore only common separators; any other letter or symbol is rejected.
        for (int index = 0; index < value.length(); index++) {
            char character = value.charAt(index);
            if (Character.isDigit(character)) {
                digits.append(character);
            } else if (character != ' ' && character != '-') {
                throw new IllegalArgumentException(
                        "Mobile phone number may contain digits, spaces, "
                                + "and hyphens only.");
            }
        }

        // Store one standard format so searches do not depend on how input was typed.
        if (digits.length() != 10 || digits.charAt(0) != '0') {
            throw new IllegalArgumentException(
                    "Mobile phone number must contain 10 digits and start with 0.");
        }
        return digits.toString();
    }
}

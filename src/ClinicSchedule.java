import java.util.ArrayList;

/**
 * Stores the simulated clinic day and fixed daily appointment schedule.
 */
public final class ClinicSchedule {
    /** Monday day name. */
    public static final String MONDAY = "MONDAY";
    /** Tuesday day name. */
    public static final String TUESDAY = "TUESDAY";
    /** Wednesday day name. */
    public static final String WEDNESDAY = "WEDNESDAY";
    /** Thursday day name. */
    public static final String THURSDAY = "THURSDAY";
    /** Friday day name. */
    public static final String FRIDAY = "FRIDAY";
    /** Saturday day name. */
    public static final String SATURDAY = "SATURDAY";
    /** Sunday day name. */
    public static final String SUNDAY = "SUNDAY";

    /** Simulated current day used by this console demonstration. */
    public static final String CURRENT_DAY = MONDAY;

    private static final int OPEN_MINUTES = 8 * 60;
    private static final int CLOSE_MINUTES = 16 * 60;
    private static final int SLOT_INTERVAL_MINUTES = 30;
    private static final int MINUTES_PER_HOUR = 60;

    private static final ArrayList<String> VALID_DAYS = createValidDays();
    private static final ArrayList<TimeSlot> DAILY_SLOTS = createDailySlots();

    private ClinicSchedule() {
        // Utility class; instances are not required.
    }

    /**
     * Returns the simulated current clinic day.
     *
     * @return current day name
     */
    public static String getCurrentDay() {
        return CURRENT_DAY;
    }

    /**
     * Returns a copy of all valid day names.
     *
     * @return valid days
     */
    public static ArrayList<String> getValidDays() {
        return new ArrayList<String>(VALID_DAYS);
    }

    /**
     * Returns a copy of all bookable time slots.
     *
     * @return daily time slots
     */
    public static ArrayList<TimeSlot> getDailySlots() {
        return new ArrayList<TimeSlot>(DAILY_SLOTS);
    }

    /**
     * Checks whether a day name is valid.
     *
     * @param day day name to check
     * @return true when the day is valid
     */
    public static boolean isValidDay(String day) {
        return VALID_DAYS.contains(normalizeDay(day));
    }

    /**
     * Checks whether a time belongs to the fixed clinic schedule.
     *
     * @param timeSlot time to check
     * @return true when the time is bookable
     */
    public static boolean isScheduledSlot(TimeSlot timeSlot) {
        return timeSlot != null && DAILY_SLOTS.contains(timeSlot);
    }

    static String normalizeDay(String day) {
        return day == null ? "" : day.trim().toUpperCase();
    }

    private static ArrayList<String> createValidDays() {
        ArrayList<String> days = new ArrayList<String>();
        days.add(MONDAY);
        days.add(TUESDAY);
        days.add(WEDNESDAY);
        days.add(THURSDAY);
        days.add(FRIDAY);
        days.add(SATURDAY);
        days.add(SUNDAY);
        return days;
    }

    private static ArrayList<TimeSlot> createDailySlots() {
        ArrayList<TimeSlot> slots = new ArrayList<TimeSlot>();
        for (int minutes = OPEN_MINUTES;
                minutes < CLOSE_MINUTES;
                minutes += SLOT_INTERVAL_MINUTES) {
            slots.add(new TimeSlot(
                    minutes / MINUTES_PER_HOUR,
                    minutes % MINUTES_PER_HOUR));
        }
        return slots;
    }
}

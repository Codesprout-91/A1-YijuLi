/**
 * Represents one validated half-hour clinic time.
 */
public class TimeSlot implements Comparable<TimeSlot> {
    private final int hour;
    private final int minute;

    /**
     * Creates a validated thirty-minute time slot.
     *
     * @param hour hour in 24-hour time
     * @param minute minute value, either zero or thirty
     */
    public TimeSlot(int hour, int minute) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException(
                    "Hour must be between 0 and 23.");
        }
        if (minute != 0 && minute != 30) {
            throw new IllegalArgumentException(
                    "Minute must be 0 or 30.");
        }
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Returns the hour.
     *
     * @return hour in 24-hour time
     */
    public int getHour() {
        return hour;
    }

    /**
     * Returns the minute.
     *
     * @return either zero or thirty
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Returns the time as minutes after midnight.
     *
     * @return minute-of-day value
     */
    public int getMinuteOfDay() {
        return hour * 60 + minute;
    }

    /**
     * Compares two time slots chronologically.
     *
     * @param other time slot to compare with
     * @return chronological comparison result
     */
    @Override
    public int compareTo(TimeSlot other) {
        if (other == null) {
            throw new IllegalArgumentException(
                    "Cannot compare with null.");
        }
        if (getMinuteOfDay() < other.getMinuteOfDay()) {
            return -1;
        }
        if (getMinuteOfDay() > other.getMinuteOfDay()) {
            return 1;
        }
        return 0;
    }

    /**
     * Checks equality by hour and minute.
     *
     * @param other object to compare with
     * @return true when both times match
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TimeSlot)) {
            return false;
        }
        TimeSlot slot = (TimeSlot) other;
        return hour == slot.hour && minute == slot.minute;
    }

    /**
     * Returns a hash code consistent with equals.
     *
     * @return hash code for the time
     */
    @Override
    public int hashCode() {
        return hour * 31 + minute;
    }

    /**
     * Returns the time in HH:mm format.
     *
     * @return formatted time
     */
    @Override
    public String toString() {
        String hourText = hour < 10 ? "0" + hour : String.valueOf(hour);
        String minuteText = minute == 0 ? "00" : String.valueOf(minute);
        return hourText + ":" + minuteText;
    }
}

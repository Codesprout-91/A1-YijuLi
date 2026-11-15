# A1-YijuLi

PROG2004 Object Oriented Programming - Assessment 1

This project is a console-based health appointment system written in Java. It
demonstrates object-oriented design without a graphical interface, database,
login system, menu, or external library.

## Main features

- Abstract `HealthProfessional` superclass
- `GeneralPractitioner`, `Nurse`, `Dietitian`, and `Specialist` subclasses
- Encapsulation and validation for professional, patient, and appointment data
- Polymorphic processing through the `HealthProfessional` base type
- Appointment filtering and natural ordering by time
- Duplicate-booking prevention
- Cancellation by professional ID and time, or by patient phone number
- Clear `SUCCESS` and `WARNING` messages
- A complete scripted demonstration in `AssignmentOne.main`

## Project structure

```text
src/
  Appointment.java
  AppointmentManager.java
  AssignmentOne.java
  Dietitian.java
  GeneralPractitioner.java
  HealthProfessional.java
  Nurse.java
  Patient.java
  Specialist.java
```

All source files are in the default package. There are no external
dependencies.

## Run in VS Code

1. Open this repository in VS Code.
2. Confirm that VS Code uses Java 17.
3. Open `src/AssignmentOne.java`.
4. Click the top-right run menu.
5. Select `Run Without Debugging`.

Do not run individual support classes. `AssignmentOne` is the program entry
point; the other classes provide the appointment-system model.

## Run in a terminal

From the repository root:

```powershell
$sources = (Get-ChildItem src\*.java).FullName
javac -d bin $sources
java -cp bin AssignmentOne
```

The demonstration ends with:

```text
Active appointments: 4
```

## Appointment rules

- Bookings are for the current day only.
- A professional must be available on the current day.
- The same professional cannot be booked twice in the same time slot.
- A professional who does not work on the current day cannot be booked.
- The default schedule uses 30-minute slots from 08:00 through 16:00.
- The last slot is inclusive, so 16:00 is the final bookable start time.
- A clinic that must close exactly at 16:00 can pass 15:30 as its last slot.
- Filtering is available by professional ID or patient phone number.
- Cancellation by phone removes the patient's earliest matching appointment.

## Flexible schedules

Three forms of schedule configuration are supported.

### Default clinic schedule

```java
AppointmentManager manager = new AppointmentManager();
```

### Generated regular schedule

```java
AppointmentManager manager = new AppointmentManager(
        LocalTime.of(9, 0),
        LocalTime.of(17, 0),
        20);
```

The arguments represent the first slot, last slot, and interval in minutes.
The last slot is included.

### Fully custom irregular schedule

```java
AppointmentManager manager = new AppointmentManager(List.of(
        LocalTime.of(9, 0),
        LocalTime.of(11, 0),
        LocalTime.of(14, 30)));
```

This form supports lunch breaks, irregular opening hours, and special clinic
days.

`AppointmentManager.createSchedule(firstSlot, lastSlot, intervalMinutes)` can
also generate a reusable schedule list without creating a manager immediately.

## Validation

- Professional IDs accept ASCII digits `0-9` only.
- Names cannot be blank.
- Patient phone numbers are normalised to ten digits and must start with `0`.
- Appointment patient, professional, and time values cannot be null.
- Requested times must exist in the manager's schedule.
- Zero or negative schedule intervals are rejected.
- A last slot before the first slot is rejected.
- A last slot that does not align with the interval is rejected.
- Operations that cannot succeed return a warning instead of corrupting state.

## Design choices

- The base `HealthProfessional` type allows new professional subclasses to be
  processed without changing appointment-management code.
- Schedule validation is separate from appointment operations, so different
  clinic hours do not require changes to filtering, sorting, or cancellation.
- Sorting works on a copy of the appointment list and preserves the manager's
  stored collection.
- The scripted `main` demonstration uses arrays and loops so more test data can
  be added without duplicating the workflow.
- The demonstration includes an unavailable professional so the current-day
  availability warning is visible in the console output.
- The assignment requires a scripted console demonstration rather than a menu,
  so this project does not contain a `Scanner`-driven interface.

## Verification

The Java sources compile under Java 17 with:

```powershell
javac -Xlint:all -Werror -d bin (Get-ChildItem src\*.java).FullName
```

The test scenarios cover validation failures, polymorphism, sorting, duplicate
bookings, filtering, cancellation, custom schedules, and adding another health
professional type.

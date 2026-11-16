# A1-yli197

PROG2004 Object Oriented Programming - Assessment 1

Repository: https://github.com/Codesprout-91/A1-yli197

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
  ClinicSchedule.java
  Dietitian.java
  GeneralPractitioner.java
  HealthProfessional.java
  Nurse.java
  Patient.java
  Specialist.java
  TimeSlot.java
test/
  A1ContractTest.java
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
javac --release 17 -Xlint:all -Werror -d bin $sources
java -cp bin AssignmentOne
```

The demonstration ends with:

```text
Active appointments: 4
```

## Run the contract tests

The repository includes a dependency-free Java test program:

```powershell
$mainSources = (Get-ChildItem src\*.java).FullName
javac --release 17 -Xlint:all -Werror -d build $mainSources test\A1ContractTest.java
java -cp build A1ContractTest
```

The expected final message is:

```text
A1 CONTRACT TESTS PASSED
```

## Appointment rules

- The scripted application uses `MONDAY` as a deterministic simulated current day.
- A professional must be available on the current day.
- The same professional cannot be booked twice in the same time slot.
- A professional who does not work on the current day cannot be booked.
- The clinic opens at 08:00 and closes at 16:00.
- Bookable appointments start every 30 minutes from 08:00 through 15:30.
- `16:00` is the closing time and is not a bookable start time.
- Filtering is available by professional ID or patient phone number.
- Phone-only cancellation succeeds when exactly one appointment matches.
- Multiple matching appointments return an ambiguity warning.

## Schedule design

`ClinicSchedule` is the single source of truth for the simulated current day
and the fixed daily slots. It generates the half-hour schedule without
hard-coding every time. `Appointment` validates its time against this shared
schedule at construction time, so an invalid appointment cannot exist.

## Validation

- Professional IDs accept ASCII digits `0-9` only.
- Names cannot be blank.
- Patient phone numbers are normalised to ten digits and must start with `04`.
- Spaces and hyphens are removed before the phone number is stored.
- Appointment patient, professional, and time values cannot be null.
- Requested times must exist in `ClinicSchedule`.
- Operations that cannot succeed return a warning instead of corrupting state.

## Design choices

- The base `HealthProfessional` type allows new professional subclasses to be
  processed without changing appointment-management code.
- `ClinicSchedule` owns day names, current day, and time-slot generation, so
  schedule changes are localized rather than repeated in every class.
- Sorting works on a copy of the appointment list and preserves the manager's
  stored collection.
- The scripted `main` demonstration uses arrays and loops so more test data can
  be added without duplicating the workflow.
- The demonstration includes an unavailable professional so the current-day
  availability warning is visible in the console output.
- The assignment requires a scripted console demonstration rather than a menu,
  so this project does not contain a `Scanner`-driven interface.

## Java API choices

The project uses only `java.util.ArrayList` from the Java standard library and
no third-party dependencies.

- Working days are stored as validated uppercase `String` values.
- `TimeSlot` is a custom immutable value object using integer hour and minute.
- `ArrayList` stores working days, valid time slots, and appointments.
- `Comparable` defines appointment and time-slot ordering.
- Sorting uses a small insertion-sort loop instead of library sorting helpers.
- The source does not use `java.time`, `EnumSet`, `StringBuilder`, `List`
  interfaces, `Set` interfaces, or `Collections` utility methods.

## Verification

The Java sources compile under Java 17 with:

```powershell
javac --release 17 -Xlint:all -Werror -d bin (Get-ChildItem src\*.java).FullName
```

The test scenarios cover validation failures, polymorphism, sorting, schedule
membership, duplicate bookings, filtering, phone-only cancellation,
professional-and-time cancellation, unavailable professionals, and empty
results.

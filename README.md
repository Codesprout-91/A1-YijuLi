# A1-YijuLi

PROG2004 Object Oriented Programming - Assessment 1

This project is a console-based health appointment system. It contains no UI,
login, database, or external libraries.

## Requirements covered

- Abstract `HealthProfessional` superclass
- General Practitioner, Nurse, Dietitian, and Specialist subclasses
- Encapsulated validation and controlled access to object data
- Patient and Appointment value modelling
- Appointment validation and natural time ordering
- Add, duplicate detection, filtering, sorting, and cancellation
- Warnings for operations that cannot be completed
- A complete scripted demonstration in `AssignmentOne.main`

## Run in VS Code

1. Open this folder in VS Code.
2. Confirm that VS Code reports Java 17.
3. Open `src/AssignmentOne.java`.
4. Select `Run` above the `main` method.

The expected entry class is `AssignmentOne`. The source files intentionally do
not use a Java package.

## Run in a terminal

From this project folder:

```powershell
$sources = (Get-ChildItem src\*.java).FullName
javac -d bin $sources
java -cp bin AssignmentOne
```

## Clinic rules

- Appointments are for the current day only.
- The default schedule contains 30-minute slots from 08:00 to 16:00.
- A professional cannot be booked twice for the same time.
- A professional must be available on the current day.
- Cancellation is supported by professional ID plus time, or by patient phone.

When multiple appointments share one phone number, cancellation by phone
removes the earliest matching appointment.

## Design choices

- `AppointmentManager` accepts the `HealthProfessional` base type, so a new
  professional subclass does not require changes to appointment management.
- The manager can receive a different `List<LocalTime>` schedule through its
  constructor instead of relying only on the default clinic hours.
- The demonstration stores booking requests in an array and processes them in
  a loop. More sample appointments can therefore be added without repeating
  console code.
- Invalid IDs, blank names, bad phone formats, missing appointment data,
  unavailable staff, invalid times, and duplicate bookings are rejected before
  they can corrupt the stored data.
- The assignment brief requires a scripted `main` demonstration and explicitly
  excludes menus and a finished user interface. The project therefore does not
  contain a `Scanner` menu. A future console or GUI layer can call the existing
  validated methods without changing the domain classes.

## Project files

- `src/` - all Java source code
- `GenAI使用记录.md` - retained GenAI use record
- `Video讲稿.md` - screen-recording guide and speaking script
- `提交检查清单.md` - final submission checks

## Verification

The project compiles with Java 17 and `-Xlint:all` without warnings. Separate
test harnesses exercised valid object creation, invalid state rejection,
polymorphism, sorting, all filters, duplicate booking rejection, both
cancellation paths, a custom schedule, and an additional professional type.

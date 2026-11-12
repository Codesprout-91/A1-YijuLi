# PROG2004 A1 Video Presentation Plan

Target length: 4 minutes 30 seconds (maximum: 5 minutes)

Record the VS Code window and speak through the four OOP pillars. Show the file
and method named in each section while speaking.

## 00:00-00:25 - Introduction

Show `AssignmentOne.java`.

Speaking script:

> Hello, my name is Yiju Li. This is my PROG2004 Assessment 1. I built a
> console-based appointment system for a health service. The system models
> health professionals, patients, appointments, and an appointment manager.
> It demonstrates normal operations and error cases through the `main` method.

## 00:25-01:05 - Abstraction

Show `HealthProfessional.java`.

Speaking script:

> The first pillar is abstraction. `HealthProfessional` is an abstract class
> because a generic health professional should not exist without a specialty.
> It defines the shared concept and forces every concrete class to implement
> `getProfessionalType`, `performPrimaryDuty`, and `getTypeSpecificDetails`.
> This means the rest of the program can rely on a clear common contract.

## 01:05-01:50 - Encapsulation

Stay in `HealthProfessional.java`, then show `Patient.java`.

Speaking script:

> The second pillar is encapsulation. In `HealthProfessional`, fields such as
> the ID, name, and available days are private and final. Construction validates
> the numeric ID and working days, and the working-day set is made unmodifiable.
> Callers receive read-only access through getters. `Patient` follows the same
> approach for the name and mobile number. Therefore external code cannot put
> the objects into an invalid state.

## 01:50-02:35 - Inheritance

Show `GeneralPractitioner.java`, then briefly show the class declarations of
`Nurse`, `Dietitian`, and `Specialist`.

Speaking script:

> The third pillar is inheritance. All four professional types extend
> `HealthProfessional`, so they reuse the common ID, name, working-day storage,
> validation, equality, and textual display logic. Each subclass adds only its
> own information, such as a clinic room, qualification, nutrition specialty,
> or medical specialty. Constructor chaining with `super` also guarantees that
> common data is validated before subclass construction completes.

## 02:35-03:20 - Polymorphism

Show the `HealthProfessional[] professionals` loop in `AssignmentOne.java`.

Speaking script:

> The fourth pillar is polymorphism. In `AssignmentOne`, objects of all four
> professional types are stored in a single `HealthProfessional` array. The
> same `performPrimaryDuty` call produces different behaviour for a general
> practitioner, nurse, dietitian, and specialist. `Appointment` also accepts
> the general type, so it can work with any professional subtype through the
> same interface without subtype-specific code.

## 03:20-04:05 - Appointment management

Show `AppointmentManager.java` and run `AssignmentOne`.

Speaking script:

> `AppointmentManager` stores appointments in an `ArrayList`, validates the
> clinic schedule, prevents double bookings, filters appointments, and returns
> appointments in time order. `Appointment` implements `Comparable`, so sorting
> is based on `LocalTime`. The demonstration also shows filtering by
> professional ID and phone, cancellation by both input types, duplicate
> booking rejection, and cancellation of a missing appointment.

## 04:05-04:30 - Conclusion

Show the successful end of the console output.

Speaking script:

> The program compiles under Java 17 and runs the complete normal and error-case
> demonstration. This design separates responsibilities, reuses common code,
> protects object state, and allows all professional types to collaborate
> through one common abstraction. Thank you for watching.

## Recording checklist

- Keep the final recording below five minutes.
- Mention all four pillars by name.
- Point to the related class or method while explaining each pillar.
- Explain why each design choice was made, not only what the code does.
- Run `AssignmentOne` and show the final active-appointment count.
- Upload the finished personal recording to SCU OneDrive.
- Ensure the sharing permission allows the tutor and assessor to open it.

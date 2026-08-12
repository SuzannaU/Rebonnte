# Implementation Plan - Minimal Error Handling

Currently, the app has no error handling for data operations (like Firestore updates). This plan introduces a minimal, robust pattern using a `DataResult` wrapper and UI state updates.

## User Review Required

> [!NOTE]
> This pattern will change the return types of your UseCases from `T` to `DataResult<T>`. You will need to update your ViewModels to handle these results.

## Proposed Changes

### [Domain Layer]

#### [NEW] [DataResult.kt](file:///C:/Users/eliza/Programmation/AndroidStudioProjects/Rebonnte/app/src/main/java/com/openclassrooms/rebonnte/domain/util/DataResult.kt)
Create a generic sealed interface to wrap data operations.

#### [MODIFY] [MedicineRepository.kt](file:///C:/Users/eliza/Programmation/AndroidStudioProjects/Rebonnte/app/src/main/java/com/openclassrooms/rebonnte/domain/repository/MedicineRepository.kt)
Update suspend functions to return `DataResult<Unit>` or `DataResult<Medicine?>`.

#### [MODIFY] [UpdateMedicineUseCase.kt](file:///C:/Users/eliza/Programmation/AndroidStudioProjects/Rebonnte/app/src/main/java/com/openclassrooms/rebonnte/domain/useCase/UpdateMedicineUseCase.kt)
Propagate the `DataResult` from the repository to the UI.

---

### [Data Layer]

#### [MODIFY] [MedicineRepositoryFirestoreImpl.kt](file:///C:/Users/eliza/Programmation/AndroidStudioProjects/Rebonnte/app/src/main/java/com/openclassrooms/rebonnte/data/repositoryImpl/MedicineRepositoryFirestoreImpl.kt)
Implement `try-catch` logic to catch Firestore/Network exceptions and return `DataResult.Error`.

---

### [UI Layer]

#### [MODIFY] [AddMedicineViewModel.kt](file:///C:/Users/eliza/Programmation/AndroidStudioProjects/Rebonnte/app/src/main/java/com/openclassrooms/rebonnte/ui/addMedicine/AddMedicineViewModel.kt)
Handle the `DataResult` and update the `SaveState` to include an `Error` state.

## Verification Plan

### Manual Verification
- Trigger a network failure (Airplane mode) and attempt to save a medicine.
- Verify that the app doesn't crash and shows an appropriate error message (to be implemented in UI).

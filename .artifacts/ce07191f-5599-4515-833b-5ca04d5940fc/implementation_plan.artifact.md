# Implementation Plan - Add Medicine Screen

This plan covers the creation of a new screen to add a medicine, including UI implementation, ViewModel refinement, and navigation wiring.

## User Review Required

> [!NOTE]
> I will be updating the `FloatingActionButton` in `MedicineListScreen` to navigate to the new `AddMedicineScreen` instead of calling `addRandomMedicine()`.

## Proposed Changes

### Resources

#### [MODIFY] [strings.xml](file:///C:/Users/eliza/Programmation/AndroidStudioProjects/Rebonnte/app/src/main/res/values/strings.xml)
Add labels and error messages for the Add Medicine form.

### Domain / ViewModel

#### [MODIFY] [AddMedicineViewModel.kt](file:///C:/Users/eliza/Programmation/AndroidStudioProjects/Rebonnte/app/src/main/java/com/openclassrooms/rebonnte/ui/addMedicine/AddMedicineViewModel.kt)
Fix logic errors in the `validate()` function where digit validation flags were inverted.

### UI

#### [MODIFY] [AddMedicineScreen.kt](file:///C:/Users/eliza/Programmation/AndroidStudioProjects/Rebonnte/app/src/main/java/com/openclassrooms/rebonnte/ui/addMedicine/AddMedicineScreen.kt)
Implement the `AddMedicineScreen` composable with a form containing:
- Name field (with error handling for empty or too long names)
- Aisle number field (with error handling for non-digit input)
- Current stock field (with error handling for non-digit input)
- "Add Medicine" button
- Loading state handling and navigation back on success.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/eliza/Programmation/AndroidStudioProjects/Rebonnte/app/src/main/java/com/openclassrooms/rebonnte/ui/MainActivity.kt)
Register `ADD_MEDICINE_ROUTE` in the `NavHost`.

#### [MODIFY] [MedicineListScreen.kt](file:///C:/Users/eliza/Programmation/AndroidStudioProjects/Rebonnte/app/src/main/java/com/openclassrooms/rebonnte/ui/medicineList/MedicineListScreen.kt)
Update `FloatingActionButton` to navigate to `AddMedicineScreen`.

## Verification Plan

### Manual Verification
- Deploy the app to a device/emulator.
- Navigate to the Medicine List.
- Click the "+" FAB.
- Fill out the form.
- Test validation by entering invalid data (empty name, non-digit stock/aisle).
- Submit the form and verify that it navigates back to the list and the new medicine appears.

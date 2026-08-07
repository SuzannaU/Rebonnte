# Fix placeholder appearing in Aisle Number field

The user sees `{aisleNumber}` in the aisle number field when navigating to `AddMedicineScreen` from `MedicineListScreen`. This is because the navigation call is using the route template string `"addMedicine?aisleNumber={aisleNumber}"` literally, instead of providing a value or using the base route.

## Proposed Changes

### UI Navigation

#### [MODIFY] [MainActivity.kt](file:///C:/Users/eliza/Programmation/AndroidStudioProjects/Rebonnte/app/src/main/java/com/openclassrooms/rebonnte/ui/MainActivity.kt)

Change the navigation call in `MedicineListScreen`'s `onAddMedicineClick` to navigate to the base route `"addMedicine"`. Since the `aisleNumber` argument is optional with a default value of `""`, this will correctly result in an empty string in the ViewModel.

### ViewModel Cleanup

#### [MODIFY] [AddMedicineViewModel.kt](file:///C:/Users/eliza/Programmation/AndroidStudioProjects/Rebonnte/app/src/main/java/com/openclassrooms/rebonnte/ui/addMedicine/AddMedicineViewModel.kt)

Change the default value in `savedStateHandle` retrieval from `"Aisle number"` to `""` to be consistent with the navigation argument's default value and prevent any other accidental placeholder displays.

## Verification Plan

### Manual Verification
- Deploy the app.
- Go to `MedicineListScreen`.
- Click the FAB to add a medicine.
- Verify that the Aisle Number field is empty (or shows the hint if applicable) instead of `{aisleNumber}`.

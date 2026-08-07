# Refactor History mapping to use String Resources

This plan introduces the `UiText` pattern to handle string resources in the UI mapping layer. This avoids hard-coded strings and keeps the mappers independent of the Android `Context`, which is better for testing and architecture.

## Proposed Changes

### [UI Utilities]

#### [NEW] [UiText.kt](file:///C:/Users/eliza/Programmation/AndroidStudioProjects/Rebonnte/app/src/main/java/com/openclassrooms/rebonnte/ui/util/UiText.kt)
Create a `UiText` sealed class to handle both hardcoded strings and string resources with arguments.

### [Resources]

#### [MODIFY] [strings.xml](file:///C:/Users/eliza/Programmation/AndroidStudioProjects/Rebonnte/app/src/main/res/values/strings.xml)
Add the following string resources:
- `history_creation`: "Creation"
- `history_no_details`: "No details available"
- `history_aisle_number`: "Aisle number"
- `history_name`: "Name"
- `history_stock`: "Stock"

### [UI Models]

#### [MODIFY] [HistoryUi.kt](file:///C:/Users/eliza/Programmation/AndroidStudioProjects/Rebonnte/app/src/main/java/com/openclassrooms/rebonnte/ui/model/HistoryUi.kt)
Change the `details` field type from `String` to `UiText`.

#### [MODIFY] [UiMappers.kt](file:///C:/Users/eliza/Programmation/AndroidStudioProjects/Rebonnte/app/src/main/java/com/openclassrooms/rebonnte/ui/model/UiMappers.kt)
Update `History.toUi()` to return `UiText` using the new string resources.

### [UI Components]

#### [MODIFY] [MedicineDetailScreen.kt](file:///C:/Users/eliza/Programmation/AndroidStudioProjects/Rebonnte/app/src/main/java/com/openclassrooms/rebonnte/ui/medicineDetail/MedicineDetailScreen.kt)
Update `HistoryItem` to resolve `UiText` and update previews.

## Verification Plan

### Automated Tests
- Build the project to ensure no compilation errors.
- (Optional) Add a unit test for `History.toUi()` if it doesn't exist.

### Manual Verification
- Deploy the app.
- Check the History section in Medicine Details to ensure strings are correctly displayed.

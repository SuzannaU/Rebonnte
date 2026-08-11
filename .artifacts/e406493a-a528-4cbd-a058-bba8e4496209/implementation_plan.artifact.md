# Align XML Theme with Compose Theme for AuthUI

This plan aims to synchronize the XML-based theme (used by Firebase AuthUI) with the existing Jetpack Compose Material 3 theme defined in `Theme.kt` and `Color.kt`.

## Proposed Changes

### [Resources]

#### [MODIFY] [colors.xml](file:///C:/Users/eliza/Programmation/AndroidStudioProjects/Rebonnte/app/src/main/res/values/colors.xml)
- Add hex values for both Light and Dark theme colors extracted from `Color.kt`.

#### [MODIFY] [themes.xml](file:///C:/Users/eliza/Programmation/AndroidStudioProjects/Rebonnte/app/src/main/res/values/themes.xml)
- Update `Theme.Rebonnte` to inherit from `Theme.Material3.DayNight.NoActionBar`.
- Map Material 3 attributes (`colorPrimary`, `colorOnPrimary`, `android:windowBackground`, etc.) to the light theme colors.
- Ensure `Theme.Rebonnte.Login` inherits from this updated base theme.

#### [NEW] [themes.xml (night)](file:///C:/Users/eliza/Programmation/AndroidStudioProjects/Rebonnte/app/src/main/res/values-night/themes.xml)
- Create a dark mode variation of the theme mapping attributes to the dark theme colors.

## Verification Plan

### Manual Verification
- Deploy the app and trigger the Firebase AuthUI login flow.
- Verify that the login screen uses the same primary color, background color, and text colors as the rest of the Compose app.
- Toggle System Dark Mode and verify that AuthUI correctly switches to the dark color scheme.

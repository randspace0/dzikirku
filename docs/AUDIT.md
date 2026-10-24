# Audit (2026-10-23)

Scope: static review of the `app` module (XML views + DataBinding, Kotlin, Gradle 7.2).
Nothing was built or run: the dev machine only has JDK 21 and the wrapper needs JDK 16 or older.
Items marked **fixed** were fixed in a PR; the rest are open.

## UI/UX (ui-ux-pro-max checklist)

| Pri | Finding | Status |
|-----|---------|--------|
| Critical | 104 `ImageView`s had no `contentDescription`; 9 of them are clickable icon-only controls | 9 clickable **fixed** (#3). About 95 non-clickable remain: decide per icon between decorative (`importantForAccessibility="no"`) and labelled |
| Critical | Clickable icons were 32dp, below the 48dp minimum | those 9 **fixed** (#3). Many other 24-38dp views exist (about 38 at 32dp, 33 at 24dp); check which are clickable |
| High | 38 layouts contain hardcoded `android:text`/`hint` (e.g. "Try Again", "Share", "Pencarian") | open; move to resources |
| High | Content descriptions live in `values/` only. Text is translated by custom `locale_*.json`, not Android resources, so a11y labels ignore the in-app language | open; add `values-in/` or route through the locale layer |
| Medium | Placeholder strings shipped in `strings.xml` (`hello_blank_fragment`, `appwidget_text` "EXAMPLE", `title_activity_bottom`) and "Hello"/"Title" in layouts | open; delete if unused |
| Medium | Dark theme: `drawable-night` and `values-night` exist, but contrast was not measured | open |

## Security and privacy

| Pri | Finding | Status |
|-----|---------|--------|
| High | `usesCleartextTraffic="true"`; the code has no `http://` endpoints (API base URLs are https) | **fixed** in the PR that adds this file |
| High | `allowBackup="true"` backs up the Room DB (notes, bookmarks) to Google Drive. Possibly wanted, but it needs `dataExtractionRules`/`fullBackupContent` and a privacy-policy mention | open; decide |
| High | `google-services.json` and `your_web_client_id` are committed and still point at the old Firebase project/app registration | open; replace after registering the new app in Firebase. Restrict the API key by package + SHA-1 |
| Medium | Release build: `isMinifyEnabled = false`, no `shrinkResources`, no signing config | open; enable R8 and test (Room, Firebase need keep rules) |
| Medium | `WRITE_EXTERNAL_STORAGE` + `requestLegacyExternalStorage` are obsolete. The share-image flow requests `WRITE_EXTERNAL_STORAGE`, which Android 13+ never grants, so **sharing an image does nothing on Android 13+**. | open; save via MediaStore/FileProvider and drop the permission |
| Medium | `PraytimeForegroundService` was never started: **removed** along with the `FOREGROUND_SERVICE` permission |
| Medium | `POST_NOTIFICATIONS` declared and requested at runtime in `MainActivity` | **fixed** |

## Build and maintainability

| Pri | Finding |
|-----|---------|
| High | **Partly fixed**: Gradle 8.4, AGP 8.2.2, Kotlin 1.9.10, `compileSdk`/`targetSdk` 34. Play needs a newer target for new apps (see the checklist). Still old: `work-runtime 2.7.0-alpha05`, `firebase-analytics 17.2.2` beside Firebase BoM 30.1.0 |
| Medium | Still-old deps: `work-runtime 2.7.0-alpha05`, `firebase-analytics 17.2.2`, `play-services-maps 17.0.1`. `com.google.android.play:core:1.10.2` (blocks Play uploads) was replaced; unused `InAppUpdater` and the alpha `hilt-lifecycle-viewmodel` were removed |
| Medium | 5 `GlobalScope` uses, plus `!!` and `printStackTrace` calls (31 grep hits in total): leaks and crashes under lifecycle changes |
| Medium | Tests are only the template `Example*Test` files; no coverage of prayer-time maths, Room migrations, or the locale layer |
| Low | Stale committed artefacts: `.idea/`, `buildSrc/build/` (contains `/Users/mayburger/...` paths), `backup/` |
| Low | `bindView.kt` declares package `com.wagyufari.gojekuiclone.util.ext` but sits in `util/` |
| Low | Repo URLs in `CONTRIBUTING.md` and `.github/ISSUE_TEMPLATE/*` still point at `wagyufari/dzikirqu-android` |

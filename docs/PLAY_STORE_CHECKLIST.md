# Play Store release checklist

App is unpublished. Package: `id.gritlabs.dzikirku`. Tick items as done.
Play policy numbers change often. Re-verify every number below in Play Console (Policy status) before submitting.

## 0. Blockers in this repo

- [ ] Get a JDK that Gradle 7.2 supports (or upgrade the toolchain) and confirm `./gradlew :app:assembleProdDebug` builds. The package rename (#2) was never compiled.
- [ ] Raise `targetSdk`/`compileSdk` to the level Play currently requires for new apps (API 35 in 2025; expect newer now). This forces AGP, Gradle, Kotlin and dependency upgrades, then a regression pass on: scoped storage, notification permission, exact alarms and the boot receiver, edge-to-edge insets, PendingIntent mutability flags, and `exported` on every component with an intent-filter.
- [ ] Build an Android App Bundle (`./gradlew :app:bundleProdRelease`). APKs are not accepted for new apps.
- [ ] Release signing: create an upload keystore, keep it out of git (password manager), enrol in Play App Signing. Add a `signingConfigs` entry that reads from a gitignored `keystore.properties`.
- [ ] Enable R8 (`isMinifyEnabled = true`, `shrinkResources = true`), add keep rules, and exercise the whole app on a release build.
- [ ] Register `id.gritlabs.dzikirku` (and `.dev`) in Firebase, add the release and Play App Signing SHA-1/256 fingerprints, replace `app/google-services.json`, update `your_web_client_id`, restrict the API key to the package + SHA.
- [ ] Check native libraries for 16 KB page-size support (required when targeting recent Android versions).
- [ ] Android 13+: `POST_NOTIFICATIONS` is declared; add the runtime request (e.g. during onboarding) once `targetSdk` is 33+.
- [ ] Android 14+: give `PraytimeForegroundService` a `foregroundServiceType` and the matching `FOREGROUND_SERVICE_<TYPE>` permission; prepare the Play foreground-service declaration.
- [ ] Remove unused permissions: `WRITE/READ_EXTERNAL_STORAGE` (use MediaStore/SAF), `SET_ALARM` and `WAKE_LOCK` if unused.
- [ ] Decide on `allowBackup` and add backup rules; keep it consistent with the privacy policy.
- [ ] Set `versionCode`/`versionName` for the first release.

## 1. Legal and content rights (this is a fork)

- [ ] The repo is GPL-3.0 (`LICENSE`). Publishing is allowed, but keep the licence and attribution and offer the corresponding source (link the GitHub repo in the listing).
- [ ] Confirm redistribution rights for each content source credited in `README.md`:
  - Quran.com images, Tanzil.net text (unmodified text + attribution required), Quranwbw.com, Sunnah.com (check terms), Praytimes.org (keep header).
  - *Buku Do'a dan Wirid* (Ustadz Yazid bin Abdul Qadir Jawas) is a copyrighted book. Get written permission from the author/publisher or remove that content. This is the biggest takedown risk.
- [ ] Do not ship the original app's icon or logo. Replace `ic_logo_144_circle` and the launcher icons if they are the original artwork.
- [ ] Choose the developer name shown on the listing.

## 2. Play Console account

- [ ] Create a developer account (one-time fee). Personal accounts created after Nov 2023 must run a closed test with a minimum number of opted-in testers for 14 consecutive days before applying for production. Check the current numbers in Console. Organization accounts are exempt but need a D-U-N-S number.
- [ ] Verify identity, contact email, phone and, if asked, address.
- [ ] Create the app: name "Dzikirku", default language, App (not Game), Free.

## 3. Store listing

- [ ] Short description (80 chars) and full description (4000 chars). Describe features truthfully: prayer times and adhan, dhikr/prayer collections, Quran, qibla, notes. Do not claim to be official or religiously authoritative.
- [ ] App icon 512x512 PNG and feature graphic 1024x500.
- [ ] Phone screenshots (at least 2); add 7"/10" tablet shots if you want tablet support featured.
- [ ] Category (Lifestyle or Books & Reference) and tags.
- [ ] Contact email, website, privacy policy URL.
- [ ] Listings in Indonesian and English (the app ships `locale_bahasa.json` and `locale_english.json`).

## 4. Policy forms (App content)

- [ ] Privacy policy: public URL, covering location (prayer times, qibla), Firebase Analytics, Crashlytics, Auth, Firestore/Storage/Database, and any notes sync. Link it inside the app too.
- [ ] Data safety: declare approximate and precise location (used for app functionality), crash logs and diagnostics (Crashlytics), analytics identifiers (Firebase Analytics), account info if Google sign-in is used, user content (notes) if synced to Firebase. State encryption in transit and the deletion path.
- [ ] Account deletion: if users can create accounts (Firebase Auth / Google sign-in), provide an in-app deletion path and a web URL.
- [ ] Permission declarations: location is foreground only (no background location requested). Justify any `SCHEDULE_EXACT_ALARM`/`USE_EXACT_ALARM` you add.
- [ ] Ads: "No ads" (none found in dependencies).
- [ ] Content rating questionnaire (IARC). Answer honestly about user-shared notes.
- [ ] Target audience 13+ or all ages; not primarily for children.
- [ ] App access: provide test credentials if any feature needs login.
- [ ] Government, financial, health and news declarations: all "No".

## 5. Testing before submit

- [ ] Internal testing track with the `bundleProdRelease` build on at least Android 8, 12 and 14+ and one low-RAM device.
- [ ] Adhan alarms fire after reboot, after force-stop, after a timezone change and with battery saver on; the notification-permission-denied path works.
- [ ] Location denied, GPS off and no-network flows for prayer times and qibla.
- [ ] Both widgets add, update and survive process death.
- [ ] Deep links: the manifest declares `dzikirqu.com` hosts. If you do not control that domain, remove them or set up your own domain and assetlinks.
- [ ] Notes: create, export, import, and Room migration from the previous schema.
- [ ] Dark mode, RTL Arabic text, 200% font scale, TalkBack on the home screen.
- [ ] Review the Play pre-launch report and fix crashes and accessibility warnings.

## 6. Release

- [ ] Internal testing, then closed testing (14 days if required), then production with a staged rollout (e.g. 10%).
- [ ] Upload the R8 mapping file and native debug symbols (the Crashlytics plugin can do this).
- [ ] Watch Android vitals and Crashlytics for the first week; keep a hotfix path ready.

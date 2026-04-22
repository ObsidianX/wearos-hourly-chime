# Watch Chime

A Wear OS app that beeps twice on the hour, every hour.

## How it works

When you launch the app it schedules an exact alarm for the next top of the hour. When that alarm fires, the watch plays two short beeps through the alarm audio stream and immediately reschedules for the following hour. A boot receiver re-registers the alarm after the watch restarts, so the chime survives reboots without reopening the app.

## Requirements

- Wear OS 2.0+ (API 26+)
- Android Studio Meerkat or later
- A Wear OS device or emulator

## Build

Open the project root in Android Studio. Gradle will sync automatically.

```
./gradlew :app:assembleDebug
```

Install to a connected watch:

```
./gradlew :app:installDebug
```

## First launch

Open the app once to activate the chime. On Android 12 (API 31–32), the OS will prompt you to grant the **Schedule Exact Alarms** permission — tap **Allow**. On Android 13+ the permission is granted automatically. After that, the app runs entirely in the background and you do not need to keep it open.

## Configuration

Configuration options:
- Enabled
  - Chimes will only play if enabled
- Disable When Locked
  - Skips a scheduled chime if the device is locked on the hour
- Volume
  - 10% - 100%, increments of 10

You can test the volume with the `Test Chime` button, and test the locked state with the `Test Scheduled` button, which will set the next chime for +10 seconds.

## Permissions

| Permission | Reason |
|---|---|
| `USE_EXACT_ALARM` | Fire at precisely HH:00:00 (API 33+, auto-granted) |
| `SCHEDULE_EXACT_ALARM` | Fire at precisely HH:00:00 (API 31–32, requires user grant) |
| `RECEIVE_BOOT_COMPLETED` | Reschedule after reboot |
| `WAKE_LOCK` | Keep the CPU awake for the ~1.5 s needed to play both beeps |

# Contributing

Bug reports and focused pull requests are welcome.

## Before opening a change

- Check that the issue has not already been reported.
- Describe the current behavior, expected behavior, and reproduction steps.
- Keep changes limited to one problem or feature.

## Verify your change

Use Java 21 and run:

```shell
./gradlew clean test buildPlugin
```

For animation changes, open **Settings > Appearance & Behavior > Angelina Progress Bar** and check both live previews at normal and HiDPI scaling.

New animation resources must be transparent, animated SVG files placed in `src/main/resources/com/angelinaprogress/intellij/characters/`.

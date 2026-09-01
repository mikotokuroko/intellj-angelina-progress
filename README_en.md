<div align="center">

<img src="./src/main/resources/META-INF/pluginIcon.svg" alt="Angelina Progress Bar icon" width="80">

# Angelina Progress Bar

Animated IntelliJ progress bars featuring **Angelina the Mellow Wish** (予愿安洁莉娜).

[English](README_en.md) | [简体中文](README.md)

<img src="https://web.hycdn.cn/upload/image/20260724/1e8b24d5d0d9f2423b7429ccdd71d503.jpg" alt="Angelina the Mellow Wish" width="800">

</div>

## Preview

| Broom Ride | Delivery Run | Diving |
|:---:|:---:|:---:|
| <img src="./src/main/resources/com/angelinaprogress/intellij/characters/broom.svg" alt="Angelina broom ride animation" width="260"> | <img src="./src/main/resources/com/angelinaprogress/intellij/characters/run.svg" alt="Angelina delivery run animation" width="260"> | <img src="./src/main/resources/com/angelinaprogress/intellij/characters/dive.svg" alt="Angelina diving animation" width="260"> |

## Features

- Replaces standard IntelliJ Platform progress bars with one of three animated SVGs.
- Keeps the artwork crisp on HiDPI displays and inside the progress-bar bounds.
- Includes a live settings preview plus controls for motion, transparency, tooltips, and height.
- Works with determinate and indeterminate progress bars.

## Install

1. Build the plugin with Java 21:

   ```shell
   ./gradlew clean test buildPlugin
   ```

2. In IntelliJ IDEA, open **Settings > Plugins**, choose the gear menu, then **Install Plugin from Disk**.
3. Select the ZIP in `build/distributions/` and restart the IDE when prompted.

## Configure and test

Open **Settings > Appearance & Behavior > Angelina Progress Bar** to choose an animation and use the live preview. To see it elsewhere in the IDE, trigger a real background task such as project indexing, Gradle sync, dependency download, or code analysis.

## Credits

The code for the progress bar itself was initially adapted from [kagof/intellij-pokemon-progress](https://github.com/kagof/intellij-pokemon-progress).

- This plugin is of course heavily dependent on JetBrains' IntelliJ SDK.
- Character naming reference: [Angelina the Mellow Wish / 予愿安洁莉娜](https://prts.wiki/w/予愿安洁莉娜).

> [!CAUTION]
> 《明日方舟》素材版权归 Hypergryph 所有，PRTS 资料遵循其站内许可。
> 本项目仅用于个人学习与自用，请勿用于商业发布。

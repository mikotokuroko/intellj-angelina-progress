<div align="center">

<img src="./src/main/resources/META-INF/pluginIcon.svg" alt="Angelina Progress Bar 图标" width="80">

# Angelina Progress Bar

让 IntelliJ 的进度条显示 **予愿安洁莉娜（Angelina the Mellow Wish）** 动画。

[English](README_en.md) | [简体中文](README.md)

<img src="https://web.hycdn.cn/upload/image/20260724/1e8b24d5d0d9f2423b7429ccdd71d503.jpg" alt="予愿安洁莉娜" width="800">

</div>

## 动画预览

| 扫帚飞行 | 极速送达 | 潜水 |
|:---:|:---:|:---:|
| <img src="./src/main/resources/com/angelinaprogress/intellij/characters/broom.svg" alt="予愿安洁莉娜扫帚飞行动画" width="260"> | <img src="./src/main/resources/com/angelinaprogress/intellij/characters/run.svg" alt="予愿安洁莉娜跑步动画" width="260"> | <img src="./src/main/resources/com/angelinaprogress/intellij/characters/dive.svg" alt="予愿安洁莉娜潜水动画" width="260"> |

## 功能

- 使用三种可选的 SVG 动画替换 IntelliJ Platform 的标准进度条。
- 在 HiDPI 屏幕上保持清晰，并确保角色完整显示在进度条范围内。
- 设置页提供实时预览，以及速度、透明度、提示文字和高度选项。
- 同时支持确定进度和不确定进度两种进度条。

## 安装

1. 使用 Java 21 构建插件：

   ```shell
   ./gradlew clean test buildPlugin
   ```

2. 在 IntelliJ IDEA 中打开 **设置 > 插件**，点击齿轮菜单，再选择 **从磁盘安装插件**。
3. 选择 `build/distributions/` 中的 ZIP 文件，并在提示时重启 IDE。

## 设置与测试

打开 **设置 > 外观与行为 > Angelina Progress Bar**，选择动画并查看实时预览。若要在 IDE 的其他位置看到进度条，可触发项目索引、Gradle 同步、依赖下载或代码分析等后台任务。
F
## 致谢

进度条核心代码最初改编自 [kagof/intellij-pokemon-progress](https://github.com/kagof/intellij-pokemon-progress)。

- 本插件高度依赖 JetBrains IntelliJ SDK。
- 角色名称参考：[予愿安洁莉娜 / Angelina the Mellow Wish](https://prts.wiki/w/予愿安洁莉娜)。

> [!CAUTION]
> 《明日方舟》素材版权归 Hypergryph 所有，PRTS 资料遵循其站内许可。
> 本项目仅用于个人学习与自用，请勿用于商业发布。

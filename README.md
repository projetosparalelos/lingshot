[![Kotlin Version](https://img.shields.io/badge/Kotlin-1.9.x-blue.svg)](https://kotlinlang.org)
[![AGP](https://img.shields.io/badge/AGP-8.x-blue?style=flat)](https://developer.android.com/studio/releases/gradle-plugin)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-blue?style=flat)](https://gradle.org)
![build](https://github.com/CharlesMoreira1/lingshot/actions/workflows/build-ci.yml/badge.svg)
<a href="https://github.com/diffplug/spotless"><img src="https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg" alt="spotless"></a>

<img src="https://github.com/CharlesMoreira1/lingshot/assets/55266908/5786aa7d-3aae-41a1-9799-c48024f324f9" width="200">

# Lingshot

Welcome to Lingshot, your ultimate companion for smart and effective language learning! This revolutionary app combines powerful features to help you master new languages in a fun and productive way.

📷 Screen Capture: Snap screenshots of any text you come across, whether in a book, website, or even in the real world. Lingshot is always ready to capture knowledge.

📖 Instant OCR: Our advanced Optical Character Recognition (OCR) instantly turns your captures into editable text within seconds. No more typing!

🌐 Instant Translation with ChatGPT: Connect to ChatGPT, an intelligent language assistant, to quickly translate captured text into dozens of languages. Learn with fluency and authenticity.

🧠 Spaced Repetition Memorization: Save your translations and phrases to create a personalized study plan using the spaced repetition method. Reinforce your memories and accelerate learning.

🗺️ Multilingual Support: Learn a variety of languages, from the most popular to the most exotic. Explore the linguistic diversity of the world.

<img src="https://github.com/CharlesMoreira1/lingshot/assets/55266908/c4db7ed7-86e1-4884-b724-25ec6d393e11" width="186">  <img src="https://github.com/CharlesMoreira1/lingshot/assets/55266908/03ab419c-3dc9-43f9-ac54-caab293c78db" width="186"> <img src="https://github.com/CharlesMoreira1/lingshot/assets/55266908/43120296-1fd0-46eb-b519-6ac8e8f043a8" width="186">  <img src="https://github.com/CharlesMoreira1/lingshot/assets/55266908/bd778f50-2fed-4bb9-8708-8b127bc8e1c4" width="186"> <img src="https://github.com/CharlesMoreira1/lingshot/assets/55266908/e2854fae-eb5e-4e02-9061-b0222671b2dc" width="186">  <img src="https://github.com/CharlesMoreira1/lingshot/assets/55266908/6342d849-6b70-4430-a528-a8ff08603565" width="186"> <img src="https://github.com/CharlesMoreira1/lingshot/assets/55266908/127698cd-d09f-488e-a5ce-35a26f60d8b6" width="186">  <img src="https://github.com/CharlesMoreira1/lingshot/assets/55266908/47dbc815-a291-4258-a995-958a7fd4ea02" width="186">

This project's primary focus is to promote a modular, scalable, maintainable, and testable architecture. It incorporates a cutting-edge tech stack and embodies the best practices in software development. While the application may seem straightforward, it encompasses all the essential components that form the foundation for a robust, large-scale application.

The design principles and architectural choices applied in this project are ideally suited for larger teams and extended application lifecycles. This application not only showcases functionalities but also serves as a testament to how well-structured and well-written code acts as a stable backbone for scalable and maintainable software development projects.

## Download

<a href='https://play.google.com/store/apps/details?id=com.lingshot.languagelearn'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width=240/></a>

## Tech-Stack

This project takes advantage of best practices, and many popular libraries and tools in the Android ecosystem.

* Tech-stack
  * [Kotlin](https://kotlinlang.org/)
    + [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)
    + [Kotlin Flow](https://kotlinlang.org/docs/flow.html)
    + [Kotlin Symbol Processing](https://kotlinlang.org/docs/ksp-overview.html)
    + [Kotlin Serialization](https://kotlinlang.org/docs/serialization.html)
    + [Kotlin Immutable Collections](https://github.com/Kotlin/kotlinx.collections.immutable)
  * [Retrofit](https://square.github.io/retrofit/)
  * [Jetpack](https://developer.android.com/jetpack)
    * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle)
    * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
    * [Room](https://developer.android.com/jetpack/androidx/releases/room)
  * [Hilt](https://dagger.dev/hilt/)
  * [Coil](https://github.com/coil-kt/coil)
  * [Lottie](http://airbnb.io/lottie)
* Modern Architecture
  * Single activity architecture
  * MVVM
  * [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
  * [Android Architecture components](https://developer.android.com/topic/libraries/architecture)
    ([ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
    , [Kotlin Flow](https://kotlinlang.org/docs/flow.html)
    , [Navigation](https://developer.android.com/jetpack/androidx/releases/navigation))
  * [Android KTX](https://developer.android.com/kotlin/ktx)
* UI
  * [Jetpack Compose](https://developer.android.com/jetpack/compose)
  * [Material Design 3](https://m3.material.io/)
    * [Dark Theme](https://material.io/develop/android/theming/dark)
    * [Dynamic Theming](https://m3.material.io/styles/color/dynamic-color/overview)
* ML Kit
  * [Text Recognition](https://developers.google.com/ml-kit/vision/text-recognition/v2)
  * [Text Speech](https://developer.android.com/reference/android/speech/tts/TextToSpeech)
  * [Language Identification](https://developers.google.com/ml-kit/language/identification)

* CI
  * [GitHub Actions](https://github.com/features/actions)
  * [Danger System](https://danger.systems/ruby/)
* Testing
  * [Paparazzi Snapshot](https://github.com/cashapp/paparazzi)
  * [Test Parameter Injector](https://github.com/google/TestParameterInjector)
* Static analysis tools (linters)
  * [Detekt](https://github.com/arturbosch/detekt)
  * [Spotless](https://github.com/diffplug/spotless)
  * [Compose Rules](https://github.com/twitter/compose-rules)
  * [Compose Lints](https://slackhq.github.io/compose-lints/rules/)
* Gradle
  * [Gradle Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html)
  * [Versions catalog](https://docs.gradle.org/current/userguide/platforms.html#sub:version-catalog)
  * [Type safe accessors](https://docs.gradle.org/7.0/release-notes.html)
* GitHub Boots
  * [Renovate](https://github.com/renovatebot/renovate)
* Other Tools
  * [Android Image Cropper](https://github.com/ArthurHub/Android-Image-Cropper)
  * [Balloon Compose](https://github.com/skydoves/Balloon)
  * [Markdown Compose](https://github.com/jeziellago/compose-markdown)
  * [Chucker Proxy](https://github.com/ChuckerTeam/chucker)

## 📃 License

```
Copyright 2023 Lingshot

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[![AGP](https://img.shields.io/badge/AGP-8.x-blue?style=flat)](https://developer.android.com/studio/releases/gradle-plugin)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-blue?style=flat)](https://gradle.org)
![build](https://github.com/CharlesMoreira1/lingshot/actions/workflows/build-ci.yml/badge.svg)
<a href="https://github.com/diffplug/spotless"><img src="https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg" alt="spotless"></a>

<img src="https://github.com/CharlesMoreira1/lingshot/assets/55266908/5786aa7d-3aae-41a1-9799-c48024f324f9" width="200">

# Lingshot

Lingshot is a simple yet powerful screenshot capture tool that makes multilingual communication more accessible than ever before. With this app, you can take a screenshot of any text you come across and instantly translate it into your preferred language.

Key Features:

📸 Easy Screen Capture: Quickly take a screenshot of any text on your device.

🔍 Automatic Text Recognition (OCR): The built-in OCR automatically recognizes text in your screenshots.

🌐 Instant Translation: Translate recognized text with just a tap.

💬 ChatGPT Integration: Our app leverages the powerful AI of ChatGPT for translation and context enhancement when needed.

🔒 Privacy Guaranteed: We value your privacy and ensure that your screenshots and translations remain secure and confidential.

<img src="https://github.com/CharlesMoreira1/lingshot/assets/55266908/9416ab15-433b-4dba-8fef-698c0b9b80d2" width="186">  <img src="https://github.com/CharlesMoreira1/lingshot/assets/55266908/d7261250-5957-4673-8309-625864d59242" width="186"> <img src="https://github.com/CharlesMoreira1/lingshot/assets/55266908/c72d2f64-1430-46d2-bf01-35f361f23e7d" width="186">  <img src="https://github.com/CharlesMoreira1/lingshot/assets/55266908/bd778f50-2fed-4bb9-8708-8b127bc8e1c4" width="186"> <img src="https://github.com/CharlesMoreira1/lingshot/assets/55266908/5913f6ec-0b72-4d36-88b3-c11eefb55d55" width="186">

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
  * [Image Cropper](https://github.com/CanHub/Android-Image-Cropper)
  * [Telephoto Zoomable](https://github.com/saket/telephoto)
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

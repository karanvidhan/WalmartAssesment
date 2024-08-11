# Astronomy Picture of the Day (APOD) Android App
Overview
This Android app displays NASA's Astronomy Picture of the Day (APOD) along with its title and explanation. The app handles both online and offline scenarios, ensuring that users can view the APOD even when they are not connected to the internet.

## Features
Fetches the Astronomy Picture of the Day from NASA's APOD API.
Displays the image, title, and explanation for the day.
Handles offline scenarios by showing the last viewed APOD if the network is unavailable.
Supports both image and video content, displaying videos in a WebView.
Allows users to scroll through the content if it's too large for the screen.
Getting Started
Prerequisites
Android Studio: Make sure you have the latest version of Android Studio installed.
API Key: You need to obtain a NASA API key to use the APOD API. You can get one from NASA's API portal.

## Dependencies
Add the following dependencies to your build.gradle (Module: app) file:

groovy
Copy code
dependencies {
    implementation "androidx.core:core-ktx:1.10.1"
    implementation "androidx.appcompat:appcompat:1.7.0"
    implementation "com.google.android.material:material:1.10.0"
    implementation "androidx.constraintlayout:constraintlayout:2.1.4"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.6.1"
    implementation "androidx.activity:activity-compose:1.8.1"
    implementation "androidx.compose.ui:ui:1.5.1"
    implementation "androidx.compose.material:material:1.5.1"
    implementation "androidx.compose.ui:ui-tooling-preview:1.5.1"
    implementation "androidx.compose.foundation:foundation:1.5.1"
    implementation "androidx.compose.runtime:runtime-livedata:1.5.1"
    implementation "androidx.navigation:navigation-compose:2.7.4"
    implementation "com.squareup.retrofit2:retrofit:2.9.0"
    implementation "com.squareup.retrofit2:converter-gson:2.9.0"
    implementation "io.coil-kt:coil-compose:2.4.0"
    implementation "androidx.room:room-ktx:2.5.3"
    kapt "androidx.room:room-compiler:2.5.3"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"
}

## How to Run the Code
Clone the repository:

sh
Copy code
### git clone https://github.com/karanvidhan/WalmartAssesment.git
Open the project in Android Studio.

### Add your NASA API key:

Open ApiClient.kt or wherever your API client is configured.
Replace the placeholder with your actual API key:
kotlin
Copy code
const val API_KEY = "YOUR_NASA_API_KEY"
Build and Run the app:

### Connect your Android device or start an emulator.
Click on the "Run" button in Android Studio to build and run the app.

## Improvement Areas

Caching is not properly implemented due to shortage of time

Enhanced Error Handling:

Improve error handling to cover more edge cases, such as API rate limits or specific HTTP error codes.
Caching Mechanism:

Implement a more robust caching mechanism that allows for storing multiple days' worth of APODs, not just the latest one.
Unit and UI Testing:

Add unit tests and UI tests to ensure the app works correctly under different scenarios.
User Interface Enhancements:

Consider adding animations, transitions, and other UI improvements to enhance the user experience.
Dark Mode Support:

Add support for dark mode to improve the app's usability in low-light conditions.
Localization:

Add support for multiple languages to make the app accessible to a wider audience.
Notifications:

Implement notifications to alert users when a new APOD is available.
Additional Features:

Consider adding features like saving favorite APODs, sharing them on social media, or setting the APOD as the device wallpaper.

## Contact
For any questions or issues, feel free to contact me at karanvidhan@gmail.com


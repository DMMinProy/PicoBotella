plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {

    namespace = "com.example.picobotella"

    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.picobotella"
        minSdk = 24
        targetSdk = 35

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation("com.airbnb.android:lottie:6.3.0")

    implementation("com.google.android.material:material:1.11.0")

    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("androidx.fragment:fragment-ktx:1.8.0")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("io.coil-kt:coil:2.6.0")

    implementation(
        "androidx.coordinatorlayout:coordinatorlayout:1.2.0"
    )

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(libs.material)
    //DOOM
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation(
        "androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")


    testImplementation(libs.junit)

    androidTestImplementation(
        libs.androidx.espresso.core
    )

    androidTestImplementation(
        libs.androidx.junit
    )
}
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.dev.ambatoplant"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dev.ambatoplant"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", "\"https://newsapi.org\"")
        buildConfigField("String", "API_KEY", "\"INPUT_YOUR_API_KEY\"")

        // Room export schema config (optional)
        buildConfigField("boolean", "ROOM_EXPORT_SCHEMA", "false")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        mlModelBinding = true
        buildConfig = true
    }

    // Optionally, specify schema location for Room

    configurations.all {
        resolutionStrategy {
            // Memaksa Gradle menggunakan versi tertentu dari tensorflow-lite-api
            force ("org.tensorflow:tensorflow-lite-api:2.13.0")
        }
    }

    dependencies {
        // AndroidX dependencies
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.activity:activity:1.9.0")
        implementation("com.google.mlkit:image-labeling:17.0.2")

        // Jangan tambahkan litert-api jika hanya menggunakan tensorflow-lite-api
        // implementation(libs.litert.support.api) // Hapus ini jika tidak perlu

        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.2.1")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

        // TensorFlow Lite dependencies (pilih salah satu)
        implementation("org.tensorflow:tensorflow-lite-task-vision:0.4.4")
        implementation("org.tensorflow:tensorflow-lite-metadata:0.4.4")
        implementation("org.tensorflow:tensorflow-lite-gpu:2.9.0")

        // Retrofit for API calls
        val retrofitVersion = "2.11.0"
        implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
        implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
        implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

        // Room for database operations
        val roomVersion = "2.6.1"
        implementation("androidx.room:room-runtime:$roomVersion")
        implementation("androidx.room:room-ktx:$roomVersion")
        ksp("androidx.room:room-compiler:$roomVersion")
        ksp("androidx.room:room-ktx:$roomVersion")

        // Koin for dependency injection
        val koinVersion = "3.5.0"
        implementation("io.insert-koin:koin-android:$koinVersion")
        implementation("io.insert-koin:koin-core:$koinVersion")

        // Lifecycle components
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.3")

        // Image cropping library
        implementation("com.github.jens-muenker:uCrop-n-Edit:3.0.6")

        // Image loading library
        implementation("com.github.bumptech.glide:glide:4.16.0")

        // Lottie animation library
        implementation("com.airbnb.android:lottie:6.1.0")
    }
}
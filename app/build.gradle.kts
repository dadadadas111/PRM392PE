plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.prm392pe"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.prm392pe"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    
    // RecyclerView
    implementation(libs.recyclerview)
    
    // CardView
    implementation(libs.cardview)
    
    // Fragment
    implementation(libs.fragment)
    
    // Image loading (Glide)
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
    
    // Permission handling
    implementation(libs.activity)
    implementation(libs.fragment)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
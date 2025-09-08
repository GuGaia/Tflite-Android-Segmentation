plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.gustavo.tflite.classification"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.gustavo.tflite.classification"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    // Se você estiver usando Compose:
    buildFeatures {
        compose = true
        viewBinding = true
    }

    // O Compose Compiler fica estável nessa versão; se não usar Compose, remova este bloco
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    // **Aqui está a forma compatível do noCompress**
    androidResources {
        // impede que o aapt comprima .tflite
        noCompress += "tflite"
    }

    // (opcional) se quiser manter excludes de recursos
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // ======= AndroidX básicos (sem version catalog) =======
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.activity:activity-compose:1.9.2") // ok mesmo sem Compose UI

    // ======= Jetpack Compose (se não usar, pode remover este bloco) =======
    implementation(platform("androidx.compose:compose-bom:2024.09.02"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    debugImplementation("androidx.compose.ui:ui-tooling")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.02"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // ======= Views clássicas (se usar layouts com ConstraintLayout/ViewBinding) =======
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // ======= Testes =======
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // ======= TensorFlow Lite =======
    implementation("org.tensorflow:tensorflow-lite:2.13.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.4.3")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.4.3")
    implementation("org.tensorflow:tensorflow-lite-gpu:2.13.0")
    // GPU (opcional): implementation("org.tensorflow:tensorflow-lite-gpu:2.13.0")
}

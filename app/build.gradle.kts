plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("plugin.serialization") version "2.0.0"
    alias(libs.plugins.devToolsKspGoogle)
    alias(libs.plugins.daggerHiltAndroid)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.quotemaster.quotemasterapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.quotemaster.quotemasterapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    packaging {
        resources {
            excludes += listOf("/META-INF/{AL2.0,LGPL2.1}",
                "META-INF/versions/9/OSGI-INF/MANIFEST.MF",
                "font/**",
                "fonts/**",
                "**/*.ttf",
                "**/*.ttc",
                "**/*.otf",
            )
        }

        jniLibs {
            excludes += listOf(
                "**/x86/**",
                "**/x86_64/**",
                "**/mips/**",
                "**/mips64/**"
            )
        }
    }

    /*// OPTIMIZATION 3: Enable ABI splits for different APKs per architecture
    splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a", "armeabi-v7a")
            isUniversalApk = false // Set to true if you want a universal APK too
        }
    }*/
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.rules)
    implementation(libs.core)
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.ui.graphics.android)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.games.activity)
    implementation(libs.androidx.compose.testing)
    implementation(libs.play.services.auth)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.ui.viewbinding)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.testing)
    implementation(libs.accompanist.permissions)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.accompanist.insets)
    //kotlinx serialization
    implementation(libs.kotlinx.serialization.json)
    implementation( libs.androidx.lifecycle.runtime.compose)
    //dagger hilt
    ksp(libs.com.google.dagger.hilt.compiler)
    implementation(libs.bundles.daggerHilt)
    //preferences data store
    implementation(libs.androidx.datastore.preferences)
    //implementation(libs.ui.graphics) // For RenderEffect
    //Coil supports GIFs
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.coil.svg) // or match your Coil version
    //testcase
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    // Retrofit and Gson
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    //
    // CameraX core libraries
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle.v130)
    implementation(libs.androidx.camera.view.v130)
    // Guava for ListenableFuture (required by CameraX lifecycle)
    implementation(libs.guava)

    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation (libs.accompanist.placeholder.material)

    //room
    ksp(libs.androidx.room.compiler)
    implementation(libs.bundles.room)
    //glide
    implementation(libs.glide)
    ksp(libs.glide.ksp)

    //permission
    implementation (libs.accompanist.permissions)

    //splash screen
    implementation (libs.androidx.core.splashscreen)



}
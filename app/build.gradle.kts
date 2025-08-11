plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.lky.mytoolbox"
    compileSdk = 35
    buildToolsVersion = "35.0.0"
    viewBinding {
        enable = true
    }
    defaultConfig {
        applicationId = "com.lky.mytoolbox"
        minSdk = 34
        targetSdk = 34
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
    packagingOptions {
        // 排除重复的 'mozilla/public-suffix-list.txt' 文件
        exclude("mozilla/public-suffix-list.txt")
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/NOTICE")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")

    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.volley)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.junit.junit)
    implementation(libs.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // UCrop 图片裁剪库
    implementation("com.github.yalantis:ucrop:2.2.8")
    implementation("com.vanniktech:android-image-cropper:4.6.0")

    // Glide 图片加载库
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    implementation("net.objecthunter:exp4j:0.4.8")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("com.umeng.umsdk:common:9.8.0")
    implementation("org.apache.httpcomponents:httpclient:4.5.14")
    implementation("com.google.mlkit:translate:17.0.3")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("com.mrljdx:ffmpeg-kit-full:6.1.4")
    implementation("com.itextpdf:itext7-core:7.2.3")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("org.jetbrains:annotations:20.1.0")

    implementation ("androidx.exifinterface:exifinterface:1.3.3")
    implementation ("com.caverock:androidsvg:1.4")

    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("com.google.guava:guava:32.1.2-jre")
//二维码生成器
    implementation ("com.google.zxing:core:3.4.1")
    implementation ("androidx.appcompat:appcompat:1.6.1")

    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation ("com.google.zxing:core:3.4.1")

    implementation("com.google.mlkit:text-recognition:16.0.0")
    implementation("com.google.mlkit:text-recognition-chinese:16.0.0")
    implementation("com.google.android.material:material:1.9.0")
}
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    //위시리스트
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.meonail"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.meonail"
        minSdk = 24
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    //위시리스트 필요 추가
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.1")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("com.squareup.okhttp3:okhttp:4.10.0") // ✅ OkHttp 추가
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation ("org.simpleframework:simple-xml:2.7.1")  // ✅ SimpleXML 추가
    implementation ("com.squareup.retrofit2:converter-simplexml:2.9.0") // ✅ Retrofit XML 컨버터 추가
    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0") // 🔥 Scalars Converter 추가
    implementation ("org.jsoup:jsoup:1.15.4")

    //캘린더
    implementation("com.prolificinteractive:material-calendarview:1.4.3")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.annotation)
    implementation(libs.glide)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    // 이미지 의존성
    implementation ("com.github.bumptech.glide:glide:4.15.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.0")
}
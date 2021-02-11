@file:Suppress("ClassName", "unused")

package io.github.reactivecircus.streamlined

object Versions {
    const val kotlin = "1.4.30"
    const val androidGradlePlugin = "7.0.0-alpha05"
    const val appVersioning = "0.8.1"
    const val googleServices = "4.3.5"
    const val detekt = "1.15.0"
    const val sqldelight = "1.4.4"
    const val playPublisher = "2.8.0"
    const val dependencyGraphGenerator = "0.5.0"
    const val bugsnag = "5.6.0"
    const val leakCanary = "2.6"
    const val desugarLibs = "1.0.10"

    object kotlinx {
        const val coroutines = "1.4.2"
        const val serialization = "1.1.0-RC"
    }

    object firebase {
        const val analytics = "18.0.2"
    }

    const val blueprint = "1.14.1"
    const val flowbinding = "1.0.0-beta02"
    const val workflow = "1.0.0-alpha.9"

    object androidx {
        const val core = "1.5.0-beta01"
        const val annotation = "1.2.0-beta01"
        const val appCompat = "1.3.0-beta01"
        const val activity = "1.2.0"
        const val fragment = "1.3.0"
        const val coordinatorLayout = "1.1.0"
        const val recyclerView = "1.2.0-beta01"
        const val swipeRefreshLayout = "1.2.0-alpha01"
        const val constraintLayout = "2.0.4"
        const val lifecycle = "2.3.0"
        const val navigation = "2.3.3"
        const val work = "2.5.0"

        object test {
            const val core = "1.3.1-alpha02"
            const val monitor = "1.3.1-alpha02"
            const val rules = "1.3.1-alpha02"
            const val runner = "1.3.1-alpha02"

            object ext {
                const val junit = "1.1.1-alpha02"
                const val truth = "1.3.1-alpha02"
            }

            const val espresso = "3.4.0-alpha02"
        }
    }

    const val material = "1.3.0"
    const val insetter = "0.4.0"
    const val dagger = "2.31.2"
    const val okhttp = "4.9.1"
    const val retrofit = "2.9.0"
    const val retrofitSerializationConverter = "0.8.0"
    const val store = "4.0.0-alpha07"
    const val coil = "1.1.1"
    const val timber = "4.7.1"
    const val junit = "4.13.1"
    const val truth = "1.1.2"
    const val radiography = "2.2.0"
}

object Plugins {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
    const val appVersioningGradlePlugin = "io.github.reactivecircus.appversioning:app-versioning-gradle-plugin:${Versions.appVersioning}"
    const val googleServicesGradlePlugin = "com.google.gms:google-services:${Versions.googleServices}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlinSerializationPlugin = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
    const val detektGradlePlugin = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.detekt}"
    const val sqldelightGradlePlugin = "com.squareup.sqldelight:gradle-plugin:${Versions.sqldelight}"
    const val playPublisherPlugin = "com.github.triplet.gradle:play-publisher:${Versions.playPublisher}"
    const val dependencyGraphGeneratorPlugin = "com.vanniktech:gradle-dependency-graph-generator-plugin:${Versions.dependencyGraphGenerator}"
}

object Libraries {
    const val bugsnag = "com.bugsnag:bugsnag-android:${Versions.bugsnag}"
    const val desugarLibs = "com.android.tools:desugar_jdk_libs:${Versions.desugarLibs}"

    object leakCanary {
        const val android = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
        const val plumber = "com.squareup.leakcanary:plumber-android:${Versions.leakCanary}"
    }

    object firebase {
        const val analyticsKtx = "com.google.firebase:firebase-analytics-ktx:${Versions.firebase.analytics}"
    }

    object kotlinx {
        object coroutines {
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinx.coroutines}"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinx.coroutines}"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinx.coroutines}"
        }

        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinx.serialization}"
    }

    object blueprint {
        const val ui = "io.github.reactivecircus.blueprint:blueprint-ui:${Versions.blueprint}"
        const val asyncCoroutines = "io.github.reactivecircus.blueprint:blueprint-async-coroutines:${Versions.blueprint}"
        const val interactorCoroutines = "io.github.reactivecircus.blueprint:blueprint-interactor-coroutines:${Versions.blueprint}"
        const val testingRobot = "io.github.reactivecircus.blueprint:blueprint-testing-robot:${Versions.blueprint}"
    }

    object flowbinding {
        const val android = "io.github.reactivecircus.flowbinding:flowbinding-android:${Versions.flowbinding}"
        const val material = "io.github.reactivecircus.flowbinding:flowbinding-material:${Versions.flowbinding}"
        const val swipeRefreshLayout = "io.github.reactivecircus.flowbinding:flowbinding-swiperefreshlayout:${Versions.flowbinding}"
    }

    object workflow {
        const val ui = "com.squareup.workflow1:workflow-ui-core-android:${Versions.workflow}"
        const val testing = "com.squareup.workflow1:workflow-testing-jvm:${Versions.workflow}"
    }

    object androidx {
        object core {
            const val ktx = "androidx.core:core-ktx:${Versions.androidx.core}"
        }

        const val annotation = "androidx.annotation:annotation:${Versions.androidx.annotation}"
        const val appCompat = "androidx.appcompat:appcompat:${Versions.androidx.appCompat}"

        object activity {
            const val ktx = "androidx.activity:activity-ktx:${Versions.androidx.activity}"
        }

        object fragment {
            const val ktx = "androidx.fragment:fragment-ktx:${Versions.androidx.fragment}"
            const val testing = "androidx.fragment:fragment-testing:${Versions.androidx.fragment}"
        }

        const val coordinatorLayout = "androidx.coordinatorlayout:coordinatorlayout:${Versions.androidx.coordinatorLayout}"
        const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.androidx.recyclerView}"
        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.androidx.swipeRefreshLayout}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.androidx.constraintLayout}"

        object lifecycle {
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.androidx.lifecycle}"
            const val commonJava8 = "androidx.lifecycle:lifecycle-common-java8:${Versions.androidx.lifecycle}"
            const val process = "androidx.lifecycle:lifecycle-process:${Versions.androidx.lifecycle}"
        }

        object navigation {
            const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.androidx.navigation}"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.androidx.navigation}"
        }

        object work {
            const val runtimeKtx = "androidx.work:work-runtime-ktx:${Versions.androidx.work}"
            const val testing = "androidx.work:work-testing:${Versions.androidx.work}"
        }

        object test {
            const val coreKtx = "androidx.test:core-ktx:${Versions.androidx.test.core}"
            const val monitor = "androidx.test:monitor:${Versions.androidx.test.monitor}"
            const val rules = "androidx.test:rules:${Versions.androidx.test.rules}"
            const val runner = "androidx.test:runner:${Versions.androidx.test.runner}"

            object ext {
                const val junitKtx = "androidx.test.ext:junit-ktx:${Versions.androidx.test.ext.junit}"
                const val truth = "androidx.test.ext:truth:${Versions.androidx.test.ext.truth}"
            }
        }

        object espresso {
            const val core = "androidx.test.espresso:espresso-core:${Versions.androidx.test.espresso}"
            const val contrib = "androidx.test.espresso:espresso-contrib:${Versions.androidx.test.espresso}"
            const val intents = "androidx.test.espresso:espresso-intents:${Versions.androidx.test.espresso}"
        }
    }

    const val material = "com.google.android.material:material:${Versions.material}"

    const val insetter = "dev.chrisbanes.insetter:insetter:${Versions.insetter}"

    object dagger {
        const val runtime = "com.google.dagger:dagger:${Versions.dagger}"
        const val compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    }

    object okhttp {
        const val client = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
        const val mockWebServer = "com.squareup.okhttp3:mockwebserver:${Versions.okhttp}"
    }

    object retrofit {
        const val client = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        const val mock = "com.squareup.retrofit2:retrofit-mock:${Versions.retrofit}"
        const val serializationConverter = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.retrofitSerializationConverter}"
    }

    object sqldelight {
        object driver {
            const val android = "com.squareup.sqldelight:android-driver:${Versions.sqldelight}"
            const val jvm = "com.squareup.sqldelight:sqlite-driver:${Versions.sqldelight}"
        }

        const val coroutinesExtensions = "com.squareup.sqldelight:coroutines-extensions:${Versions.sqldelight}"
    }

    const val store = "com.dropbox.mobile.store:store4:${Versions.store}"
    const val coil = "io.coil-kt:coil:${Versions.coil}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val junit = "junit:junit:${Versions.junit}"
    const val truth = "com.google.truth:truth:${Versions.truth}"
    const val radiography = "com.squareup.radiography:radiography:${Versions.radiography}"
}

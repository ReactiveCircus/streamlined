@file:Suppress("ClassName", "unused")

package io.github.reactivecircus.streamlined

object versions {
    const val detekt = "1.10.0"
    const val bugsnag = "5.0.1"
    const val leakCanary = "2.4"
    const val desugarLibs = "1.0.10"

    object kotlinx {
        const val coroutines = "1.3.8-1.4.0-rc"
        const val serialization = "1.0-M1-1.4.0-rc"
    }

    object firebase {
        const val analytics = "17.4.3"
    }

    const val blueprint = "1.11.0"
    const val flowbinding = "1.0.0-alpha02"
    const val workflow = "1.0.0-alpha.3"

    object androidx {
        const val core = "1.5.0-alpha01"
        const val annotation = "1.2.0-alpha01"
        const val appCompat = "1.3.0-alpha01"
        const val activity = "1.2.0-alpha07"
        const val fragment = "1.3.0-alpha07"
        const val coordinatorLayout = "1.1.0"
        const val recyclerView = "1.2.0-alpha05"
        const val swipeRefreshLayout = "1.2.0-alpha01"
        const val constraintLayout = "2.0.0-rc1"
        const val lifecycle = "2.3.0-alpha06"
        const val navigation = "2.3.0"
        const val work = "2.4.0"

        object test {
            const val core = "1.3.0-rc03"
            const val monitor = "1.3.0-rc03"
            const val rules = "1.3.0-rc03"
            const val runner = "1.3.0-rc03"

            object ext {
                const val junit = "1.1.2-rc03"
                const val truth = "1.3.0-rc03"
            }

            const val espresso = "3.3.0-rc03"
        }
    }

    const val material = "1.3.0-alpha02"
    const val insetter = "0.3.0"
    const val dagger = "2.28.3"
    const val assistedInject = "0.5.2"
    const val okhttp = "4.8.1"
    const val retrofit = "2.9.0"
    const val retrofitSerializationConverter = "0.5.0"
    const val sqldelight = "1.4.0"
    const val store = "4.0.0-SNAPSHOT"
    const val coil = "0.11.0"
    const val timber = "4.7.1"
    const val junit = "4.13"
    const val truth = "1.0.1"
}

object libraries {
    const val bugsnag = "com.bugsnag:bugsnag-android:${versions.bugsnag}"
    const val desugarLibs = "com.android.tools:desugar_jdk_libs:${versions.desugarLibs}"

    object leakCanary {
        const val android = "com.squareup.leakcanary:leakcanary-android:${versions.leakCanary}"
        const val plumber = "com.squareup.leakcanary:plumber-android:${versions.leakCanary}"
    }

    object firebase {
        const val analyticsKtx = "com.google.firebase:firebase-analytics-ktx:${versions.firebase.analytics}"
    }

    object kotlinx {
        object coroutines {
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${versions.kotlinx.coroutines}"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${versions.kotlinx.coroutines}"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${versions.kotlinx.coroutines}"
        }

        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${versions.kotlinx.serialization}"
    }

    object blueprint {
        const val ui = "io.github.reactivecircus.blueprint:blueprint-ui:${versions.blueprint}"
        const val asyncCoroutines = "io.github.reactivecircus.blueprint:blueprint-async-coroutines:${versions.blueprint}"
        const val interactorCoroutines = "io.github.reactivecircus.blueprint:blueprint-interactor-coroutines:${versions.blueprint}"
        const val testingRobot = "io.github.reactivecircus.blueprint:blueprint-testing-robot:${versions.blueprint}"
    }

    object flowbinding {
        const val android = "io.github.reactivecircus.flowbinding:flowbinding-android:${versions.flowbinding}"
        const val material = "io.github.reactivecircus.flowbinding:flowbinding-material:${versions.flowbinding}"
        const val swipeRefreshLayout = "io.github.reactivecircus.flowbinding:flowbinding-swiperefreshlayout:${versions.flowbinding}"
    }

    object workflow {
        const val runtime = "com.squareup.workflow1:workflow-runtime-jvm:${versions.workflow}"
        const val testing = "com.squareup.workflow1:workflow-testing-jvm:${versions.workflow}"
    }

    object androidx {
        object core {
            const val ktx = "androidx.core:core-ktx:${versions.androidx.core}"
        }

        const val annotation = "androidx.annotation:annotation:${versions.androidx.annotation}"
        const val appCompat = "androidx.appcompat:appcompat:${versions.androidx.appCompat}"

        object activity {
            const val ktx = "androidx.activity:activity-ktx:${versions.androidx.activity}"
        }

        object fragment {
            const val ktx = "androidx.fragment:fragment-ktx:${versions.androidx.fragment}"
            const val testing = "androidx.fragment:fragment-testing:${versions.androidx.fragment}"
        }

        const val coordinatorLayout = "androidx.coordinatorlayout:coordinatorlayout:${versions.androidx.coordinatorLayout}"
        const val recyclerView = "androidx.recyclerview:recyclerview:${versions.androidx.recyclerView}"
        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${versions.androidx.swipeRefreshLayout}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${versions.androidx.constraintLayout}"

        object lifecycle {
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${versions.androidx.lifecycle}"
            const val commonJava8 = "androidx.lifecycle:lifecycle-common-java8:${versions.androidx.lifecycle}"
            const val process = "androidx.lifecycle:lifecycle-process:${versions.androidx.lifecycle}"
        }

        object navigation {
            const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:${versions.androidx.navigation}"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:${versions.androidx.navigation}"
        }

        object work {
            const val runtimeKtx = "androidx.work:work-runtime-ktx:${versions.androidx.work}"
            const val testing = "androidx.work:work-testing:${versions.androidx.work}"
        }

        object test {
            const val coreKtx = "androidx.test:core-ktx:${versions.androidx.test.core}"
            const val monitor = "androidx.test:monitor:${versions.androidx.test.monitor}"
            const val rules = "androidx.test:rules:${versions.androidx.test.rules}"
            const val runner = "androidx.test:runner:${versions.androidx.test.runner}"

            object ext {
                const val junitKtx = "androidx.test.ext:junit-ktx:${versions.androidx.test.ext.junit}"
                const val truth = "androidx.test.ext:truth:${versions.androidx.test.ext.truth}"
            }
        }

        object espresso {
            const val core = "androidx.test.espresso:espresso-core:${versions.androidx.test.espresso}"
            const val contrib = "androidx.test.espresso:espresso-contrib:${versions.androidx.test.espresso}"
            const val intents = "androidx.test.espresso:espresso-intents:${versions.androidx.test.espresso}"
        }
    }

    const val material = "com.google.android.material:material:${versions.material}"

    const val insetter = "dev.chrisbanes:insetter-ktx:${versions.insetter}"

    object dagger {
        const val runtime = "com.google.dagger:dagger:${versions.dagger}"
        const val compiler = "com.google.dagger:dagger-compiler:${versions.dagger}"
    }

    object assistedInject {
        const val annotations = "com.squareup.inject:assisted-inject-annotations-dagger2:${versions.assistedInject}"
        const val processor = "com.squareup.inject:assisted-inject-processor-dagger2:${versions.assistedInject}"
    }

    object okhttp {
        const val client = "com.squareup.okhttp3:okhttp:${versions.okhttp}"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${versions.okhttp}"
        const val mockWebServer = "com.squareup.okhttp3:mockwebserver:${versions.okhttp}"
    }

    object retrofit {
        const val client = "com.squareup.retrofit2:retrofit:${versions.retrofit}"
        const val mock = "com.squareup.retrofit2:retrofit-mock:${versions.retrofit}"
        const val serializationConverter = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${versions.retrofitSerializationConverter}"
    }

    object sqldelight {
        object driver {
            const val android = "com.squareup.sqldelight:android-driver:${versions.sqldelight}"
            const val jvm = "com.squareup.sqldelight:sqlite-driver:${versions.sqldelight}"
        }

        const val coroutinesExtensions = "com.squareup.sqldelight:coroutines-extensions:${versions.sqldelight}"
    }

    const val store = "com.dropbox.mobile.store:store4:${versions.store}"
    const val coil = "io.coil-kt:coil:${versions.coil}"
    const val timber = "com.jakewharton.timber:timber:${versions.timber}"
    const val junit = "junit:junit:${versions.junit}"
    const val truth = "com.google.truth:truth:${versions.truth}"
}

plugins {
    `kotlin-dsl`
}

// force compilation of Dependencies.kt so it can be referenced in buildSrc/build.gradle.kts
sourceSets.main {
    java {
        setSrcDirs(setOf(projectDir.parentFile.resolve("src/main/kotlin")))
        include("io/github/reactivecircus/streamlined/Dependencies.kt")
    }
}

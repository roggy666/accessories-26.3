import io.wispforest.helpers.Extensions.modrinth
import io.wispforest.helpers.Extensions.modrinthImplementation

plugins {
    id("multiloader-platform")
    id("multiloader-publishing")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    // Core Libs
    implementation(libs.fabric.loader)
    implementation(libs.fabric.api)
    // --

    // General Libs
    compileOnly(libs.modmenu)
    runtimeOnly(libs.modmenu)
    //--

//    modrinth(this::modLocalRuntime, "ok-boomer" to "0.1.3+1.21")
//    modrinth(this::modLocalRuntime, "sodium" to "${libs.versions.sodium.get()}-fabric")

    // Trinkets has no 26.x release; the compat layer is disabled until one exists
    // compileOnly(libs.trinkets)
}

// The Trinkets compat mixin is @Pseudo but still needs the Trinkets API on the compile classpath,
// so it is left out of the build (and of accessories-fabric.mixins.json) while the dependency is disabled
sourceSets {
    main {
        java {
            exclude("**/mixin/trinkets/**")
        }
    }
}

repositories {}
// The shadow jar is the real mod artifact (plain jar is disabled), so publish that instead of the java component's jar
afterEvaluate {
    publishing.publications.named<MavenPublication>("mavenCommon") {
        setArtifacts(listOf(tasks.shadowJar))
        artifact(tasks.named("sourcesJar")) { classifier = "sources" }
    }
}

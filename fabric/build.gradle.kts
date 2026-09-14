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

repositories {}
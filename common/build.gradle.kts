import io.wispforest.helpers.Extensions.fabricModule
import io.wispforest.helpers.Extensions.modrinth

plugins {
    id("multiloader-base")
    id("multiloader-publishing")
}

// Item viewer integrations are only compiled when the viewer is enabled (EMI has no 26.x build yet)
val enabledViewers = (rootProject.property("enabled_item_viewers") as String).split(",").filter { it.isNotBlank() }

sourceSets {
    main {
        java {
            if ("emi" !in enabledViewers) exclude("**/compat/emi/**")
            if ("rei" !in enabledViewers) exclude("**/compat/rei/**")
            if ("jei" !in enabledViewers) exclude("**/compat/jei/**")
        }
    }
}

dependencies {
    // Core Libs
    compileOnly(libs.mixin.extras.common)
    annotationProcessor(libs.mixin.extras.common)
    // --

    // General Libs
    fabricModule(this::compileOnlyApi, "fabric-api-base")
    // --

    modrinth(this::compileOnly, "sodium" to "${libs.versions.sodium.get()}-fabric")
}

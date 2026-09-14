import io.wispforest.helpers.Extensions.modId
import io.wispforest.helpers.MavenSetupUtils

plugins {
    id("java-library")
    id("maven-publish")
}

/**
 * Handles the ability to publish either the common or fabric module and sets up the maven credentials within [MavenSetupUtils.setupMavenRepo]
 */
publishing {
    var modid = rootProject.modId

    publications {
        create<MavenPublication>("mavenCommon") {
            val name = project.name
            artifactId = "${modid}${(if(name.isEmpty()) "" else "-${name.replace("-mojmap", "")}")}"
            afterEvaluate {
                this@create.from(components["java"])
            }
        }
    }

    MavenSetupUtils.setupMavenRepo(rootProject, repositories)
}
import org.kordamp.gradle.plugin.jandex.tasks.JandexTask

plugins {
    id("org.kordamp.gradle.jandex") version "0.9.0"
}

defaultTasks("clean", "build")

allprojects {
    apply {
        plugin("org.kordamp.gradle.jandex")
    }

    repositories {
        jcenter()
    }

    val unzip by tasks.registering(Copy::class) {
        into(File(project.buildDir, "dependencies"))
        configurations.runtimeClasspath.get()
            .files { it.group == "com.vaadin" }
            .forEach { depJar ->
                from(zipTree(depJar))
            }
    }

    tasks.withType(JandexTask::class).configureEach {
        dependsOn(unzip)
        sources.from(File(project.buildDir, "dependencies/com/vaadin/flow"))
    }
}

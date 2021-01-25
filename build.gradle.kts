plugins {
    id("org.kordamp.gradle.jandex") version "0.9.0"
}

defaultTasks("clean", "build")

repositories {
    jcenter()
}

dependencies {
    // Vaadin 14
    implementation("com.vaadin:vaadin-core:${properties["vaadin_version"]}") {
        // Webjars are only needed when running in Vaadin 13 compatibility mode
        listOf("com.vaadin.webjar", "org.webjars.bowergithub.insites",
                "org.webjars.bowergithub.polymer", "org.webjars.bowergithub.polymerelements",
                "org.webjars.bowergithub.vaadin", "org.webjars.bowergithub.webcomponents")
                .forEach { exclude(group = it) }
    }
}

val unzip by tasks.registering(Copy::class) {
    into(File(project.buildDir, "dependencies"))
    configurations.runtimeClasspath.get()
        .files { it.group == "com.vaadin" }
        .forEach { depJar ->
            from(zipTree(depJar))
        }
}

(tasks.getByPath("jandex") as org.kordamp.gradle.plugin.jandex.tasks.JandexTask).apply {
    dependsOn(unzip)
    sources.from(File(project.buildDir, "dependencies/com/vaadin/flow"))
}

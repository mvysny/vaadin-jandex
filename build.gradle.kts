import com.jfrog.bintray.gradle.BintrayExtension
import org.kordamp.gradle.plugin.jandex.tasks.JandexTask
import java.util.*

plugins {
    kotlin("jvm") version "1.4.21"
    id("org.kordamp.gradle.jandex") version "0.9.0" apply false
    id("com.jfrog.bintray") version "1.8.3"
    `maven-publish`
    id("org.jetbrains.dokka") version "1.4.0"
}

defaultTasks("clean", "build")

allprojects {
    group = "com.github.mvysny.vaadin-jandex"
    version = "${properties["vaadin_version"]}"

    repositories {
        jcenter()
    }
}

subprojects {
    apply {
        plugin("kotlin")
        plugin("org.kordamp.gradle.jandex")
        plugin("maven-publish")
        plugin("com.jfrog.bintray")
        plugin("org.jetbrains.dokka")
    }

    val unzip by tasks.registering(Copy::class) {
        into(File(project.buildDir, "dependencies"))
        configurations.compileOnly.get()
            .files { it.group == "com.vaadin" }
            .forEach { depJar ->
                from(zipTree(depJar))
            }
    }

    tasks.withType(JandexTask::class).configureEach {
        dependsOn(unzip)
        sources.from(File(project.buildDir, "dependencies/com/vaadin/flow"))
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            // to see the exceptions of failed tests in Travis-CI console.
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }

    // creates a reusable function which configures proper deployment to Bintray
    ext["configureBintray"] = { artifactId: String ->

        val local = Properties()
        val localProperties: File = rootProject.file("local.properties")
        if (localProperties.exists()) {
            localProperties.inputStream().use { local.load(it) }
        }

        val sourceJar = task("sourceJar", Jar::class) {
            dependsOn(tasks["classes"])
            archiveClassifier.set("sources")
            from(sourceSets.main.get().allSource)
        }

        val javadocJar = task("javadocJar", Jar::class) {
            from(tasks["dokkaJavadoc"])
            archiveClassifier.set("javadoc")
        }

        publishing {
            publications {
                create("mavenJava", MavenPublication::class.java).apply {
                    groupId = project.group.toString()
                    this.artifactId = artifactId
                    version = project.version.toString()
                    pom {
                        description.set("Vaadin Jandex index")
                        name.set(artifactId)
                        url.set("https://github.com/mvysny/vaadin-jandex")
                        licenses {
                            license {
                                name.set("The Apache Software License, Version 2.0")
                                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                                distribution.set("repo")
                            }
                        }
                        developers {
                            developer {
                                id.set("mavi")
                                name.set("Martin Vysny")
                                email.set("martin@vysny.me")
                            }
                        }
                        scm {
                            url.set("https://github.com/mvysny/vaadin-jandex")
                        }
                    }

                    from(components["java"])
                    artifact(sourceJar)
                    artifact(javadocJar)
                }
            }
        }

        bintray {
            user = local.getProperty("bintray.user")
            key = local.getProperty("bintray.key")
            pkg(closureOf<BintrayExtension.PackageConfig> {
                repo = "github"
                name = "com.github.mvysny.jandex-index"
                setLicenses("Apache-2.0")
                vcsUrl = "https://github.com/mvysny/vaadin-jandex"
                publish = true
                setPublications("mavenJava")
                version(closureOf<BintrayExtension.VersionConfig> {
                    this.name = project.version.toString()
                    released = Date().toString()
                })
            })
        }
    }
}

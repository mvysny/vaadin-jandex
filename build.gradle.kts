import org.kordamp.gradle.plugin.jandex.tasks.JandexTask

plugins {
    kotlin("jvm") version "1.4.30"
    id("org.kordamp.gradle.jandex") version "0.9.0" apply false
    `maven-publish`
    signing
}

defaultTasks("clean", "build")

allprojects {
    group = "com.github.mvysny.vaadin-jandex"
    version = "${properties["vaadin_version"]}"

    repositories {
        mavenCentral()
        maven("https://maven.vaadin.com/vaadin-prereleases")
    }
}

subprojects {
    apply {
        plugin("kotlin")
        plugin("org.kordamp.gradle.jandex")
        plugin("maven-publish")
        plugin("org.gradle.signing")
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
        sources.from(File(project.buildDir, "dependencies/com/vaadin"))
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

        java {
            withJavadocJar()
            withSourcesJar()
        }

        tasks.withType<Javadoc> {
            isFailOnError = false
        }

        publishing {
            repositories {
                maven {
                    setUrl("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                    credentials {
                        username = project.properties["ossrhUsername"] as String?
                        password = project.properties["ossrhPassword"] as String?
                    }
                }
            }
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
                }
            }
        }

        signing {
            sign(publishing.publications["mavenJava"])
        }
    }
}

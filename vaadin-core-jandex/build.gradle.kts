dependencies {
    // Vaadin 14
    compileOnly("com.vaadin:vaadin-core:${properties["vaadin_version"]}") {
        // Webjars are only needed when running in Vaadin 13 compatibility mode
        listOf("com.vaadin.webjar", "org.webjars.bowergithub.insites",
            "org.webjars.bowergithub.polymer", "org.webjars.bowergithub.polymerelements",
            "org.webjars.bowergithub.vaadin", "org.webjars.bowergithub.webcomponents")
            .forEach { exclude(group = it) }
    }
    compileOnly("javax.servlet:javax.servlet-api:3.1.0")

    testImplementation(kotlin("stdlib-jdk8"))
    testImplementation("com.github.mvysny.dynatest:dynatest-engine:${properties["dynatest_version"]}")
    testImplementation("org.jboss:jandex:2.4.3.Final")
}

val configureBintray = ext["configureBintray"] as (artifactId: String) -> Unit
configureBintray("vaadin-core-jandex")

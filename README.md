# vaadin-jandex

> Work on this project has been kindly sponsored by [Inacta AG](https://inacta.ch).

Builds the Jandex index of all Vaadin core libraries. This enables Vaadin-based
apps to run on top of Quarkus+Undertow.

There are two artifacts built:

* [vaadin-core-jandex](vaadin-core-jandex) builds the Jandex index for `vaadin-core` (only
  the open-source components)
* [vaadin-jandex](vaadin-jandex) builds the Jandex index for `vaadin`
  (both the open-source components and the pro components such as Board, GridPro,
  CRUD etc).

## Available on JCenter and Maven Central

Browse the [vaadin-jandex on Maven Central](https://repo1.maven.org/maven2/com/github/mvysny/vaadin-jandex/)
to see versions available. The vaadin-jandex library tracks Vaadin's release versioning;
simply use the same vaadin-jandex as is your Vaadin version.

## How to build a custom version

1. git clone the project
2. Edit `gradle.properties` and modify the Vaadin version to the version of your choice, e.g. 14.4.6.
3. Run `./gradlew publishToMavenLocal`. The library is now installed in your local repo.
4. git clone https://github.com/mvysny/vaadin-quarkus
5. Modify the vaadin-quarkus's `pom.xml` and add the dependency on this library:

```xml
<dependency>
  <groupId>com.github.mvysny.vaadin-jandex</groupId>
  <artifactId>vaadin-jandex</artifactId>
  <version>14.4.6</version>
</dependency>
```

6. Edit the vaadin-quarkus's `application.properties` and delete all Vaadin-related
   `.index-dependency.` lines.
7. Run the vaadin-quarkus project - it should now start as usual.

## License

See [License](LICENSE.txt).

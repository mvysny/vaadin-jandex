# vaadin-jandex

Builds the Jandex index of all Vaadin core libraries. This enables Vaadin-based
apps to run on top of Quarkus+Undertow.

## How to use

1. git clone the project
2. Edit `pom.xml` and modify the Vaadin version to the version of your choice.
3. Run `mvn -C clean install`. The library is now installed in your local repo.
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

The `pom.xml` contains a workaround for https://github.com/wildfly/jandex-maven-plugin/issues/25 - please vote to get it fixed.

## Gradle

On top of `pom.xml` there's `build.gradle` which builds the same thing. However,
the support to deploy to a local maven repo is lacking as of now.

## Further Work

Only `vaadin-core` is indexed at the moment - I need to split this project into
two submodules, one of which would build `vaadin-core`, the other would build
`vaadin`.

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

## Example

See the [vaadin-quarkus](https://github.com/mvysny/vaadin-quarkus) example app for more details.

## Available on JCenter and Maven Central

Browse the [vaadin-jandex on Maven Central](https://repo1.maven.org/maven2/com/github/mvysny/vaadin-jandex/)
to see versions available. The vaadin-jandex library tracks Vaadin's release versioning;
simply use the same vaadin-jandex as is your Vaadin version.

In order to add Vaadin Jandex index to your app, simply add the
following dependency into your project:

```xml
<dependency>
  <groupId>com.github.mvysny.vaadin-jandex</groupId>
  <artifactId>vaadin-jandex</artifactId>
  <version>14.6.3</version>
</dependency>
```

(or use `vaadin-core-jandex` if you're only using vaadin-core components).

## How to build a custom version

1. git clone this project
2. Edit `gradle.properties` and modify the Vaadin version to the version of your choice, e.g. 14.4.6.
3. Edit `build.gradle.kts` and set the version line to `version = "${properties["vaadin_version"]}"` (get rid of the -SNAPSHOT suffix)
4. Run `./gradlew publishToMavenLocal`. The library is now installed in your local repo.
5. Modify your app's `pom.xml` and add the dependency on this library:

```xml
<dependency>
  <groupId>com.github.mvysny.vaadin-jandex</groupId>
  <artifactId>vaadin-jandex</artifactId>
  <version>14.4.6</version>
</dependency>
```

(or use `vaadin-core-jandex` if you're only using vaadin-core components).

6. Run your vaadin-quarkus project - it should now start as usual.

## Temporary

This project is just temporary; the goal is to have the Jandex index merged
into Vaadin artifacts themselves. In order to achieve that, please vote for
the following tickets:

* [Flow #9862](https://github.com/vaadin/flow/issues/9862)
* [Components #579](https://github.com/vaadin/vaadin-flow-components/issues/579)

## Releasing

1. Edit `gradle.properties` and set `vaadin_version` to the desired version.
2. Edit `build.gradle.kts` and remove `-SNAPSHOT` in the `version=` stanza
3. Commit with the commit message of simply being the desired Vaadin version.
4. git tag the commit with the same tag name as the commit message above.
5. `git push`, `git push --tags`
6. Run `./gradlew clean build publish` then follow the release procedure on OSSRH/MavenCentral
7. Add the `-SNAPSHOT` back and commit and push.

## License

See [License](LICENSE.txt).

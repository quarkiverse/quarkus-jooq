# jOOQ Extension for Quarkus
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-1-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->

[![Build](https://github.com/quarkiverse/quarkus-jooq/workflows/Build/badge.svg)](https://github.com/quarkiverse/quarkus-jooq/actions?query=workflow%3ABuild)
[![Maven Central](https://img.shields.io/maven-central/v/io.quarkiverse.jooq/quarkus-jooq.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.quarkiverse.jooq/quarkus-jooq)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

jOOQ generates Java code from your database and lets you build type safe SQL queries through its fluent API.

This extension allows you to develop applications that interact with the [supported databases](https://www.jooq.org/download/support-matrix) using [jOOQ](https://github.com/jOOQ/jOOQ).

- [Contributors](#contributors)
- [Configuration](#configuration)
- [Usage](#usage)
- [Native Mode Support](#native-mode-support)
- [jOOQ Commercial Distributions](#jooq-commercial-distributions)

## Contributors ✨

The is extension is based on the prior work by [@leotu](https://github.com/leotu) - [quarkus-ext-jooq](https://github.com/leotu/quarkus-ext-jooq)

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="http://hakt.com.au"><img src="https://avatars.githubusercontent.com/u/343859?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Tim King</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-jooq/commits?author=angrymango" title="Code">💻</a> <a href="#maintenance-angrymango" title="Maintenance">🚧</a></td>
  </tr>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!

## Configuration

After configuring `quarkus BOM`:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-bom</artifactId>
            <version>${insert.newest.quarkus.version.here}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

You can just configure the `quarkus-jooq` extension by adding the following dependency:

```xml
<dependency>
    <groupId>io.quarkiverse.jooq</groupId>
    <artifactId>quarkus-jooq</artifactId>
    <version>${latest.release.version}</version>
</dependency>
```
<!--
***NOTE:*** You can bootstrap a new application quickly by using [code.quarkus.io](https://code.quarkus.io) and choosing `quarkus-cxf`.
-->

## Usage

The default DSL context can then be injected with:

```yaml
# default datasource
quarkus.datasource.db-kind=h2
quarkus.datasource.username=username-default
quarkus.datasource.password=username-default
quarkus.datasource.jdbc.url=jdbc:h2:tcp://localhost:19092/mem:default;DATABASE_TO_UPPER=FALSE;
quarkus.datasource.jdbc.min-size=1
quarkus.datasource.jdbc.max-size=2

# default jOOQ datasource
quarkus.jooq.dialect=H2
```

The default ```org.jooq.DSLContext``` can then be injected:

```java
@Inject
DSLContext dsl; // default
```

### Custom Configuration

Update the configuration with a reference to a named dependency to inject:

```yaml
# Using custom jOOQ configuration injection
quarkus.jooq.configuration-inject=myCustomConfiguration
```

Then provide the custom configuration in the apply method.

```java
@ApplicationScoped
public class MyCustomConfigurationFactory {
    private static final Logger LOGGER = Logger.getLogger(MyCustomConfigurationFactory.class);
    
    @ApplicationScoped
    @Produces
    @Named("myCustomConfiguration")
    public JooqCustomContext create() {
        LOGGER.debug("MyCustomConfigurationFactory: create");
        return new JooqCustomContext() {
            @Override
            public void apply(Configuration configuration) {
                // Custom configuration here...
            }
        };
    }
}
```

Alternatively, using a qualified class that implements ```io.quarkiverse.jooq.runtime.JooqCustomContext```:

```yaml
# Using custom jOOQ configuration
quarkus.jooq.configuration=io.quarkiverse.jooq.MyCustomConfiguration
```

Then provide the custom configuration in the apply method.

```java
public class MyCustomConfiguration implements JooqCustomContext {
    @Override
    public void apply(Configuration configuration) {
        // Custom configuration here...
    }
}
```

### Multiple Datasources

Multiple data sources can be configured using named data sources as follows:

```yaml
# named data source
quarkus.datasource.datasource1.db-kind=h2
quarkus.datasource.datasource1.username=username1
quarkus.datasource.datasource1.password=username1
quarkus.datasource.datasource1.jdbc.url=jdbc:h2:tcp://localhost:19092/mem:datasource1;DATABASE_TO_UPPER=FALSE;
quarkus.datasource.datasource1.jdbc.min-size=1
quarkus.datasource.datasource1.jdbc.max-size=2

# jOOQ configuration referencing the named data source
quarkus.jooq.dsl1.dialect=H2
quarkus.jooq.dsl1.datasource=datasource1
quarkus.jooq.dsl1.configuration=io.quarkiverse.jooq.MyCustomConfiguration1
```

## Native Mode Support

Native compilation is supported in the standard Quarkus way using:

```shell
 ./mvnw package -Pnative 
```

## jOOQ Commercial Distributions

Only the Open Source version of jOOQ is supported as an automated build target at this time because of the lack of access to the pro jars at build time. 

In order to build use the jOOQ commercial features the extension must be built by the license holder. This can be done by:

* Adding the commercial jOOQ jars to a private maven repository
* Configuring settings.xml with the access details, for example: 
```xml
<settings xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd" xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <profiles>
        <profile>
            <id>jooq-pro</id>
            <repositories>
                <repository>
                    <id>[REPO_ID]</id>
                    <name>[REPO_NAME]</name>
                    <url>[REPO_URL]</url>
                </repository>
            </repositories>
        </profile>
    </profiles>
    <servers>
        <server>
            <id>[REPO_ID]</id>
            <username>[USERNAME]</username>
            <password>[PASSWORD]</password>
        </server>
    </servers>
</settings>
```
* Building the extension:
```shell
cd [EXTENSION_HOME]/quarkus-jooq-pro
mvn clean install -Pjooq-pro 
```
* Optionally release the artifacts to the private maven repository for use elsewhere and import into your project:
```xml
<dependency>
    <groupId>io.quarkiverse.jooq.pro</groupId>
    <artifactId>quarkus-jooq</artifactId>
    <version>${quarkus-jooq-pro.version}</version>
</dependency>
```
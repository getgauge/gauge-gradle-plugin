[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/gradle/plugin/org/gauge/gradle/gauge-gradle-plugin/maven-metadata.xml.svg?colorB=007ec6&label=Plugin+Portal)](https://plugins.gradle.org/plugin/org.gauge)
[![Build Status](https://travis-ci.org/getgauge/gauge-gradle-plugin.svg?branch=master)](https://travis-ci.org/getgauge/gauge-gradle-plugin)
[![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-v1.4%20adopted-ff69b4.svg)](CODE_OF_CONDUCT.md)

# Gauge Gradle Plugin

Use the gauge-gradle-plugin to execute specifications in your [Gauge](http://getgauge.io) Java project and manage dependencies using [Gradle](http://gradle.org//).

> **NOTE**: Prior to v1.8.0 the `gauge-gradle-plugin` had a different community maintainer. Versions prior to this were published to 
Maven Central & Bintray; with out-of-date versions available on the Gradle Plugins Portal.
>
> From v1.8.0+ [the Gradle Plugins Portal](https://plugins.gradle.org/plugin/org.gauge) will be the primary means of release for this plugin; under the care of the core Gauge team.

## Installation

### On a new project

You can use this plugin on a new project via a Gauge [project template](https://docs.gauge.org/latest/installation.html#project-templates):

```
gauge init java_gradle
```

### Using the plugins DSL

If you have an existing project and you would like to add the plugin manually you can add it like the below


````groovy
plugins {
    id 'java'
    id 'org.gauge' version '1.8.0'
}

group = 'my-gauge-tests'
version = '1.0-SNAPSHOT'

description = "My Gauge Tests"

repositories {
    mavenCentral()
}

dependencies {
    implementation 'com.thoughtworks.gauge:gauge-java:+'
}

// configure gauge task here (optional)
gauge {
    specsDir = 'specs'
    inParallel = true
    nodes = 2
    env = 'dev'
    tags = 'tag1'
    additionalFlags = '--verbose'
    gaugeRoot = '/opt/gauge'
}
````

### Using legacy plugin 'apply' style

* apply plugin `gauge` 
* update the `buildscript` to add the Gradle plugins repo and classpath

````groovy
apply plugin: 'java'
apply plugin: 'gauge'

group = "my-gauge-tests"
version = "1.0-SNAPSHOT"

description = "My Gauge Tests"

buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
         classpath "gradle.plugin.org.gauge.gradle:gauge-gradle-plugin:1.8.0"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'com.thoughtworks.gauge:gauge-java:+'
}

// configure gauge task here (optional)
gauge {
    specsDir = 'specs'
    inParallel = true
    nodes = 2
    env = 'dev'
    tags = 'tag1'
    additionalFlags = '--verbose'
    gaugeRoot = '/opt/gauge'
}

````

## Usage

### Running
````
gradle gauge
````
#### Execute list of specs
```
gradle gauge -PspecsDir="specs/first.spec specs/second.spec"
```
#### Execute specs in parallel
```
gradle gauge -PinParallel=true -PspecsDir=specs
```
#### Execute specs by tags
```
gradle gauge -Ptags="!in-progress" -PspecsDir=specs
```
#### Specifying execution environment
```
gradle gauge -Penv="dev" -PspecsDir=specs
```

Note : Pass specsDir parameter as the last one.

### All additional Properties
The following plugin properties can be additionally set:

|Property name|Usage|Description|
|-------------|-----|-----------|
|specsDir| -PspecsDir=specs| Gauge specs directory path. Required for executing specs|
|tags    | -Ptags="tag1 & tag2" |Filter specs by specified tags expression|
|inParallel| -PinParallel=true | Execute specs in parallel|
|nodes    | -Pnodes=3 | Number of parallel execution streams. Use with ```parallel```|
|env      | -Penv=qa  | gauge env to run against  |
|additionalFlags| -PadditionalFlags="--verbose" | Add additional gauge flags to execution|
|gaugeRoot| -PgaugeRoot="/opt/gauge" | Path to gauge installation root|

### Adding/configuring custom Gauge tasks
It is possible to define new custom Gauge tasks specific for different environments. For example,

````groovy

task gaugeDev(type: GaugeTask) {
    doFirst {
        gauge {
            specsDir = 'specs'
            inParallel = true
            nodes = 2
            env = 'dev'
            additionalFlags = '--verbose'
        }
    }
}

task gaugeTest(type: GaugeTask) {
    doFirst {
        gauge {
            specsDir = 'specs'
            inParallel = true
            nodes = 4
            env = 'test'
            additionalFlags = '--verbose'
        }
    }
}
````

### Running gauge task with source code of gradle plugin
run the gauge command with -
````
gradle gauge --include-build {PATH_TO_GRADLE_PLUGIN}
````

or add this property in `settings.gradle`
````
includeBuild {PATH_TO_GRADLE_PLUGIN}
````

## License

Gauge is released under the Apache License, Version 2.0. See [LICENSE](LICENSE) for the full license text.

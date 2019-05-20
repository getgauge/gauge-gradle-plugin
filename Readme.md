[![Maven Central](https://img.shields.io/maven-central/v/com.thoughtworks.gauge.gradle/gauge-gradle-plugin.svg)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22gauge-gradle-plugin%22)
[![Download Nightly](https://api.bintray.com/packages/gauge/gauge-gradle-plugin/Nightly/images/download.svg) ](https://bintray.com/gauge/gauge-gradle-plugin/)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/d4d3e7d6c4ce4fa3a79f2790167fd511)](https://www.codacy.com/app/manupsunny/gauge-gradle-plugin)
[![License](http://img.shields.io/:license-gpl3-blue.svg)](https://www.gnu.org/licenses/gpl.txt)
[![Build Status](https://travis-ci.org/getgauge/gauge-gradle-plugin.svg?branch=master)](https://travis-ci.org/getgauge/gauge-gradle-plugin)

[![Contributor Covenant](https://img.shields.io/badge/Contributor%20Covenant-v1.4%20adopted-ff69b4.svg)](CODE_OF_CONDUCT.md)

# Gauge Gradle Plugin

Use the gauge-gradle-plugin to execute specifications in your [Gauge](http://getgauge.io) java project and manage dependencies using [Gradle](http://gradle.org//).

### Create java project with gradle

```
gauge init java_gradle
```


### Using plugin in project

### Using legacy plugin application:

Apply plugin ***gauge*** and add classpath to your ***build.gradle***. Here is a sample gradle file,

````groovy
apply plugin: 'java'
apply plugin: 'gauge'
apply plugin: 'application'

group = "my-gauge-tests"
version = "1.1.0"

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
    'com.thoughtworks.gauge:gauge-java:+',
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

Older Versions of the Plugin are also available at [Gradle Plugin Portal](https://plugins.gradle.org/plugin/org.gauge). For an uptodate version of the Plugin please use `buildscript` and `mavenCentral()` in your build.gradle as shown in the example above.

### Using the plugins DSL:
````groovy
plugins {
    id 'java'
    id 'org.gauge' version "1.8.0"
}

group 'Gradle_example'
version '1.0-SNAPSHOT'

sourceCompatibility = 1.8
targetCompatibility = 1.8

repositories {
    mavenCentral()
}

dependencies {
    implementation 'com.thoughtworks.gauge:gauge-java:0.7.1'
    testImplementation group: 'org.assertj', name: 'assertj-core', version: '3.8.0'
}
````

### Executing specs using gradle
To execute gauge specs,

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

### Install from Nightly

* Add bintray repo url in maven.
* Update the version to nightly.

Example :-

```
buildscript {
    repositories {
        mavenCentral()
         maven {
            url "https://dl.bintray.com/gauge/gauge-gradle-plugin"
        }
    }
    dependencies {
        classpath 'org.gauge.gradle:gauge-gradle-plugin:1.8.0-nightly-2019-05-20'
    }
}
```


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
It is possible to define new custom Gauge tasks by extending `GaugePlugin` class. It can be used to create/configure tasks specific for different environments. For example,

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

### License

![GNU Public License version 3.0](http://www.gnu.org/graphics/gplv3-127x51.png)

Gauge Gradle Plugin is released under [GNU Public License Version 3.0](http://www.gnu.org/licenses/gpl-3.0.txt)

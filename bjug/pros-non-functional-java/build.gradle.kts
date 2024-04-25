plugins {
    id("java")
    id("me.champeau.jmh") version "0.7.2"
}

group = "org.example"

repositories {
    mavenCentral()
}

dependencies {
  implementation("org.openjdk.jmh:jmh-core:1.37")

    // see https://github.com/melix/jmh-gradle-plugin/blob/master/build.gradle.kts
    //implementation("org.openjdk.jmh:jmh-generator-annprocess:1.37")
    // this is the line that solves the missing /META-INF/BenchmarkList error
    jmhAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")
//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.test {
    useJUnitPlatform()
}
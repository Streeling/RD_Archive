plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("org.deeplearning4j:deeplearning4j-core:1.0.0-M2.1")
    implementation("org.deeplearning4j:deeplearning4j-nlp:1.0.0-M2.1")
    implementation("org.nd4j:nd4j-native-platform:1.0.0-M1")
    implementation("org.nd4j:nd4j-backends:1.0.0-M2.1")
    implementation("org.nd4j:nd4j-backends:1.0.0-M2.1")
    implementation("commons-cli:commons-cli:1.6.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.hamcrest:hamcrest-core:2.2")
}

tasks.test {
    useJUnitPlatform()
}
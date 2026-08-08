plugins {
    `java-library`
    `maven-publish`
    signing
}

group = "com.xposedornot"
version = "1.1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    api("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    api("com.fasterxml.jackson.core:jackson-annotations:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.0")
    implementation("org.bouncycastle:bcprov-jdk18on:1.78.1")

    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    testImplementation("org.mockito:mockito-core:5.11.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.javadoc {
    options {
        (this as StandardJavadocDocletOptions).apply {
            addStringOption("Xdoclint:none", "-quiet")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = "com.xposedornot"
            artifactId = "xposedornot"
            from(components["java"])
            pom {
                name.set("XposedOrNot Java Client")
                description.set("Java client library for the XposedOrNot data breach API")
                url.set("https://github.com/XposedOrNot/XposedOrNot-Java")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("xposedornot")
                        name.set("XposedOrNot")
                        email.set("deva@xposedornot.com")
                    }
                }
                scm {
                    url.set("https://github.com/XposedOrNot/XposedOrNot-Java")
                    connection.set("scm:git:git://github.com/XposedOrNot/XposedOrNot-Java.git")
                    developerConnection.set("scm:git:ssh://github.com/XposedOrNot/XposedOrNot-Java.git")
                }
            }
        }
    }
    repositories {
        maven {
            name = "local"
            url = uri(layout.buildDirectory.dir("repo"))
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

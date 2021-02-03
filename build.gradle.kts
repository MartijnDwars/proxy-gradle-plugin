plugins {
    `java-gradle-plugin`
    `maven-publish`
    signing
    id("com.gradle.plugin-publish") version "0.12.0"
}
 
tasks.named<Wrapper>("wrapper") {
    gradleVersion = "6.8.1"
}

group = "nl.martijndwars"
version = "0.1-SNAPSHOT"

java {                                      
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    jcenter()
}

dependencies {
    testImplementation("junit:junit:4.13")
}

pluginBundle {
    website = "https://github.com/MartijnDwars/proxy-gradle-plugin"
    vcsUrl = "https://github.com/MartijnDwars/proxy-gradle-plugin.git"
    tags = listOf("proxy", "corporate", "enterprise", "http_proxy", "no_proxy")
}

gradlePlugin {
    plugins {
        create("proxyPlugin") {
            id = "nl.martijndwars.proxy"
            displayName = "Proxy Gradle Plugin"
            description = "Make Gradle use the web proxy based on the HTTP_PROXY, HTTPS_PROXY and NO_PROXY variables."
            implementationClass = "nl.martijndwars.proxy.ProxyGradlePluginPlugin"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("martijndwars")
                        name.set("Martijn Dwars")
                        email.set("ikben@martijndwars.nl")
                    }
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}

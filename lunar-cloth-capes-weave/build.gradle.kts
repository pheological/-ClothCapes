plugins {
    kotlin("jvm") version "2.0.0"
}

group = "com.grappenmaker"
version = "0.1"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly("net.weavemc:Weave-Loader:0.2.6")
}

kotlin { jvmToolchain(8) }

tasks {
    jar {
        from(configurations.runtimeClasspath.map { conf ->
            conf.map { if (it.isDirectory) it else zipTree(it) }
        })

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}
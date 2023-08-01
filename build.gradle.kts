plugins {
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.1.0"
    kotlin("jvm") version "1.9.0"
    application
}

group = "dev.themeinerlp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    implementation("cloud.commandframework", "cloud-paper", "1.8.2")
    implementation("cloud.commandframework", "cloud-annotations", "1.8.2")
    implementation("cloud.commandframework", "cloud-minecraft-extras", "1.8.2")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("me.lucko:commodore:2.2") {
        isTransitive = false
    }
    testImplementation(kotlin("test"))
}

tasks {
    test {
        useJUnitPlatform()
    }
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    runServer {
        minecraftVersion("1.20.1")
        jvmArgs("-DPaper.IgnoreJavaVersion=true", "-Dcom.mojang.eula.agree=true")
    }

    shadowJar {
        archiveFileName.set("${rootProject.name}.${archiveExtension.getOrElse("jar")}")
    }
}

paper {
    main = "${rootProject.group}.mmsst.MMSST"
    apiVersion = "1.20"
    name = "MMSST"
}

kotlin {
    jvmToolchain(17)
}

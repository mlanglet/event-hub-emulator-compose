plugins {
    id("kotlin-common-conventions")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.microsoft.azure.azurefunctions")
}

group = "eventHubEmulatorCompose"
version = "1.0.0"

dependencies {
    implementation(libs.azure.functions.java)
    implementation(libs.azure.messaging.eventhubs)
    implementation(libs.spring.function.adapter)
    implementation(libs.spring.boot.starter.logging)

    runtimeOnly(libs.kotlin.reflect)
}

azurefunctions {
    localDebug = "transport=dt_socket,server=y,suspend=n,address=5005"
    appName = "eventHubEmulatorCompose"

    // Need to set these to run azure-function-gradle-plugin tasks locally
    resourceGroup = "resourceGroup"
    pricingTier = "Consumption"
    region = "westeurope"
}

tasks {
    jar {
        archiveClassifier.set("")
        manifest {
            attributes["Main-Class"] = "eventHubEmulatorCompose.AzureFunctionApplication"
        }
    }
    azureFunctionsDeploy {
        enabled = false
    }
    azureFunctionsPackage {
        dependsOn("build")
    }
    azureFunctionsPackageZip {
        dependsOn("build")
    }
}

tasks.build {
    finalizedBy("azureFunctionsPackageZip")
}

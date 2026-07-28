rootProject.name = "AI_Account_Book"

pluginManagement {
  repositories {
    google {
      maven { url = uri("https://maven.aliyun.com/repository/google") }
      maven { url = uri("https://maven.aliyun.com/repository/releases") }
      maven { url = uri("https://maven.aliyun.com/repository/central") }
      maven { url = uri("https://maven.aliyun.com/repository/public") }
      maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
      maven { url = uri("https://maven.aliyun.com/repository/apache-snapshots") }
      maven { url = uri("https://maven.aliyun.com/nexus/content/groups/public/") }
      maven { url = uri("https://jitpack.io") }
      mavenContent {
        includeGroupAndSubgroups("androidx")
        includeGroupAndSubgroups("com.android")
        includeGroupAndSubgroups("com.google")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositories {
    google {
      maven { url = uri("https://maven.aliyun.com/repository/google") }
      maven { url = uri("https://maven.aliyun.com/repository/releases") }
      maven { url = uri("https://maven.aliyun.com/repository/central") }
      maven { url = uri("https://maven.aliyun.com/repository/public") }
      maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
      maven { url = uri("https://maven.aliyun.com/repository/apache-snapshots") }
      maven { url = uri("https://maven.aliyun.com/nexus/content/groups/public/") }
      maven { url = uri("https://jitpack.io") }
      mavenContent {
        includeGroupAndSubgroups("androidx")
        includeGroupAndSubgroups("com.android")
        includeGroupAndSubgroups("com.google")
      }
    }
    mavenCentral()
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":androidApp")
include(":desktopApp")
include(":shared")
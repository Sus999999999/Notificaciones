allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

val newBuildDir: Directory =
    rootProject.layout.buildDirectory
        .dir("../../build")
        .get()
rootProject.layout.buildDirectory.value(newBuildDir)

subprojects {
    val newSubprojectBuildDir: Directory = newBuildDir.dir(project.name)
    project.layout.buildDirectory.value(newSubprojectBuildDir)
}
subprojects {
    project.evaluationDependsOn(":app")
}

subprojects {
    if (project.name != "app") {
        val configureAndroid = {
            val extension = project.extensions.findByName("android")
            if (extension != null) {
                val appCompileSdk = try {
                    val appExt = project(":app").extensions.findByName("android")
                    val getCompileSdk = appExt?.javaClass?.methods?.find { it.name == "getCompileSdk" && it.parameterCount == 0 }
                    getCompileSdk?.invoke(appExt) as? Int
                } catch (_: Exception) {
                    null
                } ?: 34

                val targetSdk = if (appCompileSdk >= 34) appCompileSdk else 34

                try {
                    val setCompileSdk = extension.javaClass.methods.find {
                        it.name == "setCompileSdk" && it.parameterCount == 1
                    }
                    setCompileSdk?.invoke(extension, targetSdk)
                } catch (_: Exception) {}

                try {
                    val compileSdkVersion = extension.javaClass.methods.find {
                        it.name == "compileSdkVersion" && it.parameterCount == 1 &&
                            (it.parameterTypes[0] == Int::class.javaPrimitiveType || it.parameterTypes[0] == java.lang.Integer::class.java)
                    }
                    compileSdkVersion?.invoke(extension, targetSdk)
                } catch (_: Exception) {}
            }
        }

        if (project.state.executed) {
            configureAndroid()
        } else {
            afterEvaluate { configureAndroid() }
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}

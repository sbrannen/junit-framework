plugins {
	java
	id("org.graalvm.buildtools.native")
}

val jupiterVersion: String by project
val platformVersion: String by project
val vintageVersion: String by project

dependencies {
	testImplementation("org.junit.jupiter:junit-jupiter:$jupiterVersion")
	testImplementation("junit:junit:4.13.2")
	testImplementation("org.junit.platform:junit-platform-suite:$platformVersion")
	testRuntimeOnly("org.junit.vintage:junit-vintage-engine:$vintageVersion")
	testRuntimeOnly("org.junit.platform:junit-platform-reporting:$platformVersion")
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 21
}

tasks.test {
	useJUnitPlatform {
		includeEngines("junit-platform-suite")
	}

	val outputDir = reports.junitXml.outputLocation
	jvmArgumentProviders += CommandLineArgumentProvider {
		listOf(
			"-Djunit.platform.reporting.open.xml.enabled=true",
			"-Djunit.platform.reporting.output.dir=${outputDir.get().asFile.absolutePath}"
		)
	}
}

val initializeAtBuildTime = mapOf<String, List<String>>(
	// These need to be added to native-build-tools
	"5.14" to listOf(
		"org.junit.platform.commons.util.DefaultClasspathScanner",
		"org.junit.platform.launcher.core.HierarchicalOutputDirectoryCreator",
	),
)

graalvmNative {
	binaries {
		named("test") {
			buildArgs.add("-H:+ReportExceptionStackTraces")
			buildArgs.add("--initialize-at-build-time=${initializeAtBuildTime.values.flatten().joinToString(",")}")
		}
	}
}

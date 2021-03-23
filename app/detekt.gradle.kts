/**
 * `detektOnFiles` task runs `detekt` on given files
 * INPUT is a string that contains comma separated paths to Kotlin files
 * ./gradlew detektOnFiles -PkotlinFiles=$INPUT
 * E.g `./gradlew detektOnFiles -PkotlinFiles="src/main/kotlin/DetektDemo.kt,src/main/kotlin/PSIDemo`
 */
task<io.gitlab.arturbosch.detekt.Detekt>("detektOnFiles") {
    val argumentName = "kotlinFiles"
    if (project.hasProperty(argumentName)) {
        val kotlinFiles: String by project
        val listOfFiles: List<String>? = kotlinFiles.takeIf { it.isNotBlank() }?.split(",")
        listOfFiles?.let { fileList ->
            setSource(files(fileList))
            config.setFrom(files("./detekt-ruleset.yml"))
            autoCorrect = true
            reports {
                xml {
                    enabled = true
                    destination = file("./build/detekt/reports/detekt.xml")
                }
                html {
                    enabled = true
                    destination = file("./build/detekt/reports/detekt.html")
                }
            }
        }
    }
}

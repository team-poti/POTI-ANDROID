// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}

tasks.register("installGitHook") {
    description = "Installs the pre-commit git hook"
    group = "help"
    doLast {
        val preCommitFile = file(".git/hooks/pre-commit")
        preCommitFile.writeText(
            """
            #!/bin/bash
            echo "Running ktlintFormat..."
            ./gradlew ktlintFormat
            if [ $? -ne 0 ]; then
                echo "❌ ktlintFormat failed! Please fix the errors."
                exit 1
            fi
            git add .
            echo "✅ ktlintFormat finished!"
            """.trimIndent().replace("\r\n", "\n")
        )
        try {
            val permissions = java.nio.file.attribute.PosixFilePermissions.fromString("rwxr-xr-x")
            java.nio.file.Files.setPosixFilePermissions(preCommitFile.toPath(), permissions)
        } catch (e: Exception) {
        }
        println("✅ Git pre-commit hook installed successfully!")
    }
}

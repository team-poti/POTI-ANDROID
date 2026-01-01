// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}

tasks.register("installPrePushHook") {
    description = "Installs the pre-push git hook"
    group = "help"
    doLast {
        val prePushFile = file(".git/hooks/pre-push")
        val script = """
            #!/bin/bash
            echo "--------------------------------------------------"
            echo "🚀 Pushing to GitHub... Running ktlintCheck first."
            echo "--------------------------------------------------"

            ./gradlew ktlintCheck

            if [ $? -ne 0 ]; then
                echo ""
                echo "❌ ktlintCheck failed!"
                echo "스타일 가이드를 위반했습니다. 코드를 수정하고 다시 푸시해주세요."
                echo "Tip: ./gradlew ktlintFormat 을 실행하면 자동으로 고쳐집니다."
                echo "--------------------------------------------------"
                exit 1
            fi

            echo "✅ Style check passed! Proceeding with push..."
            echo "--------------------------------------------------"
        """.trimIndent().replace("\r\n", "\n") + "\n"

        prePushFile.writeText(script)
        prePushFile.setExecutable(true)
        println("✅ Git pre-push hook installed successfully!")
    }
}

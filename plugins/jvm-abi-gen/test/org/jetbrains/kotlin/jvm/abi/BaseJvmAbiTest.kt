/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.jvm.abi

import junit.framework.TestCase
import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services
import org.jetbrains.kotlin.incremental.utils.TestMessageCollector
import java.io.File

abstract class BaseJvmAbiTest : TestCase() {
    private lateinit var workingDir: File

    override fun setUp() {
        super.setUp()
        workingDir = createTempDir(javaClass.simpleName)
        workingDir.deleteOnExit()
    }

    override fun tearDown() {
        workingDir.deleteRecursively()
        super.tearDown()
    }

    private val abiPluginJar = File("dist/kotlinc/lib/jvm-abi-gen.jar")
    private fun abiOption(option: String, value: String): String =
        "plugin:${JvmAbiCommandLineProcessor.COMPILER_PLUGIN_ID}:$option=$value"

    inner class Compilation(val projectDir: File, val name: String) {
        val srcDir: File
            get() = projectDir.resolve(name)

        val destinationDir: File
            get() = workingDir.resolve("$name/out")

        val abiDir: File
            get() = workingDir.resolve("$name/abi")

        override fun toString(): String =
            "compilation '$name'"
    }

    fun make(compilation: Compilation) {
        check(abiPluginJar.exists()) { "Plugin jar '$abiPluginJar' does not exist" }
        check(compilation.srcDir.exists()) { "Source dir '${compilation.srcDir}' does not exist" }

        val messageCollector = TestMessageCollector()
        val compiler = K2JVMCompiler()
        val args = compiler.createArguments().apply {
            freeArgs = listOf(compilation.srcDir.canonicalPath)
            pluginClasspaths = arrayOf(abiPluginJar.canonicalPath)
            pluginOptions = arrayOf(abiOption("outputDir", compilation.abiDir.canonicalPath))
            destination = compilation.destinationDir.canonicalPath
        }
        val exitCode = compiler.exec(messageCollector, Services.EMPTY, args)
        if (exitCode != ExitCode.OK || messageCollector.errors.isNotEmpty()) {
            val errorLines = listOf("Could not compile $compilation", "Exit code: $exitCode", "Errors:") + messageCollector.errors
            error(errorLines.joinToString("\n"))
        }
    }
}
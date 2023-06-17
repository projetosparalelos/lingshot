package helper

import java.io.File
import java.io.FileInputStream
import java.util.Properties

object KeyHelper {
    private val properties by lazy {
        Properties().apply { load(FileInputStream(File("teach-me-print-keys.properties"))) }
    }

    fun getValue(key: String): String {
        return properties.getProperty(key) ?: ""
    }
}
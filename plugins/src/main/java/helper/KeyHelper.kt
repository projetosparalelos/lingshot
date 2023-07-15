package helper

import java.io.File
import java.io.FileInputStream
import java.util.Properties

internal object KeyHelper {
    private val properties by lazy {
        Properties().apply { load(FileInputStream(File("lingshot-keys.properties"))) }
    }

    fun getValue(key: String): String {
        return properties.getProperty(key) ?: ""
    }
}

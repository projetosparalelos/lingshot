package extension

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun VersionCatalog.getVersion(library: String): String = findVersion(library).get().requiredVersion

fun VersionCatalog.getLibrary(library: String) = findLibrary(library).get()

fun VersionCatalog.getBundle(bundle: String) = findBundle(bundle).get()

package extension

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

internal val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun VersionCatalog.getVersion(library: String): String = findVersion(library).get().requiredVersion

internal fun VersionCatalog.getLibrary(library: String) = findLibrary(library).get()

internal fun VersionCatalog.getBundle(bundle: String) = findBundle(bundle).get()

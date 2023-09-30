# Detekt
Dir["**/reports/detekt/detekt.xml"].each do |file_name|
  kotlin_detekt.severity = "warning"
  kotlin_detekt.gradle_task = "detekt"
  kotlin_detekt.report_file = file_name
  kotlin_detekt.detekt(inline_mode: true)
end

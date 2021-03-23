kotlin_files = (git.added_files + git.modified_files).select{ |file| file.end_with?(".kt", ".kts") }
unless kotlin_files.empty?
  comma_separated_kotlin_files = kotlin_files.join(",")
  message comma_separated_kotlin_files
  detekt_report_file = "./build/reports/detekt/detekt.xml"
  message `./gradlew detektOnFiles -PkotlinFiles=#{comma_separated_kotlin_files}`
  checkstyle_format.base_path = Dir.pwd
  checkstyle_format.report detekt_report_file
end

kotlin_files = (git.added_files + git.modified_files).select{ |file| file.end_with?(".kt", ".kts") }
kotlin_files = kotlin_files.collect{ |s| s[4..-1] }
unless kotlin_files.empty?
  comma_separated_kotlin_files = kotlin_files.join(",")
  puts "Files to scan are: "+comma_separated_kotlin_files
  detekt_report_file = Dir.pwd+"/app/build/reports/detekt/release.xml"
  puts `./gradlew detektRelease`
  checkstyle_format.base_path = Dir.pwd
  checkstyle_format.report detekt_report_file
end

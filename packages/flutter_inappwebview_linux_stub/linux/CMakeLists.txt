cmake_minimum_required(VERSION 3.10)
project(flutter_inappwebview_linux_plugin LANGUAGES CXX)

set(PLUGIN_NAME "flutter_inappwebview_linux_plugin")

add_library(${PLUGIN_NAME} SHARED
  "in_app_web_view_linux_plugin.cc"
)

apply_standard_settings(${PLUGIN_NAME})

target_compile_definitions(${PLUGIN_NAME} PRIVATE FLUTTER_PLUGIN_IMPL)

target_include_directories(${PLUGIN_NAME} INTERFACE
  "${CMAKE_CURRENT_SOURCE_DIR}/include"
)

target_link_libraries(${PLUGIN_NAME} PRIVATE flutter)
target_link_libraries(${PLUGIN_NAME} PRIVATE PkgConfig::GTK)

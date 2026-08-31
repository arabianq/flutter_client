# Shared branding configuration.
#
# Branding is driven by tool/dart_defines/custom.json so the app can be
# rebranded (and installed in parallel with the original client) from a single
# file. When the file or a key is missing, upstream defaults are used:
#   APP_NAME   -> human-readable display name (window titles, etc.)
#   APP_ID     -> application/bundle identifier
# Derived values:
#   BINARY_NAME -> executable name, e.g. "wayg_chat"
#   PKG_NAME    -> package name, e.g. "wayg-chat"

function(_branding_read_key _file _key _fallback _out_var)
  set(${_out_var} "${_fallback}" PARENT_SCOPE)
  if(NOT EXISTS "${_file}")
    return()
  endif()
  file(READ "${_file}" _contents)
  string(REGEX MATCH "\"${_key}\"[ \t\r\n]*:[ \t\r\n]*\"[^\"]*\"" _match "${_contents}")
  if(_match STREQUAL "")
    return()
  endif()
  string(REGEX REPLACE "\"${_key}\"[ \t\r\n]*:[ \t\r\n]*\"" "" _value "${_match}")
  string(REGEX REPLACE "\"$" "" _value "${_value}")
  if(NOT _value STREQUAL "")
    set(${_out_var} "${_value}" PARENT_SCOPE)
  endif()
endfunction()

function(wayg_read_branding _defines_file)
  _branding_read_key("${_defines_file}" "APP_NAME" "fluxer_app" _app_name)
  _branding_read_key("${_defines_file}" "APP_ID" "com.parakeetsoft.fluxer_app" _app_id)
  string(TOLOWER "${_app_name}" _slug)
  string(REGEX REPLACE "[^a-z0-9]+" "_" _slug "${_slug}")
  string(REGEX REPLACE "_+$" "" _slug "${_slug}")
  string(REGEX REPLACE "_" "-" _pkg "${_slug}")
  set(BRANDING_APP_NAME "${_app_name}" PARENT_SCOPE)
  set(BRANDING_APP_ID "${_app_id}" PARENT_SCOPE)
  set(BRANDING_BINARY_NAME "${_slug}" PARENT_SCOPE)
  set(BRANDING_PKG_NAME "${_pkg}" PARENT_SCOPE)
endfunction()

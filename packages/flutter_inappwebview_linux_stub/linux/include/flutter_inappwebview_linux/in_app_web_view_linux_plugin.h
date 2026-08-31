#ifndef FLUTTER_PLUGIN_IN_APP_WEB_VIEW_LINUX_PLUGIN_H_
#define FLUTTER_PLUGIN_IN_APP_WEB_VIEW_LINUX_PLUGIN_H_

#include <flutter_linux/flutter_linux.h>

#if defined(__cplusplus)
extern "C" {
#endif

G_MODULE_EXPORT void in_app_web_view_linux_plugin_register_with_registrar(
    FlPluginRegistrar* registrar);

#if defined(__cplusplus)
}
#endif

#endif  // FLUTTER_PLUGIN_IN_APP_WEB_VIEW_LINUX_PLUGIN_H_

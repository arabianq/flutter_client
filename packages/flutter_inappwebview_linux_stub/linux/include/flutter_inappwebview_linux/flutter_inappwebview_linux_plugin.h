#ifndef FLUTTER_PLUGIN_FLUTTER_INAPPWEBVIEW_LINUX_PLUGIN_H_
#define FLUTTER_PLUGIN_FLUTTER_INAPPWEBVIEW_LINUX_PLUGIN_H_

#include <flutter_linux/flutter_linux.h>

#if defined(__cplusplus)
extern "C" {
#endif

G_MODULE_EXPORT void flutter_inappwebview_linux_plugin_register_with_registrar(
    FlPluginRegistrar* registrar);

#if defined(__cplusplus)
}
#endif

#endif  // FLUTTER_PLUGIN_FLUTTER_INAPPWEBVIEW_LINUX_PLUGIN_H_

import 'package:flutter_inappwebview_platform_interface/flutter_inappwebview_platform_interface.dart';

class InAppWebViewLinux extends InAppWebViewPlatform {
  static void registerWith() {
    InAppWebViewPlatform.instance = InAppWebViewLinux();
  }
}

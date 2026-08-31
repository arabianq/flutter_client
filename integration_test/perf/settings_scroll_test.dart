import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:integration_test/integration_test.dart';

import '../support/app_launcher.dart';
import '../support/perf_utils.dart';
import '../support/scroll_utils.dart';
import '../support/test_account.dart';

void main() {
  final IntegrationTestWidgetsFlutterBinding binding =
      IntegrationTestWidgetsFlutterBinding.ensureInitialized();

  testWidgets('profile and settings scroll perf', (WidgetTester tester) async {
    if (kIsWeb) {
      return;
    }

    await launchFluxerApp(tester);
    await ensureAuthenticated(tester);
    await tapBottomNav(tester, 'You');
    await tester.pump(const Duration(seconds: 2));

    expect(find.bySemanticsLabel('You'), findsWidgets);

    expect(find.byType(Scrollable), findsWidgets);

    await traceScrollPerf(
      binding,
      tester,
      reportKey: 'settings_scroll',
      scrollTarget: findPrimaryScrollable(),
      flingCount: 8,
    );
  });
}

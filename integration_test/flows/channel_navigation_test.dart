import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:integration_test/integration_test.dart';

import '../support/app_launcher.dart';
import '../support/navigation_helpers.dart';
import '../support/test_account.dart';

void main() {
  IntegrationTestWidgetsFlutterBinding.ensureInitialized();

  testWidgets('open guild channel from sidebar', (WidgetTester tester) async {
    if (kIsWeb) {
      return;
    }

    await launchFluxerApp(tester);
    await ensureAuthenticated(tester);
    await openGuildChannel(tester);

    expect(find.byType(ListView), findsWidgets);
    expect(find.bySemanticsLabel('Loading messages'), findsNothing);
  });

  testWidgets('open dm channel from home list', (WidgetTester tester) async {
    if (kIsWeb) {
      return;
    }

    await launchFluxerApp(tester);
    await ensureAuthenticated(tester);
    await openDmChannel(tester);

    expect(find.byType(ListView), findsWidgets);
    expect(find.bySemanticsLabel('Loading messages'), findsNothing);
  });
}

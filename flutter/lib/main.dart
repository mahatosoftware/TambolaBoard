import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:google_mobile_ads/google_mobile_ads.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'firebase_options.dart';
import 'localization/app_localizations.dart';
import 'localization/language_option.dart';
import 'screens/main_screen.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  try {
    await Firebase.initializeApp(
      options: DefaultFirebaseOptions.currentPlatform,
    );
  } catch (e) {
    debugPrint("Firebase init error: $e");
  }

  try {
    MobileAds.instance.initialize();
  } catch (e) {
    debugPrint("MobileAds init error: $e");
  }

  runApp(const TambolaApp());
}

class TambolaApp extends StatefulWidget {
  const TambolaApp({super.key});

  static void setLocale(BuildContext context, String code) {
    final state = context.findAncestorStateOfType<TambolaAppState>() ??
        context.findRootAncestorStateOfType<TambolaAppState>();
    state?.setLocale(code);
  }

  @override
  State<TambolaApp> createState() => TambolaAppState();
}

class TambolaAppState extends State<TambolaApp> {
  Locale _locale = const Locale('en');

  @override
  void initState() {
    super.initState();
    _loadSavedLanguage();
  }

  Future<void> _loadSavedLanguage() async {
    final prefs = await SharedPreferences.getInstance();
    final code = prefs.getString('selected_language') ?? 'en';
    if (mounted) {
      setState(() {
        _locale = Locale(code);
      });
    }
  }

  void setLocale(String code) {
    setState(() {
      _locale = Locale(code);
    });
    SharedPreferences.getInstance().then((prefs) {
      prefs.setString('selected_language', code);
    });
  }

  @override
  Widget build(BuildContext context) {
    const purpleBg = Color(0xFF6C0BA9);
    const purpleDark = Color(0xFF4A007E);
    const shamrockGreen = Color(0xFF009E60);
    const byzantium = Color(0xFF702963);
    const bronze = Color(0xFFCD7F32);

    final colorScheme = ColorScheme(
      brightness: Brightness.light,
      primary: purpleBg,
      onPrimary: Colors.white,
      primaryContainer: shamrockGreen,
      onPrimaryContainer: Colors.white,
      secondary: purpleDark,
      onSecondary: Colors.white,
      secondaryContainer: byzantium,
      onSecondaryContainer: Colors.white,
      tertiary: Colors.white,
      onTertiary: Colors.black,
      tertiaryContainer: bronze,
      onTertiaryContainer: Colors.white,
      error: Colors.red,
      onError: Colors.white,
      surface: purpleBg,
      onSurface: Colors.white,
    );

    return MaterialApp(
      title: 'Tambola Board',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        useMaterial3: true,
        colorScheme: colorScheme,
        scaffoldBackgroundColor: purpleBg,
        dialogTheme: DialogThemeData(
          backgroundColor: const Color(0xFF3C0084),
          surfaceTintColor: Colors.transparent,
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
          titleTextStyle: const TextStyle(
            fontSize: 20,
            fontWeight: FontWeight.bold,
            color: Colors.white,
          ),
          contentTextStyle: const TextStyle(
            fontSize: 16,
            color: Colors.white70,
          ),
        ),
        textButtonTheme: TextButtonThemeData(
          style: ButtonStyle(
            foregroundColor: WidgetStateProperty.resolveWith<Color>((states) {
              if (states.contains(WidgetState.focused)) {
                return const Color(0xFFFFD700);
              }
              return const Color(0xFF009E60);
            }),
            side: WidgetStateProperty.resolveWith<BorderSide?>((states) {
              if (states.contains(WidgetState.focused)) {
                return const BorderSide(color: Color(0xFFFFD700), width: 2.5);
              }
              return null;
            }),
            overlayColor: WidgetStateProperty.resolveWith<Color?>((states) {
              if (states.contains(WidgetState.focused)) {
                return const Color(0xFFFFD700).withValues(alpha: 0.25);
              }
              return null;
            }),
            textStyle: WidgetStateProperty.all(
              const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
            ),
          ),
        ),
        outlinedButtonTheme: OutlinedButtonThemeData(
          style: ButtonStyle(
            foregroundColor: WidgetStateProperty.resolveWith<Color>((states) {
              if (states.contains(WidgetState.focused)) {
                return const Color(0xFFFFD700);
              }
              return Colors.white;
            }),
            side: WidgetStateProperty.resolveWith<BorderSide>((states) {
              if (states.contains(WidgetState.focused)) {
                return const BorderSide(color: Color(0xFFFFD700), width: 3.0);
              }
              return const BorderSide(color: Colors.white54, width: 1.5);
            }),
            overlayColor: WidgetStateProperty.resolveWith<Color?>((states) {
              if (states.contains(WidgetState.focused)) {
                return const Color(0xFFFFD700).withValues(alpha: 0.25);
              }
              return null;
            }),
            textStyle: WidgetStateProperty.all(
              const TextStyle(fontWeight: FontWeight.bold),
            ),
          ),
        ),
        elevatedButtonTheme: ElevatedButtonThemeData(
          style: ButtonStyle(
            backgroundColor: WidgetStateProperty.resolveWith<Color>((states) {
              if (states.contains(WidgetState.focused)) {
                return const Color(0xFF3C0084);
              }
              return colorScheme.primaryContainer;
            }),
            foregroundColor: WidgetStateProperty.resolveWith<Color>((states) {
              if (states.contains(WidgetState.focused)) {
                return const Color(0xFFFFD700);
              }
              return Colors.white;
            }),
            side: WidgetStateProperty.resolveWith<BorderSide?>((states) {
              if (states.contains(WidgetState.focused)) {
                return const BorderSide(color: Color(0xFFFFD700), width: 3.0);
              }
              return const BorderSide(color: Colors.transparent, width: 3.0);
            }),
            elevation: WidgetStateProperty.resolveWith<double>((states) {
              if (states.contains(WidgetState.focused)) {
                return 8.0;
              }
              return 2.0;
            }),
            shadowColor: WidgetStateProperty.resolveWith<Color?>((states) {
              if (states.contains(WidgetState.focused)) {
                return const Color(0xFFFFD700);
              }
              return null;
            }),
            overlayColor: WidgetStateProperty.resolveWith<Color?>((states) {
              if (states.contains(WidgetState.focused)) {
                return const Color(0xFFFFD700).withValues(alpha: 0.25);
              }
              return null;
            }),
            textStyle: WidgetStateProperty.all(
              const TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
            ),
          ),
        ),
        iconButtonTheme: IconButtonThemeData(
          style: ButtonStyle(
            foregroundColor: WidgetStateProperty.resolveWith<Color>((states) {
              if (states.contains(WidgetState.focused)) {
                return const Color(0xFFFFD700);
              }
              return Colors.white;
            }),
            side: WidgetStateProperty.resolveWith<BorderSide?>((states) {
              if (states.contains(WidgetState.focused)) {
                return const BorderSide(color: Color(0xFFFFD700), width: 2.5);
              }
              return null;
            }),
            overlayColor: WidgetStateProperty.resolveWith<Color?>((states) {
              if (states.contains(WidgetState.focused)) {
                return const Color(0xFFFFD700).withValues(alpha: 0.25);
              }
              return null;
            }),
          ),
        ),
      ),
      locale: _locale,
      supportedLocales: supportedLanguages.map((l) => Locale(l.code)).toList(),
      localizationsDelegates: const [
        AppLocalizations.delegate,
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
        GlobalCupertinoLocalizations.delegate,
      ],
      localeResolutionCallback: (locale, supportedLocales) {
        return _locale;
      },
      home: MainScreen(
        onLanguageChanged: setLocale,
      ),
    );
  }
}

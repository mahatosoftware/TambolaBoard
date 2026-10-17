import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../localization/app_localizations.dart';
import '../../localization/language_option.dart';
import '../../main.dart';

class LanguageSettingsDialog extends StatefulWidget {
  final Function(String)? onLanguageChanged;

  const LanguageSettingsDialog({
    super.key,
    this.onLanguageChanged,
  });

  @override
  State<LanguageSettingsDialog> createState() => _LanguageSettingsDialogState();
}

class _LanguageSettingsDialogState extends State<LanguageSettingsDialog> {
  late String _currentLang;

  @override
  void initState() {
    super.initState();
    _currentLang = 'en';
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    final activeCode = AppLocalizations.of(context)?.locale.languageCode;
    if (activeCode != null && activeCode.isNotEmpty) {
      _currentLang = activeCode;
    }
  }

  void _selectLanguage(String code) {
    setState(() {
      _currentLang = code;
    });

    TambolaApp.setLocale(context, code);

    if (widget.onLanguageChanged != null) {
      widget.onLanguageChanged!(code);
    }

    SharedPreferences.getInstance().then((prefs) {
      prefs.setString('selected_language', code);
    });

    if (mounted) {
      Navigator.pop(context);
    }
  }

  @override
  Widget build(BuildContext context) {
    final loc = AppLocalizations.of(context)!;
    final theme = Theme.of(context);
    final size = MediaQuery.of(context).size;

    return Dialog(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
      backgroundColor: theme.colorScheme.primary,
      child: Container(
        constraints: BoxConstraints(
          maxHeight: size.height * 0.85,
          maxWidth: 450,
        ),
        padding: const EdgeInsets.all(24),
        child: Column(
          children: [
            Text(
              loc.translate('title_settings'),
              style: const TextStyle(
                fontWeight: FontWeight.w900,
                fontSize: 24,
                color: Colors.white,
              ),
            ),
            const SizedBox(height: 6),
            Text(
              loc.translate('label_select_language'),
              style: const TextStyle(
                fontWeight: FontWeight.w500,
                fontSize: 15,
                color: Color(0xFFE0E0E0),
              ),
            ),
            const SizedBox(height: 4),
            Text(
              loc.translate('hint_tv_language_select'),
              style: const TextStyle(
                fontWeight: FontWeight.bold,
                fontSize: 12,
                color: Color(0xFFFFD700),
              ),
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 16),
            Expanded(
              child: ListView.separated(
                itemCount: supportedLanguages.length,
                separatorBuilder: (context, index) => const SizedBox(height: 6),
                itemBuilder: (context, index) {
                  final option = supportedLanguages[index];
                  final isSelected = _currentLang == option.code;

                  return _LanguageTile(
                    option: option,
                    isSelected: isSelected,
                    onTap: () => _selectLanguage(option.code),
                  );
                },
              ),
            ),
            const SizedBox(height: 16),
            SizedBox(
              width: double.infinity,
              child: _CloseButton(
                text: loc.translate('btn_close'),
                onPressed: () => Navigator.pop(context),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _LanguageTile extends StatefulWidget {
  final LanguageOption option;
  final bool isSelected;
  final VoidCallback onTap;

  const _LanguageTile({
    required this.option,
    required this.isSelected,
    required this.onTap,
  });

  @override
  State<_LanguageTile> createState() => _LanguageTileState();
}

class _LanguageTileState extends State<_LanguageTile> {
  bool _isFocused = false;

  @override
  Widget build(BuildContext context) {
    final isSelected = widget.isSelected;
    final isFocused = _isFocused;

    return InkWell(
      autofocus: isSelected,
      onFocusChange: (focused) {
        setState(() {
          _isFocused = focused;
        });
      },
      borderRadius: BorderRadius.circular(10),
      onTap: widget.onTap,
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 150),
        padding: const EdgeInsets.symmetric(vertical: 10, horizontal: 12),
        decoration: BoxDecoration(
          color: isFocused
              ? const Color(0xFFFFD700).withValues(alpha: 0.25)
              : isSelected
                  ? const Color(0xFF009E60).withValues(alpha: 0.25)
                  : Colors.white.withValues(alpha: 0.05),
          borderRadius: BorderRadius.circular(10),
          border: Border.all(
            color: isFocused
                ? const Color(0xFFFFD700)
                : isSelected
                    ? const Color(0xFF009E60)
                    : Colors.white24,
            width: isFocused ? 3 : (isSelected ? 2 : 1),
          ),
          boxShadow: isFocused
              ? [
                  BoxShadow(
                    color: const Color(0xFFFFD700).withValues(alpha: 0.4),
                    blurRadius: 8,
                    spreadRadius: 1,
                  )
                ]
              : null,
        ),
        child: Row(
          children: [
            ExcludeFocus(
              child: Radio<String>(
                value: widget.option.code,
                // ignore: deprecated_member_use
                groupValue: isSelected ? widget.option.code : '',
                // ignore: deprecated_member_use
                onChanged: (_) => widget.onTap(),
                activeColor: isFocused ? const Color(0xFFFFD700) : const Color(0xFF009E60),
              ),
            ),
            const SizedBox(width: 8),
            Expanded(
              child: Text(
                "${widget.option.nameEnglish} (${widget.option.nameNative})",
                style: TextStyle(
                  fontSize: 16,
                  fontWeight: (isSelected || isFocused)
                      ? FontWeight.bold
                      : FontWeight.w500,
                  color: isFocused ? const Color(0xFFFFD700) : Colors.white,
                ),
              ),
            ),
            if (isSelected)
              Icon(
                Icons.check_circle,
                color: isFocused ? const Color(0xFFFFD700) : const Color(0xFF009E60),
                size: 22,
              ),
          ],
        ),
      ),
    );
  }
}

class _CloseButton extends StatefulWidget {
  final String text;
  final VoidCallback onPressed;

  const _CloseButton({
    required this.text,
    required this.onPressed,
  });

  @override
  State<_CloseButton> createState() => _CloseButtonState();
}

class _CloseButtonState extends State<_CloseButton> {
  bool _isFocused = false;

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);

    return ElevatedButton(
      onFocusChange: (focused) {
        setState(() {
          _isFocused = focused;
        });
      },
      style: ElevatedButton.styleFrom(
        backgroundColor: _isFocused
            ? const Color(0xFFFFD700)
            : theme.colorScheme.primaryContainer,
        foregroundColor: _isFocused
            ? Colors.black
            : theme.colorScheme.tertiary,
        side: _isFocused
            ? const BorderSide(color: Colors.white, width: 2)
            : null,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(10),
        ),
      ),
      onPressed: widget.onPressed,
      child: Text(
        widget.text,
        style: const TextStyle(
          fontWeight: FontWeight.bold,
          fontSize: 16,
        ),
      ),
    );
  }
}

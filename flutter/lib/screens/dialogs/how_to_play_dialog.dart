import 'package:flutter/material.dart';
import '../../localization/app_localizations.dart';
import '../../utils/general_util.dart';
import '../../utils/screen_size_util.dart';

const Color dialogBgColor = Color(0xFF3C0084);
const Color cardBgDark = Color(0xFF1E0738);
const Color innerCardBg = Color(0xFF2B104D);
const Color emeraldGreen = Color(0xFF00E676);
const Color shamrockGreen = Color(0xFF00C853);
const Color accentGold = Color(0xFFFFD700);
const Color accentPurple = Color(0xFFE1BEE7);
const Color softPurpleBorder = Color(0xFF9C27B0);

class HowToPlayDialog extends StatelessWidget {
  const HowToPlayDialog({super.key});

  @override
  Widget build(BuildContext context) {
    final loc = AppLocalizations.of(context)!;
    final isTv = ScreenSizeUtil.isTv(context);
    final size = MediaQuery.of(context).size;

    return Dialog(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(24)),
      backgroundColor: dialogBgColor,
      child: Container(
        constraints: BoxConstraints(
          maxHeight: size.height * 0.92,
          maxWidth: 620,
        ),
        padding: const EdgeInsets.all(20),
        child: Column(
          children: [
            Text(
              loc.translate('title_how_to_play'),
              style: const TextStyle(
                fontWeight: FontWeight.w900,
                fontSize: 24,
                color: Colors.white,
              ),
            ),
            const SizedBox(height: 4),
            Text(
              loc.translate('subtitle_how_to_play'),
              style: const TextStyle(
                fontWeight: FontWeight.w500,
                fontSize: 14,
                color: Color(0xFFE0E0E0),
              ),
            ),
            const SizedBox(height: 16),
            Expanded(
              child: SingleChildScrollView(
                child: Column(
                  children: [
                    _StepHeader(stepNumber: "1", title: loc.translate('step_1_title')),
                    const SizedBox(height: 8),
                    _ModeratedModeCard(loc: loc),
                    const SizedBox(height: 12),
                    _UnmoderatedModeCard(loc: loc),
                    const SizedBox(height: 16),
                    _StepHeader(stepNumber: "2", title: loc.translate('step_2_title')),
                    const SizedBox(height: 8),
                    _StepSectionCard(description: loc.translate('step_2_desc')),
                    const SizedBox(height: 16),
                    _StepHeader(stepNumber: "3", title: loc.translate('step_3_title')),
                    const SizedBox(height: 8),
                    _StepSectionCard(description: loc.translate('step_3_desc')),
                    const SizedBox(height: 16),
                    _StepHeader(stepNumber: "4", title: loc.translate('step_4_title')),
                    const SizedBox(height: 8),
                    _StepSectionCard(description: loc.translate('step_4_desc')),
                    const SizedBox(height: 16),
                    _StepHeader(stepNumber: "5", title: loc.translate('step_5_title')),
                    const SizedBox(height: 8),
                    _StepSectionCard(
                      description: isTv
                          ? loc.translate('step_5_desc_tv')
                          : loc.translate('step_5_desc_mobile'),
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 16),
            SizedBox(
              width: double.infinity,
              height: 52,
              child: ElevatedButton(
                style: ElevatedButton.styleFrom(
                  backgroundColor: shamrockGreen,
                  foregroundColor: Colors.white,
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12),
                  ),
                ),
                onPressed: () => Navigator.pop(context),
                child: Text(
                  loc.translate('btn_close'),
                  style: const TextStyle(
                    fontWeight: FontWeight.w900,
                    fontSize: 17,
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _StepHeader extends StatelessWidget {
  final String stepNumber;
  final String title;

  const _StepHeader({required this.stepNumber, required this.title});

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Container(
          width: 34,
          height: 34,
          decoration: const BoxDecoration(
            color: accentGold,
            shape: BoxShape.circle,
          ),
          alignment: Alignment.center,
          child: Text(
            stepNumber,
            style: const TextStyle(
              fontWeight: FontWeight.w900,
              fontSize: 16,
              color: Colors.black,
            ),
          ),
        ),
        const SizedBox(width: 12),
        Expanded(
          child: Text(
            title,
            style: const TextStyle(
              fontWeight: FontWeight.w900,
              fontSize: 19,
              color: Colors.white,
            ),
          ),
        ),
      ],
    );
  }
}

class _ModeratedModeCard extends StatelessWidget {
  final AppLocalizations loc;

  const _ModeratedModeCard({required this.loc});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      decoration: BoxDecoration(
        color: cardBgDark,
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: emeraldGreen, width: 2),
      ),
      padding: const EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 5),
            decoration: BoxDecoration(
              color: shamrockGreen,
              borderRadius: BorderRadius.circular(8),
            ),
            child: Text(
              loc.translate('badge_recommended_app'),
              style: const TextStyle(
                fontWeight: FontWeight.w900,
                fontSize: 11,
                color: Colors.white,
              ),
            ),
          ),
          const SizedBox(height: 8),
          Text(
            loc.translate('mode_moderated_title'),
            style: const TextStyle(
              fontWeight: FontWeight.w900,
              fontSize: 20,
              color: emeraldGreen,
            ),
          ),
          Text(
            loc.translate('mode_moderated_subtitle'),
            style: const TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 15,
              color: accentGold,
            ),
          ),
          const SizedBox(height: 8),
          Text(
            loc.translate('mode_moderated_desc'),
            style: const TextStyle(
              fontSize: 14,
              fontWeight: FontWeight.w500,
              color: Colors.white,
            ),
          ),
          const SizedBox(height: 12),
          _ScenarioCard(
            title: loc.translate('scenario_digital_title'),
            desc: loc.translate('scenario_digital_desc'),
            titleColor: accentPurple,
          ),
          const SizedBox(height: 8),
          _ScenarioCard(
            title: loc.translate('scenario_paper_title'),
            desc: loc.translate('scenario_paper_desc'),
            titleColor: accentGold,
          ),
          const SizedBox(height: 12),
          Container(
            width: double.infinity,
            padding: const EdgeInsets.all(12),
            decoration: BoxDecoration(
              color: shamrockGreen,
              borderRadius: BorderRadius.circular(10),
            ),
            child: Text(
              loc.translate('rule_moderated_banner'),
              style: const TextStyle(
                fontWeight: FontWeight.w900,
                fontSize: 14,
                color: Colors.white,
              ),
            ),
          ),
          const SizedBox(height: 10),
          SizedBox(
            width: double.infinity,
            child: ElevatedButton(
              style: ElevatedButton.styleFrom(
                backgroundColor: accentGold,
                foregroundColor: Colors.black,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(10),
                ),
              ),
              onPressed: () => GeneralUtil.openPlayStore(),
              child: Text(
                loc.translate('btn_get_tickets_app'),
                style: const TextStyle(
                  fontWeight: FontWeight.w900,
                  fontSize: 14,
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class _UnmoderatedModeCard extends StatelessWidget {
  final AppLocalizations loc;

  const _UnmoderatedModeCard({required this.loc});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      decoration: BoxDecoration(
        color: cardBgDark,
        borderRadius: BorderRadius.circular(16),
        border: Border.all(color: softPurpleBorder, width: 1.5),
      ),
      padding: const EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            loc.translate('mode_unmoderated_title'),
            style: const TextStyle(
              fontWeight: FontWeight.w900,
              fontSize: 20,
              color: Color(0xFFE040FB),
            ),
          ),
          Text(
            loc.translate('mode_unmoderated_subtitle'),
            style: const TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 15,
              color: Color(0xFFCE93D8),
            ),
          ),
          const SizedBox(height: 8),
          Text(
            loc.translate('mode_unmoderated_desc'),
            style: const TextStyle(
              fontSize: 14,
              fontWeight: FontWeight.w500,
              color: Colors.white,
            ),
          ),
          const SizedBox(height: 10),
          Padding(
            padding: const EdgeInsets.only(left: 4),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(loc.translate('unmoderated_bullet_1'),
                    style: const TextStyle(fontSize: 13.5, color: Color(0xFFF5F5F5))),
                const SizedBox(height: 4),
                Text(loc.translate('unmoderated_bullet_2'),
                    style: const TextStyle(fontSize: 13.5, color: Color(0xFFF5F5F5))),
                const SizedBox(height: 4),
                Text(loc.translate('unmoderated_bullet_3'),
                    style: const TextStyle(fontSize: 13.5, color: Color(0xFFF5F5F5))),
                const SizedBox(height: 4),
                Text(loc.translate('unmoderated_bullet_4'),
                    style: const TextStyle(fontSize: 13.5, color: Color(0xFFF5F5F5))),
              ],
            ),
          ),
          const SizedBox(height: 12),
          Container(
            width: double.infinity,
            padding: const EdgeInsets.all(12),
            decoration: BoxDecoration(
              color: innerCardBg,
              borderRadius: BorderRadius.circular(10),
              border: Border.all(color: const Color(0xFFCE93D8)),
            ),
            child: Text(
              loc.translate('rule_unmoderated_banner'),
              style: const TextStyle(
                fontWeight: FontWeight.w900,
                fontSize: 14,
                color: Colors.white,
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class _ScenarioCard extends StatelessWidget {
  final String title;
  final String desc;
  final Color titleColor;

  const _ScenarioCard({
    required this.title,
    required this.desc,
    required this.titleColor,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: innerCardBg,
        borderRadius: BorderRadius.circular(10),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            title,
            style: TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 15,
              color: titleColor,
            ),
          ),
          const SizedBox(height: 3),
          Text(
            desc,
            style: const TextStyle(
              fontSize: 13,
              color: Color(0xFFF5F5F5),
              height: 1.3,
            ),
          ),
        ],
      ),
    );
  }
}

class _StepSectionCard extends StatelessWidget {
  final String description;

  const _StepSectionCard({required this.description});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        color: innerCardBg,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: softPurpleBorder.withValues(alpha: 0.5)),
      ),
      child: Text(
        description,
        style: const TextStyle(
          fontSize: 14,
          color: Colors.white,
          height: 1.4,
        ),
      ),
    );
  }
}

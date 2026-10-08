# Tambola Board - Flutter Application Functionality & Architecture Specification

## Overview
This document details the architecture, functionality, data models, localization, and features of the **Tambola Board** Flutter application converted from the native Android app (`in.mahato.tambola`).

---

## 1. Project & Package Configuration
- **Package Name**: `in.mahato.tambola`
- **Flutter Framework Version**: `3.47.6` / Dart `3.13.5`
- **Dependencies**:
  - `sqflite`: SQLite database for local game data persistence.
  - `firebase_core`, `firebase_auth`, `cloud_firestore`: Firebase Anonymous Sign-in and Firestore real-time game ID verification & player synchronization.
  - `flutter_tts`: Multi-language Text-To-Speech engine for number calling.
  - `qr_flutter`: Scannable QR code generation for Game ID.
  - `pdf` & `printing`: PDF document generation and system sharing for winner reports.
  - `google_mobile_ads`: AdMob Banner and Interstitial Ads integration.
  - `shared_preferences`: Persistent user preferences (selected language).
  - `url_launcher`: Play Store link launcher.

---

## 2. Localization & Multi-Language System
The application supports **26 languages** with full UI localization and language-specific TTS number phrases:
- **Languages Supported**: English (en), Hindi (hi), Bengali (bn), Odia (or), Kannada (kn), Tamil (ta), Marathi (mr), Telugu (te), Gujarati (gu), Malayalam (ml), Punjabi (pa), Assamese (as), Spanish (es), Portuguese (pt), French (fr), German (de), Arabic (ar), Indonesian (id), Turkish (tr), Italian (it), Japanese (ja), Korean (ko), Chinese (zh), Dutch (nl), Russian (ru), Vietnamese (vi).
- **TTS Phrases**: Numbers 1..90 speak funny/rhyming phrases customized per language (e.g., "At the Beginning" for 1 in English, "একলা চলো রে" in Bengali, "एक से भले दो" in Hindi).
- **Settings Dialog**: Accessible from the Main Screen to switch languages dynamically at runtime.

---

## 3. Local SQLite Database Architecture (`tambola_db.db`)

### Tables & Schemas:
1. `called_numbers`:
   - `number` (INTEGER, PRIMARY KEY)
   - `isLast` (INTEGER, 1 if last called, 0 otherwise)
2. `game_metadata`:
   - `gameId` (TEXT, PRIMARY KEY)
3. `saved_rules`:
   - `ruleId` (INTEGER, PRIMARY KEY)
   - `ruleName` (TEXT)
   - `percentage` (INTEGER)
   - `quantity` (INTEGER)
   - `amountPerItem` (INTEGER)
   - `totalRuleAmount` (INTEGER)
4. `winning_prizes`:
   - `prizeId` (INTEGER, PRIMARY KEY AUTOINCREMENT)
   - `ruleId`, `ruleName`, `percentage`, `quantity`, `amountPerItem`, `totalRuleAmount`
   - `winnerName` (TEXT, nullable)
   - `isClaimed` (INTEGER, 1 if claimed, 0 otherwise)
5. `players`:
   - `id` (INTEGER, PRIMARY KEY AUTOINCREMENT)
   - `gameId` (TEXT)
   - `name` (TEXT)

---

## 4. Screens & User Flow

### 1. Main Dashboard (`MainScreen`)
- **Responsive Layout**: Adapts automatically to TV / Wide Screens (Hero Cards + 2x2 Action Grid) and Mobile (Single Column).
- **Action Buttons**:
  - `Start New Game`: Triggers Interstitial Ad (mobile/tablet) then navigates to Game Mode Selection.
  - `Continue Last Game`: Loads existing session from SQLite DB and opens Game Board.
  - `View Winners`: Displays all recorded winner prizes.
  - `How to Play`: Interactive 5-step tutorial dialog with digital/paper ticket scenarios.
  - `Settings`: Language selection dialog.
  - `Exit`: Closes application.

### 2. Game Mode Selection (`GameModeSelectionScreen`)
- **Moderated Mode**: For games managed via the Tambola Tickets Android App (syncs player tickets).
- **Unmoderated Mode**: Host-only standalone game.
- Includes bottom AdMob Banner.

### 3. Game ID Input (`GameIdInputScreen`)
- Accepts 6-character Game ID.
- Authenticates anonymously with Firebase Auth and verifies document existence in Firestore (`games/{gameId}`).
- Includes link to download the Tambola Tickets app on Google Play.

### 4. Rule Selection (`RuleSelectionScreen`)
- **22 Tambola Rules**: Full House, Second House, Third House, Early Five, Top Line, Middle Line, Bottom Line, Corner, Diamond, Pyramid, Inverted Pyramid, Star, Odds, Even, First Half, Second Half, Breakfast, Lunch, Dinner, Temperature, Below Fifty, Above Fifty.
- **Rule Detail & Visualizer**: Interactive 3x9 ticket grid highlighting winning index patterns for each rule.

### 5. Points Distribution (`PointsDistributionScreen`)
- Adjust quantity (+ / -) for each selected rule.
- Auto-distributes percentage points (Full House allocation vs remaining pattern rules).
- Persists configured rules to SQLite database.

### 6. Game Board (`GameScreen`)
- **QR Code**: Renders scannable QR code of `gameId`.
- **Last Number Card**: Displays the current last-called number in large font.
- **Last 5 History**: Displays circles containing the last 5 called numbers.
- **Number Grid**: 1 to 90 grid; called numbers highlighted in Magenta, last called number animated/flashing in Cyan.
- **Controls**:
  - `Call Next`: Picks a random uncalled number, speaks phrase via TTS, updates DB.
  - `Auto Call`: Periodically draws a number every 6 seconds.
  - `Claim Prize`: Opens `WinnerBoardDialog`.
  - `Undo`: Confirms and rolls back the last called number.
  - `Reset`: Resets board and generates a new game ID.
  - `Return to Main Menu`: Confirms and exits to Main Screen.
- **Firestore Player Sync**: For 6-character game IDs, automatically fetches player names from Firestore (`tickets`, `players`, root document) into SQLite `players` table.

### 7. Winner Management (`WinnerBoardDialog` & `ViewWinnersScreen`)
- Manages prizes generated from selected rules x quantity.
- Edits winner names manually or selects multiple players via search & select dialog (`MultiSelectPlayerDialog`).
- Confirms before unclaiming prizes.
- **PDF Report Generation**: Exports an A4 PDF summary featuring title, date/time, Game ID, and table of prizes, winner names, and claim statuses, triggering system share sheet.

---

## 5. File Structure Reference

```
lib/
├── firebase_options.dart
├── main.dart
├── db/
│   └── app_database.dart
├── localization/
│   ├── app_localizations.dart
│   └── language_option.dart
├── models/
│   ├── called_number.dart
│   ├── game_metadata.dart
│   ├── player_entity.dart
│   ├── saved_rule_entity.dart
│   ├── tambola_rule.dart
│   └── winning_prize_entity.dart
├── screens/
│   ├── dialogs/
│   │   ├── how_to_play_dialog.dart
│   │   ├── language_settings_dialog.dart
│   │   ├── multi_select_player_dialog.dart
│   │   └── winner_board_dialog.dart
│   ├── game_id_input_screen.dart
│   ├── game_mode_selection_screen.dart
│   ├── game_screen.dart
│   ├── main_screen.dart
│   ├── points_distribution_screen.dart
│   ├── rule_selection_screen.dart
│   └── view_winners_screen.dart
└── utils/
    ├── ad_banner_widget.dart
    ├── ad_config.dart
    ├── ad_interstitial_helper.dart
    ├── funny_phrase_util.dart
    ├── general_util.dart
    ├── pdf_share_util.dart
    └── screen_size_util.dart
```

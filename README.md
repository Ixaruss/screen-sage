<div align="center">
  <img src="app/src/main/res/drawable/logo.png" alt="Screen Sage Logo" width="120" height="120">
  
  # Screen Sage
  
  **Your AI-Powered Screen Assistant**
  
  [![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com/)
  [![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org/)
  [![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
  
</div>

---

## Overview

Screen Sage is an intelligent Android accessibility app that brings AI assistance directly to your screen. With a floating chat overlay, you can get instant explanations, answers, and insights about anything on your device - all while maintaining your privacy with on-device AI processing.

## ScreenShots
<div>
<h2>home screen & history</h2>

<p >
  <img src="https://github.com/user-attachments/assets/a9418d22-93fa-48e3-b336-b44b1767e81b" height="500">
  <img src="https://github.com/user-attachments/assets/4945af23-80fd-48fb-b355-64266d8e2e1a" height="500">
</p>

</div>

<div>
<p align="center">
<h2>Widget</h2>
<img src="https://github.com/user-attachments/assets/8d7a9a8e-aa33-4cbd-b1e4-d3df6d1c495b" height="200">
</p>

</div>


## Features

### 🎯 Core Capabilities

- **Floating Chat Overlay** - Access AI assistance from anywhere on your device with a draggable, collapsible chat interface
- **On-Device AI** - Privacy-first local model (Gemma3-1B) runs entirely on your device - no internet required
- **Cloud AI Support** - Optional integration with Gemini, ChatGPT, and Claude for faster responses
- **Text Selection Assistant** - Select text in any app and get instant AI explanations
- **Conversation History** - Persistent chat sessions with automatic title generation and quick resume
- **Customizable Themes** - Choose from multiple color schemes (Pink, Blue, Purple, Green) and light/dark modes
- **Home Screen Widget** - Quick toggle for overlay activation directly from your home screen

### 🎨 Modern UI/UX (v1.2)

- **Material Design 3** - Modern segmented buttons and radio groups for intuitive settings
- **Smooth Navigation** - Swipe between tabs with fast 250ms animations
- **Animated Feedback** - Visual confirmation for save operations with loading → success states
- **Glass UI Design** - Beautiful frosted glass effects throughout the overlay interface
- **Quote of the Day** - Inspirational quotes on the home screen
- **Enhanced Empty States** - Time-based welcome messages and centered branding

### 🔒 Privacy & Performance

- **100% Private** - Local model keeps your data on your device
- **Offline Capable** - Works without internet when using local AI
- **Optimized Performance** - Background image caching and efficient model loading for fast responses
- **Low Memory Footprint** - ~1.5GB RAM usage with local model
- **Stable Input** - Debounced requests prevent crashes during rapid typing

### 🎨 User Experience

- **Intuitive Interface** - Clean, modern chat UI with markdown support
- **Smart Positioning** - Overlay snaps to screen edges and remembers position
- **Gesture Controls** - Drag to move, throw to dismiss, tap to expand, swipe to navigate
- **Accessibility First** - Built with Android accessibility services for seamless integration
- **Widget Sync** - Power button state synchronized between app and widget


## Installation

### Download

1. Download the latest APK from [Releases](https://github.com/Ixaruss/Screen-Sage/releases)
2. Enable "Install from Unknown Sources" in your device settings
3. Install the APK
4. Grant required permissions (Overlay, Accessibility)

### Build from Source

```bash
# Clone the repository
git clone https://github.com/Ixaruss/Screen-Sage.git
cd Screen-Sage

# Build the APK
./gradlew assembleDebug

# Install on connected device
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Setup

### 1. Grant Permissions

Screen Sage requires two permissions to function:

- **Display over other apps** - For the floating chat overlay

### 2. Choose AI Provider

Navigate to Settings and select your preferred AI provider:

#### Local AI (Recommended for Privacy)
- Download the Gemma3-1B model (~676MB)
- No API key required
- Works offline
- Slower but private

#### Cloud AI (Faster Responses)
- **Gemini** - Get API key from [Google AI Studio](https://makersuite.google.com/app/apikey)
- **ChatGPT** - Get API key from [OpenAI Platform](https://platform.openai.com/api-keys)
- **Claude** - Get API key from [Anthropic Console](https://console.anthropic.com/)

### 3. Start Using

1. Tap the floating icon to open the chat
2. Type your question and tap send
3. Select text in any app for instant explanations
4. Drag the overlay to reposition it


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">
  
  [Report Bug](https://github.com/Ixaruss/Screen-Sage/issues) · [Request Feature](https://github.com/Ixaruss/Screen-Sage/issues)
</div>

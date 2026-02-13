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

## Features

### 🎯 Core Capabilities

- **Floating Chat Overlay** - Access AI assistance from anywhere on your device with a draggable, collapsible chat interface
- **On-Device AI** - Privacy-first local model (Gemma3-1B) runs entirely on your device - no internet required
- **Cloud AI Support** - Optional integration with Gemini, ChatGPT, and Claude for faster responses
- **Text Selection Assistant** - Select text in any app and get instant AI explanations
- **Conversation History** - Persistent chat sessions with automatic title generation
- **Customizable Themes** - Choose from multiple color schemes (Pink, Blue, Purple, Green)

### 🔒 Privacy & Performance

- **100% Private** - Local model keeps your data on your device
- **Offline Capable** - Works without internet when using local AI
- **Optimized Performance** - Efficient model loading and caching for fast responses
- **Low Memory Footprint** - ~1.5GB RAM usage with local model

### 🎨 User Experience

- **Intuitive Interface** - Clean, modern chat UI with markdown support
- **Smart Positioning** - Overlay snaps to screen edges and remembers position
- **Gesture Controls** - Drag to move, throw to dismiss, tap to expand
- **Accessibility First** - Built with Android accessibility services for seamless integration


## Installation

### Requirements

- Android 8.0 (API 26) or higher
- 4GB RAM minimum (for local AI model)
- ~700MB storage space (for local model download)

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
- **Accessibility Service** - To detect text selection in other apps

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

## Usage

### Chat Interface

- **Expand** - Tap the floating icon
- **Move** - Drag the header or icon
- **Dismiss** - Throw the overlay off-screen or tap outside
- **Send Message** - Type and tap the send button

### Text Selection

1. Select text in any app
2. Screen Sage automatically opens with the selected text
3. Get instant AI explanations

### Conversation Management

- Conversations are automatically saved
- Tap the chat title to rename
- Access history from the main app

## Configuration

### Settings Options

- **AI Provider** - Choose between Local, Gemini, ChatGPT, or Claude
- **API Key** - Enter your cloud provider API key
- **Model Selection** - Choose specific model versions
- **System Prompt** - Customize AI behavior
- **Theme Color** - Select overlay color scheme
- **Model Management** - Download, update, or delete local model

## Technical Details

### Architecture

- **Language**: Kotlin
- **UI Framework**: Android Views with Material Design
- **AI Integration**: MediaPipe LLM Inference (local), Retrofit (cloud APIs)
- **Async Processing**: Kotlin Coroutines
- **Storage**: SharedPreferences, JSON file storage
- **Markdown Rendering**: Markwon library

### Local Model

- **Model**: Gemma3-1B (Instruction-Tuned)
- **Quantization**: 4-bit block quantization
- **Size**: 676MB
- **Max Tokens**: 512
- **Context Window**: 128 tokens
- **Inference Engine**: TensorFlow Lite with XNNPACK

### Performance Optimizations

- Model preloading on service start
- Request debouncing and mutex locks
- Conversation history limiting (last 3 messages)
- Efficient memory management

## Troubleshooting

### Common Issues

**Overlay not showing**
- Ensure "Display over other apps" permission is granted
- Restart the app

**Text selection not working**
- Enable Accessibility Service in system settings
- Grant accessibility permissions

**Local model slow**
- First response is always slower (model loading)
- Subsequent responses are faster
- Consider using cloud AI for speed

**App crashes on typing**
- Update to latest version (fixed in v1.1.0)
- Clear app cache and restart

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- **Gemma3** - Google's open-source language model
- **MediaPipe** - Google's ML framework for on-device inference
- **Markwon** - Markdown rendering library
- **Material Design** - UI/UX guidelines t

---

<div align="center">
  
  [Report Bug](https://github.com/Ixaruss/Screen-Sage/issues) · [Request Feature](https://github.com/Ixaruss/Screen-Sage/issues)
</div>

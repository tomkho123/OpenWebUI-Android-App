#!/bin/bash

# Script build APK cho Open WebUI Android App

echo "🚀 Open WebUI Android App Builder"
echo "=================================="
echo ""

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Check Android SDK
if [ -z "$ANDROID_HOME" ]; then
    if [ -d "$HOME/Android/Sdk" ]; then
        export ANDROID_HOME=$HOME/Android/Sdk
    else
        echo -e "${YELLOW}⚠️  ANDROID_HOME not found${NC}"
        echo "Please install Android Studio"
        exit 1
    fi
fi

echo -e "${GREEN}✓ Android SDK: $ANDROID_HOME${NC}"

# Check Java
if ! command -v java &> /dev/null; then
    echo -e "${YELLOW}⚠️  Java not found${NC}"
    echo "Installing JDK..."
    sudo apt update
    sudo apt install -y default-jdk
fi

echo -e "${GREEN}✓ Java: $(java -version 2>&1 | head -n 1)${NC}"

# Build APK
echo ""
echo "🔨 Building APK..."
echo ""

cd /home/ubuntu/OpenWebUI-Android-App

# Make gradlew executable
chmod +x gradlew 2>/dev/null || true

# Build debug APK
echo "Building debug APK..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✅ Build successful!${NC}"
    echo ""
    echo "📱 APK location:"
    echo "   app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "📲 Install:"
    echo "   adb install app/build/outputs/apk/debug/app-debug.apk"
    echo ""

    # Show APK size
    APK_SIZE=$(du -h app/build/outputs/apk/debug/app-debug.apk | cut -f1)
    echo "📦 APK size: $APK_SIZE"
    echo ""

    # Check if device connected
    if command -v adb &> /dev/null; then
        if adb devices | grep -q "device$"; then
            echo "📱 Device detected. Install now? (y/n)"
            read -r answer
            if [ "$answer" = "y" ]; then
                adb install app/build/outputs/apk/debug/app-debug.apk
                echo -e "${GREEN}✅ Installed!${NC}"
            fi
        else
            echo "ℹ️  No Android device detected"
            echo "   Connect device via USB and run:"
            echo "   adb install app/build/outputs/apk/debug/app-debug.apk"
        fi
    fi
else
    echo ""
    echo -e "${YELLOW}❌ Build failed${NC}"
    echo ""
    echo "Check:"
    echo "  1. Android SDK installed"
    echo "  2. Java installed"
    echo "  3. Internet connection (for dependencies)"
    exit 1
fi

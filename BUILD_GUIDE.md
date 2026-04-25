# 🚀 Build Guide cho Open WebUI Android App

## ⚠️ Lưu ý quan trọng:

**APK không thể build trực tiếp trên server này** vì thiếu Android SDK (~1GB).

## 📱 Cách build APK:

### Method 1: Dùng Android Studio (Khuyên dùng - dễ nhất)

#### Bước 1: Cài đặt Android Studio

**Trên máy tính của bạn (Windows/Mac/Linux):**

1. Download Android Studio:
   - Windows: https://developer.android.com/studio#downloads
   - Mac: https://developer.android.com/studio#downloads
   - Linux: https://developer.android.com/studio#downloads

2. Cài đặt Android Studio

3. Mở Android Studio, cài đặt Android SDK:
   - Tools → SDK Manager
   - Chọn: Android SDK Platform-Tools, Android SDK Build-Tools
   - Chọn Android 13.0 (API 33) hoặc mới hơn

#### Bước 2: Mở project

```bash
# Copy project này về máy tính của bạn
scp -r ubuntu@your-server:/home/ubuntu/OpenWebUI-Android-App ./

# Hoặc download từ GitHub nếu bạn push lên
```

#### Bước 3: Build APK

1. **Mở Android Studio**
2. **File → Open** → Chọn thư mục `OpenWebUI-Android-App`
3. **Đợi Gradle sync** (lần đầu sẽ lâu, ~5-10 phút)
4. **Build → Build Bundle(s) / APK(s) → Build APK(s)**
5. **APK sẽ ở:** `app/build/outputs/apk/debug/app-debug.apk`

#### Bước 4: Cài đặt vào điện thoại

```bash
# Cài qua USB
adb install app/build/outputs/apk/debug/app-debug.apk

# Hoặc copy APK vào điện thoại và install
```

---

### Method 2: Command line (Linux/Mac)

#### Bước 1: Cài đặt Android SDK

```bash
# Download command line tools
wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip
unzip commandlinetools-linux-9477386_latest.zip
mkdir -p ~/Android/cmdline-tools/latest
mv cmdline-tools/* ~/Android/cmdline-tools/latest/

# Set environment variables
export ANDROID_HOME=~/Android
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools

# Install SDK packages
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
```

#### Bước 2: Build APK

```bash
cd OpenWebUI-Android-App

# Update local.properties
echo "sdk.dir=$ANDROID_HOME" > local.properties

# Build
./gradlew assembleDebug
```

---

### Method 3: Dùng GitHub Actions (Tự động hóa)

Push code lên GitHub và dùng GitHub Actions để build:

#### Bước 1: Push to GitHub

```bash
cd /home/ubuntu/OpenWebUI-Android-App
git init
git add .
git commit -m "Initial commit"

# Create repo on GitHub first
git remote add origin https://github.com/YOUR_USERNAME/OpenWebUI-Android-App.git
git push -u origin main
```

#### Bước 2: Create GitHub Actions workflow

Tạo file: `.github/workflows/build.yml`

```yaml
name: Build Android APK

on:
  push:
    branches: [ main ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3

    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: gradle

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew

    - name: Build with Gradle
      run: ./gradlew assembleDebug

    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: app-debug
        path: app/build/outputs/apk/debug/app-debug.apk
```

#### Bước 3: Get APK

1. Push code lên GitHub
2. Vào **Actions** tab
3. Chọn **Build Android APK** workflow
4. Run workflow
5. Download APK từ Artifacts

---

## 📱 Sau khi có APK:

### Cài đặt:

```bash
# Via USB
adb install app-debug.apk

# Hoặc copy vào điện thoại và install (cần enable "Unknown sources")
```

### Cách dùng:

1. **Mở app**
2. **Nhập server URL**, ví dụ:
   - `http://192.168.1.100:8080`
   - `http://your-domain.com:8080`
3. **Bấm Connect**
4. **Done!**

---

## 🔧 Tìm server URL:

### Trên server:

```bash
# Tìm IP
hostname -I

# Hoặc
ip addr show | grep inet
```

### Test từ điện thoại:

```bash
# Mập trình duyệt trên điện thoại
# Nhập: http://server-ip:8080
```

---

## 🎯 Tóm tắt:

**Cách nhanh nhất:**
1. Cài Android Studio
2. Open project
3. Build APK (3 clicks)
4. Install vào phone

**Thời gian:**
- Cài Android Studio: 15 phút
- Build APK: 5 phút (lần đầu)
- Install: 1 phút

**Total: ~20 phút**

---

## 📞 Help:

### Lỗi "SDK location not found"

→ Install Android SDK via Android Studio

### Lỗi "Gradle sync failed"

→ Check internet connection, reopen project

### App không connect được server

→ Check:
- Server đang chạy: `docker ps | grep open-webui`
- Đúng IP: `ping server-ip`
- Port mở: `telnet server-ip 8080`

---

## ✅ Done!

Bạn đã có:
- ✅ Project Android hoàn chỉnh
- ✅ UI nhập server URL
- ✅ WebView tối ưu
- ✅ Hướng dẫn build chi tiết

**Bước tiếp theo:**
1. Cài Android Studio
2. Open project
3. Build APK
4. Install vào phone
5. Enjoy! 🎉

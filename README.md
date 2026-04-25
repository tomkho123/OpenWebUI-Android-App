# 📱 Open WebUI Android App

Ứng dụng Android cho Open WebUI với khả năng **nhập server URL động**.

## 🎯 Tính năng

- ✅ **Nhập server URL linh hoạt** - Không cần hardcode
- ✅ **Lưu URL** - Tiện lợi khi dùng nhiều lần
- ✅ **WebView tối ưu** - Hiệu suất tốt
- ✅ **UI đẹp** - Material Design
- ✅ **Error handling** - Thông báo lỗi rõ ràng
- ✅ **Settings dialog** - Dễ dàng thay đổi server

## 🚀 Screenshot

App có:
- **Header** với nút Settings ⚙️
- **WebView** chiếm toàn màn hình
- **Settings Dialog** để nhập URL
- **ProgressBar** khi loading

## 📱 Yêu cầu

- **Android:** 7.0 (API 24) trở lên
- **RAM:** 2GB+
- **Storage:** 10MB free
- **Mạng:** WiFi hoặc 4G

## 🔧 Cách build

### Method 1: Dùng Android Studio (Khuyên dùng)

```bash
# 1. Mở Android Studio
# File → Open → Chọn thư mục /home/ubuntu/OpenWebUI-Android-App

# 2. Đợi Gradle sync

# 3. Build APK
# Build → Build Bundle(s) / APK(s) → Build APK(s)

# 4. APK sẽ ở: app/build/outputs/apk/debug/app-debug.apk
```

### Method 2: Dùng command line

```bash
cd /home/ubuntu/OpenWebUI-Android-App

# Build debug APK
./gradlew assembleDebug

# Build release APK (cần signing)
./gradlew assembleRelease
```

## 📲 Cài đặt

```bash
# Via USB
adb install app/build/outputs/apk/debug/app-debug.apk

# Or copy APK to phone and install
```

## 🎮 Cách sử dụng

### Lần đầu mở app:

1. App sẽ hiện **Settings Dialog**
2. Nhập server URL, ví dụ:
   - `http://192.168.1.100:8080`
   - `http://your-domain.com:8080`
   - `https://openwebui.example.com`
3. Check **"Save server URL"** để lưu lại
4. Bấm **"Connect"**

### Thay đổi server:

1. Bấm nút **⚙️ Settings** ở góc trên phải
2. Nhập URL mới
3. Bấm **"Connect"**

## 🌐 Tìm server URL của bạn

### Tìm IP server:

```bash
# Trên server
hostname -I

# Hoặc
ip addr show | grep inet
```

### Test kết nối:

```bash
# Từ điện thoại
# Mập trình duyệt → nhập: http://server-ip:8080
```

### Nếu không vào được:

```bash
# Mở port 8080
sudo ufw allow 8080

# Hoặc tắt firewall tạm
sudo ufw disable
```

## 🎨 Tùy biến

### Thay đổi màu header:

```xml
<!-- app/src/main/res/values/colors.xml -->
<color name="headerBackground">#1976D2</color>
```

### Thay đổi tên app:

```xml
<!-- app/src/main/res/values/strings.xml -->
<string name="app_name">Open WebUI</string>
```

### Thay đổi icon:

```bash
# Thay icon tại:
app/src/main/res/mipmap-*/ic_launcher.png
```

## 📦 Kích thước APK

- **Debug:** ~3 MB
- **Release (signed):** ~2 MB

## 🐛 Troubleshooting

### App không load được server

**Checklist:**
1. ✅ Server đang chạy: `docker ps | grep open-webui`
2. ✅ Đúng IP: `ping <server-ip>`
3. ✅ Port mở: `telnet <server-ip> 8080`
4. ✅ URL đúng: `http://` hoặc `https://`

### Lỗi "ERR_CONNECTION_REFUSED"

```bash
# Mở port
sudo ufw allow 8080

# Hoặc
iptables -A INPUT -p tcp --dport 8080 -j ACCEPT
```

### Lỗi "ERR_CLEARTEXT_NOT_PERMITTED"

Nếu dùng HTTPS:
```xml
<!-- AndroidManifest.xml -->
android:usesCleartextTraffic="false"
```

## 🔒 Security

### Production deployment:

1. **Dùng HTTPS**
2. **Add certificate pinning**
3. **Validate server URL**
4. **Add authentication**

## 📊 Architecture

```
MainActivity
├── setupUI()              # Create UI
├── configureWebView()     # Setup WebView
├── loadServerUrl()        # Load URL
├── showSettingsDialog()   # Settings dialog
└── SharedPreferences      # Save URL
```

## 🚀 Next Steps

### Deploy lên Play Store:

1. **Signing**
   ```bash
   keytool -genkey -v -keystore my-release-key.jks \
     -keyalg RSA -keysize 2048 -validity 10000 -alias my-alias
   ```

2. **Build release APK**
   ```bash
   ./gradlew assembleRelease
   ```

3. **Upload to Play Console**

### Features có thể thêm:

- [ ] Biometric authentication
- [ ] Server list (lưu nhiều URL)
- [ ] Dark mode toggle
- [ ] Pull-to-refresh
- [ ] Offline caching
- [ ] Push notifications

## 📝 License

MIT License

## 🤝 Contributing

PRs welcome!

## 📞 Support

Nếu gặp issues:

```bash
# Check logcat
adb logcat | grep OpenWebUI

# Test server
curl http://your-server:8080
```

---

**Generated on:** $(date)
**Version:** 1.0
**Platform:** Android 7.0+

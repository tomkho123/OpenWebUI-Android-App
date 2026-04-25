# 📱 Open WebUI Android App

Ứng dụng Android cho Open WebUI với **UI nhập server URL động**.

## 🎯 Tính năng

- ✅ **Nhập server URL linh hoạt** - Không cần hardcode
- ✅ **Lưu URL** - Tiện lợi khi dùng nhiều lần
- ✅ **WebView tối ưu** - Hiệu suất tốt
- ✅ **UI đẹp** - Material Design
- ✅ **Error handling** - Thông báo lỗi rõ ràng

## 🚀 Build APK (5 phút)

### Bước 1: Push lên GitHub

```bash
# Tạo repo trên GitHub: https://github.com/new

# Push code
git remote add origin https://github.com/YOUR_USERNAME/OpenWebUI-Android-App.git
git branch -M main
git push -u origin main
```

### Bước 2: GitHub Actions build

1. Vào GitHub repo → **Actions** tab
2. Click **"Build Android APK"** workflow
3. Click **"Run workflow"**

### Bước 3: Download APK

- Đợi 3-5 phút
- Download từ **Artifacts** section

### Bước 4: Install

```bash
adb install app-debug.apk
```

## 📱 Cách dùng

1. Mở app
2. Nhập server URL: `http://192.168.1.100:8080`
3. Bấm "Connect"
4. Done! ✅

## 🌐 Tìm server URL

```bash
# Trên server
hostname -I
```

## 📦 Kích thước

- **APK:** ~3 MB
- **RAM:** 2GB+
- **Android:** 7.0+

## 📚 Files

- `MainActivity.java` - Code chính
- `AndroidManifest.xml` - Config
- `layout/*.xml` - UI design
- `.github/workflows/build.yml` - Auto build

## 🎉 Done!

App đã sẵn sàng build và sử dụng!

---

**Version:** 1.0
**Platform:** Android 7.0+
**License:** MIT

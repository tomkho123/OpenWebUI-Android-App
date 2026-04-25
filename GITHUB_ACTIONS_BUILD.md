# 🚀 Build APK với GitHub Actions

## ⚠️ Vấn đề:

Server này là **ARM architecture** và không thể build APK trực tiếp vì AAPT2 (Android build tool) chỉ hỗ trợ x86_64.

## ✅ Giải pháp: Dùng GitHub Actions (MIỄN PHÍ!)

GitHub Actions sẽ build APK cho bạn trên x86_64 runners và cho download.

---

## 📝 Các bước để build:

### Bước 1: Push code lên GitHub

```bash
cd /home/ubuntu/OpenWebUI-Android-App

# Initialize git
git init
git add .
git commit -m "Open WebUI Android App - Dynamic Server URL Input"

# Create repo on GitHub first, then:
git remote add origin https://github.com/YOUR_USERNAME/OpenWebUI-Android-App.git
git branch -M main
git push -u origin main
```

### Bước 2: Kích hoạt GitHub Actions

1. Vào repository trên GitHub
2. Click **Actions** tab
3. See workflow "Build Android APK"
4. Click **Run workflow** → **Run workflow**

### Bước 3: Download APK

1. Đợi ~3-5 phút để build
2. Vào **Actions** → Chọn workflow run mới nhất
3. Scroll xuống **Artifacts**
4. Click **app-debug** để download

### Bước 4: Cài vào điện thoại

```bash
# Via USB
adb install app-debug.apk

# Hoặc copy vào phone và install (enable "Unknown sources")
```

---

## 🎯 Hoặc: Dùng Replit/CodeSandbox (Online IDE)

Nếu không muốn dùng GitHub:

### Option A: Replit

1. Vào https://replit.com
2. Create new Repl → Import from GitHub
3. Upload project
4. Open Shell → Run build commands
5. Download APK

### Option B: Glot.io

1. Vào https://glot.io
2. Create workspace
3. Upload project
4. Run build
5. Download APK

---

## 💡 Cách nhanh nhất (5 phút):

### Dùng GitHub Actions:

```bash
# 1. Tạo GitHub repo (trên web github.com → New repository)

# 2. Push code
cd /home/ubuntu/OpenWebUI-Android-App
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/YOUR_USERNAME/OpenWebUI-Android-App.git
git push -u origin main

# 3. Vào GitHub → Actions → Run workflow

# 4. Đợi 3-5 phút

# 5. Download APK từ Artifacts

# 6. Install vào phone!
```

---

## 📱 Sau khi có APK:

### Cài đặt:

```bash
adb install app-debug.apk
```

### Cách dùng:

1. Mở app
2. Nhập server URL: `http://your-server-ip:8080`
3. Bấm "Connect"
4. Done! ✅

---

## 🎯 Tóm tắt:

**Server không thể build** (ARM architecture)
→ **Dùng GitHub Actions** (miễn phí, 3-5 phút)
→ **Download APK**
→ **Install vào phone**

**Total time: ~10 phút** (bao gồm push code)

---

## 📞 Help:

### GitHub Actions failed?

→ Check:
- GitHub token permissions
- Gradle wrapper committed
- No syntax errors in build files

### Không thể push GitHub?

→ Dùng:
- GitHub web interface (upload files)
- Or GitLab CI (similar to GitHub Actions)
- Or CodeSandbox/Replit

---

## ✅ Done!

Bạn đã có:
- ✅ GitHub Actions workflow
- ✅ Tự động build APK
- ✅ Download APK artifact
- ✅ Install vào phone

**Chỉ cần push code lên GitHub và run workflow! 🚀**

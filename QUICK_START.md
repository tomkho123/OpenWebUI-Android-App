# 🎯 Hướng dẫn build Android App cho Open WebUI

## 📁 File đã tạo:

```
/home/ubuntu/
├── COMPARISON.md                      # So sánh 3 phương pháp
├── build-android-app.sh               # Script build nhanh (Method 1)
├── OpenWebUI-Android/                 # Method 1: WebView + Remote
│   ├── app/src/main/
│   │   ├── AndroidManifest.xml
│   │   └── java/com/openwebui/app/MainActivity.java
│   ├── build.gradle
│   ├── settings.gradle
│   └── README.md
├── OpenWebUI-Android-Embedded/        # Method 2: Embedded Python
│   ├── app/src/main/
│   │   ├── python/server.py
│   │   └── java/.../MainActivityEmbedded.java
│   ├── build.gradle
│   └── README.md
└── OpenWebUI-ReactNative/             # Method 3: React Native
    └── README.md
```

---

## 🚀 Cách sử dụng:

### **Cách 1: Nhanh nhất - Dùng script (2 phút)**

```bash
# Chạy script build
cd /home/ubuntu
./build-android-app.sh

# Nhập server URL của bạn (ví dụ: http://192.168.1.100:8080)

# Mở Android Studio, build APK
# Cài đặt vào điện thoại
```

### **Cách 2: Dùng project template (10 phút)**

```bash
# 1. Mở project trên Android Studio
cd /home/ubuntu/OpenWebUI-Android

# 2. Thay đổi server URL
# Mở: app/src/main/java/com/openwebui/app/MainActivity.java
# Sửa dòng: private static final String OPEN_WEBUI_URL = "http://your-ip:8080";

# 3. Build APK
# Build → Build Bundle(s) / APK(s) → Build APK(s)

# 4. Cài đặt
adb install app/build/outputs/apk/debug/app-debug.apk
```

### **Cách 3: React Native (1-2 ngày)**

```bash
# Xem hướng dẫn ở:
cat /home/ubuntu/OpenWebUI-ReactNative/README.md
```

---

## 📊 Tóm tắt 3 phương pháp:

|  | WebView + Remote | Embedded Python | React Native |
|---|---|---|---|
| **Độ khó** | ⭐ Dễ nhất | ⭐⭐⭐ Khó | ⭐⭐ Trung bình |
| **APK Size** | 2 MB | 80-120 MB | 15-25 MB |
| **Offline** | ❌ Không | ✅ Có | ✅ Có |
| **Time** | 1-2 giờ | 2-3 ngày | 1-2 ngày |
| **Platform** | Android | Android | iOS + Android |

---

## ⚡ Nên chọn phương pháp nào?

### 👉 Chọn **Method 1** nếu:
- ✅ Bạn có server chạy 24/7
- ✅ Muốn app nhẹ, build nhanh
- ✅ Device cấu hình thấp
- ✅ Chỉ cần Android

**Đây là method khuyên dùng!**

### 👉 Chọn **Method 2** nếu:
- ✅ Cần offline hoàn toàn
- ✅ Device có RAM 4GB+
- ✅ Không muốn phụ thuộc server
- ✅ Cần full features

### 👉 Chọn **Method 3** nếu:
- ✅ Cần cả iOS và Android
- ✅ Team đã biết React
- ✅ Muốn native performance

---

## 🔥 Bắt đầu ngay:

```bash
# Bước 1: Chạy script build
cd /home/ubuntu
./build-android-app.sh

# Bước 2: Mở Android Studio
# File → Open → Chọn thư mục được tạo

# Bước 3: Build APK
# Build → Build Bundle(s) / APK(s) → Build APK(s)

# Bước 4: Cài vào điện thoại
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 📱 Cấu hình điện thoại:

### Yêu cầu tối thiểu:
- **Android:** 7.0 (API 24) trở lên
- **RAM:** 2GB+
- **Storage:** 50MB free

### Kiểm tra server có accessible từ điện thoại:

```bash
# 1. Tìm IP của server
hostname -I

# 2. Test từ điện thoại
# Mập trình duyệt trên điện thoại, nhập:
# http://server-ip:8080

# 3. Nếu không vào được:
#    - Kiểm tra firewall: sudo ufw allow 8080
#    - Đảm bảo server và điện thoại cùng mạng WiFi
```

---

## 🐛 Troubleshooting:

### App không load được server?

1. **Kiểm tra server đang chạy:**
   ```bash
   docker ps | grep open-webui
   ```

2. **Test từ máy tính:**
   ```bash
   curl http://localhost:8080
   ```

3. **Tìm IP đúng:**
   ```bash
   ip addr show | grep inet
   ```

4. **Test từ điện thoại:**
   - Mở trình duyệt trên điện thoại
   - Nhập: `http://your-server-ip:8080`
   - Nếu không được → kiểm tra firewall

### Lỗi "ERR_CONNECTION_REFUSED"?

```bash
# Mở port 8080
sudo ufw allow 8080

# Hoặc tắt firewall tạm thời
sudo ufw disable
```

### Lỗi "NET::ERR_CLEARTEXT_NOT_PERMITTED"?

```xml
<!-- Thêm vào AndroidManifest.xml -->
android:usesCleartextTraffic="true"
```

---

## 📚 Tài liệu tham khảo:

- **So sánh chi tiết:** `/home/ubuntu/COMPARISON.md`
- **Method 1 README:** `/home/ubuntu/OpenWebUI-Android/README.md`
- **Method 2 README:** `/home/ubuntu/OpenWebUI-Android-Embedded/README.md`
- **Method 3 README:** `/home/ubuntu/OpenWebUI-ReactNative/README.md`

---

## 🎉 Hoàn tất!

Bạn đã có mọi thứ cần thiết để build Android app cho Open WebUI.

**Khuyến nghị:** Bắt đầu với **Method 1** (WebView + Remote) vì nó đơn giản nhất!

Nếu cần help, check logcat:
```bash
adb logcat | grep OpenWebUI
```

Good luck! 🚀

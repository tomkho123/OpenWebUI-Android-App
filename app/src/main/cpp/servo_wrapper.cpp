#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "ServoView"

extern "C" {
    // Rust functions (will be linked from Servo library)
    extern int rust_init();
    extern void rust_load_url(const char* url);
    extern void rust_go_back();
    extern void rust_reload();
    extern void rust_cleanup();
}

extern "C" JNIEXPORT void JNICALL
Java_com_openwebui_app_ServoView_nativeInit(
    JNIEnv* env,
    jobject thiz
) {
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Native init called");
    rust_init();
}

extern "C" JNIEXPORT void JNICALL
Java_com_openwebui_app_ServoView_nativeLoadUrl(
    JNIEnv* env,
    jobject thiz,
    jstring url
) {
    if (url == nullptr) {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "URL is null");
        return;
    }

    const char *urlStr = env->GetStringUTFChars(url, 0);
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Loading URL: %s", urlStr);

    rust_load_url(urlStr);

    env->ReleaseStringUTFChars(url, urlStr);
}

extern "C" JNIEXPORT void JNICALL
Java_com_openwebui_app_ServoView_nativeGoBack(
    JNIEnv* env,
    jobject thiz
) {
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Go back called");
    rust_go_back();
}

extern "C" JNIEXPORT void JNICALL
Java_com_openwebui_app_ServoView_nativeReload(
    JNIEnv* env,
    jobject thiz
) {
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Reload called");
    rust_reload();
}

extern "C" JNIEXPORT void JNICALL
Java_com_openwebui_app_ServoView_nativeCleanup(
    JNIEnv* env,
    jobject thiz
) {
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Cleanup called");
    rust_cleanup();
}

extern "C" JNIEXPORT void JNICALL
Java_com_openwebui_app_ServoView_onSurfaceChanged(
    JNIEnv* env,
    jobject thiz,
    jint width,
    jint height
) {
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Surface changed: %dx%d", width, height);
    // TODO: Notify Servo of surface size change
}

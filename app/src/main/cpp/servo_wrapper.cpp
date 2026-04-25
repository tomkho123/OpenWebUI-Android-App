#include <jni.h>
#include <string>
#include <android/log.h>
#include <android/log.h>

#define LOG_TAG "ServoView"

// Rust functions declared in servo library
extern "C" {
    extern int rust_init();
    extern void rust_load_url(const char* url);
    extern void rust_go_back();
    extern void rust_reload();
    extern void rust_cleanup();
    extern void rust_set_surface(long surface_ptr, int width, int height);
    extern int rust_handle_touch_event(int action, float x, float y);
    extern const char* rust_get_url();
}

// Get native surface pointer using JNI
static jlong getSurfacePointer(JNIEnv* env, jobject surfaceObject) {
    jclass surfaceClass = env->FindClass("android/view/Surface", NULL);
    if (surfaceClass == NULL) {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Could not find Surface class");
        return 0;
    }

    jmethodID getHandleMethod = env->GetMethodID(surfaceClass, "getNativeHandle", "()J");
    if (getHandleMethod == NULL) {
        __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, "Could not find getNativeHandle method");
        return 0;
    }

    jlong handle = env->CallLongMethodA(surfaceObject, getHandleMethod);
    return handle;
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
Java_com_openwebui_app_ServoView_nativeSetSurface(
    JNIEnv* env,
    jobject thiz,
    jlong surfacePtr,
    jint width,
    jint height
) {
    __android_log_print(ANDROID_LOG_INFO, LOG_TAG, "Set surface: ptr=%ld, size=%dx%d", surfacePtr, width, height);
    rust_set_surface((long)surfacePtr, (int)width, (int)height);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_openwebui_app_ServoView_nativeHandleTouchEvent(
    JNIEnv* env,
    jobject thiz,
    jint action,
    jfloat x,
    jfloat y
) {
    __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, "Touch: action=%d, x=%.2f, y=%.2f", action, x, y);
    return (jboolean)rust_handle_touch_event((int)action, (float)x, (float)y);
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_openwebui_app_ServoView_nativeGetUrl(
    JNIEnv* env,
    jobject thiz
) {
    const char* url = rust_get_url();
    jstring result = env->NewStringUTF(url);
    return result;
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_openwebui_app_ServoView_getSurfacePointer(
    JNIEnv* env,
    jobject thiz,
    jobject surfaceObject
) {
    return getSurfacePointer(env, surfaceObject);
}

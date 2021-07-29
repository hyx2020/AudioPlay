#include "audio/AudioOboeEngine.hpp"

#define LOG_TAG "Native"
#define LOGD_MINE(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

extern "C"
JNIEXPORT void JNICALL
Java_org_hyx_lib_1play_lib_JniLib_detection(JNIEnv *env, jobject thiz) {
    LOGD_MINE("transfer success");
}extern "C"

JNIEXPORT jboolean JNICALL
Java_org_hyx_lib_1play_lib_AudioEngine_isAAudioRecommended(JNIEnv *env, jobject thiz) {
    // TODO: implement isAAudioRecommended()
    return false;
}
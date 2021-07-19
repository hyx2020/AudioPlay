#include <jni.h>
#include <string>


#include <android/log.h>
#define LOG_TAG "DISTANCE_ESTIMATE"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)


extern "C" JNIEXPORT void JNICALL
Java_org_hyx_lib_1play_JniLib_detection(
        JNIEnv* env,
        jobject /* this */) {
    LOGD("transfer success");
}

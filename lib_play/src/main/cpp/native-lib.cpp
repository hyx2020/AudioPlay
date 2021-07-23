#include "audio/audioRecord.hpp"
#include "audio/audioTrack.hpp"

#define LOG_TAG "Native"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT void JNICALL
Java_org_hyx_lib_1play_JniLib_detection(
        JNIEnv* env,
        jobject /* this */) {
    LOGD("transfer success");
}

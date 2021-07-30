#include <jni.h>
#include <string>
#include <android/log.h>
#include <logging_macros.h>

#include "audio/LiveEffectEngine.h"

#define LOG_TAG "JniBridge"
#define LOGD_MINE(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

static const int kOboeApiAAudio = 1;
static const int kOboeApiOpenSLES = 0;

static LiveEffectEngine *engine = nullptr;

extern "C"
JNIEXPORT void JNICALL
Java_org_hyx_lib_1play_lib_JniLib_detection(JNIEnv *env, jobject thiz) {
    LOGD_MINE("transfer success");
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_org_hyx_lib_1play_lib_AudioEngine_create(JNIEnv *env, jobject thiz) {
    if (engine == nullptr) {
        engine = new LiveEffectEngine();
    }
    return (engine != nullptr) ? JNI_TRUE : JNI_FALSE;
}


extern "C"
JNIEXPORT void JNICALL
Java_org_hyx_lib_1play_lib_AudioEngine_delete(JNIEnv *env, jobject thiz) {
    if (engine) {
        engine->setEffectOn(false);
        delete engine;
        engine = nullptr;
    }
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_org_hyx_lib_1play_lib_AudioEngine_setEffectOn(JNIEnv *env, jobject thiz, jboolean is_effect_on) {
    if (engine == nullptr) {
        LOGE("Engine is null, you must call createEngine before calling this method");
        return JNI_FALSE;
    }
    return engine->setEffectOn(is_effect_on) ? JNI_TRUE : JNI_FALSE;
}

extern "C"
JNIEXPORT void JNICALL
Java_org_hyx_lib_1play_lib_AudioEngine_setRecordingDeviceId(JNIEnv *env, jobject thiz, jint device_id) {
    if (engine == nullptr) {
        LOGE("Engine is null, you must call createEngine before calling this method");
        return;
    }
    engine->setRecordingDeviceId(device_id);
}

extern "C"
JNIEXPORT void JNICALL
Java_org_hyx_lib_1play_lib_AudioEngine_setPlaybackDeviceId(JNIEnv *env, jobject thiz, jint device_id) {
    if (engine == nullptr) {
        LOGE("Engine is null, you must call createEngine before calling this method");
        return;
    }
    engine->setPlaybackDeviceId(device_id);
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_org_hyx_lib_1play_lib_AudioEngine_setAPI(JNIEnv *env, jobject thiz, jint api_type) {
    if (engine == nullptr) {
        LOGE("Engine is null, you must call createEngine before calling this method");
        return JNI_FALSE;
    }
    oboe::AudioApi audioApi;
    switch (api_type) {
        case kOboeApiAAudio:
            if (engine->isAAudioRecommended()) {
                audioApi = oboe::AudioApi::AAudio;
                break;
            }
        case kOboeApiOpenSLES:
        default:
            audioApi = oboe::AudioApi::OpenSLES;
            LOGE("Default API selection to setAPI() %d", api_type);
    }
    return engine->setAudioApi(audioApi) ? JNI_TRUE : JNI_FALSE;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_org_hyx_lib_1play_lib_AudioEngine_isAAudioRecommended(JNIEnv *env, jobject thiz) {
    if (engine == nullptr) {
        LOGE("Engine is null, you must call createEngine before calling this method");
        return JNI_FALSE;
    }
    return engine->isAAudioRecommended() ? JNI_TRUE : JNI_FALSE;
}

extern "C"
JNIEXPORT void JNICALL
Java_org_hyx_lib_1play_lib_AudioEngine_native_1setDefaultStreamValues(JNIEnv *env, jobject thiz, jint default_sample_rate, jint default_frames_per_burst) {
    oboe::DefaultStreamValues::SampleRate = (int32_t) default_sample_rate;
    oboe::DefaultStreamValues::FramesPerBurst = (int32_t) default_frames_per_burst;
}
extern "C"
JNIEXPORT void JNICALL
Java_org_hyx_lib_1play_lib_AudioEngine_setChannelInput(JNIEnv *env, jobject thiz, jint channel) {
    if (engine == nullptr) {
        LOGE("Engine is null, you must call createEngine before calling this method");
        return;
    }
    engine->setInputChannelCount(channel);
}
#include "audio/AudioOboeEngine.hpp"

#define LOG_TAG "Native"
#define LOGD_MINE(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

extern "C"
JNIEXPORT void JNICALL
Java_org_hyx_lib_1play_lib_JniLib_detection(JNIEnv *env, jobject thiz) {
    LOGD_MINE("transfer success");
}extern "C"

////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*audio引擎创建、打开、关闭、删除操作*/
////////////////////////////////////////////////////////////////////////////////////////////////////////////
JNIEXPORT jlong JNICALL
Java_org_hyx_lib_1play_lib_AudioEngine_native_1createEngine(JNIEnv *env, jobject thiz) {
    // We use std::nothrow so `new` returns a nullptr if the engine creation fails
    auto *engine = new(std::nothrow) AudioOboeEngine();
    if (engine == nullptr) {
        LOGE("Could not instantiate HelloOboeEngine");
        return 0;
    }
    return reinterpret_cast<jlong>(engine);
}extern "C"

JNIEXPORT jint JNICALL
Java_org_hyx_lib_1play_lib_AudioEngine_native_1startEngine(JNIEnv *env, jobject thiz,
                                                           jlong engine_handle) {
    auto *engine = reinterpret_cast<AudioOboeEngine *>(engine_handle);
    return static_cast<jint>(engine->start());
}extern "C"

JNIEXPORT jint JNICALL
Java_org_hyx_lib_1play_lib_AudioEngine_native_1stopEngine(JNIEnv *env, jobject thiz,
                                                          jlong engine_handle) {
    auto *engine = reinterpret_cast<AudioOboeEngine *>(engine_handle);
    return static_cast<jint>(engine->stop());
}extern "C"

JNIEXPORT void JNICALL
Java_org_hyx_lib_1play_lib_AudioEngine_native_1deleteEngine(JNIEnv *env, jobject thiz,
                                                            jlong engine_handle) {
    auto *engine = reinterpret_cast<AudioOboeEngine *>(engine_handle);
    engine->stop();
    delete engine;
}extern "C"

JNIEXPORT void JNICALL
Java_org_hyx_lib_1play_lib_AudioEngine_native_1setDefaultStreamValues(JNIEnv *env, jobject thiz,
                                                                      jint sample_rate,
                                                                      jint frames_per_burst) {
    oboe::DefaultStreamValues::SampleRate = (int32_t) sample_rate;
    oboe::DefaultStreamValues::FramesPerBurst = (int32_t) frames_per_burst;
}extern "C"

JNIEXPORT void JNICALL
Java_org_hyx_lib_1play_lib_AudioEngine_native_1setAudioApi(JNIEnv *env, jobject thiz,
                                                           jlong engine_handle, jint audio_api) {
    auto *engine = reinterpret_cast<AudioOboeEngine *>(engine_handle);
    if (engine == nullptr) {
        LOGE("Engine handle is invalid, call createHandle() to create a new one");
        return;
    }

    auto api = static_cast<oboe::AudioApi>(audio_api);
    engine->setAudioApi(api);}
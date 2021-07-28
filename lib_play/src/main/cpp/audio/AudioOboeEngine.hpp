//
// Created by hyx on 2021/7/22.
//

#ifndef AUDIOPLAY_AUDIOOBOEENGINE_HPP
#define AUDIOPLAY_AUDIOOBOEENGINE_HPP

#include <jni.h>
#include <string>
#include <android/log.h>

#include <oboe/Oboe.h>
#include "IRestartable.h"
#include "DefaultErrorCallback.h"

#define LOG_RECORD "Record"
#define LOGD_RECORD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_RECORD, __VA_ARGS__)

class AudioOboeEngine: public IRestartable {
public:
    AudioOboeEngine();

    virtual ~AudioOboeEngine() = default;

    oboe::Result start();

    oboe::Result stop();

    void restart() override;

    void setDeviceId(int32_t deviceId);

    void setChannelCount(int channelCount);

    void setAudioApi(oboe::AudioApi audioApi);

    void setBufferSizeInBursts(int32_t numBursts);
private:
    oboe::Result reopenSteam();
    oboe::Result openPlaybackStream();

    std::shared_ptr<oboe::AudioStream> mStream;
};

#endif //AUDIOPLAY_AUDIOOBOEENGINE_HPP

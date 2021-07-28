//
// Created by hyx on 2021/7/22.
//

#include "AudioOboeEngine.hpp"

AudioOboeEngine::AudioOboeEngine(){
}

void AudioOboeEngine::restart() {

}

oboe::Result AudioOboeEngine::start() {
    return oboe::Result::ErrorInternal;
}

oboe::Result AudioOboeEngine::stop() {
    return oboe::Result::ErrorInternal;
}

oboe::Result AudioOboeEngine::reopenSteam() {
    return oboe::Result::ErrorInternal;
}

oboe::Result AudioOboeEngine::openPlaybackStream() {
    return oboe::Result::ErrorInternal;
}

void AudioOboeEngine::setAudioApi(oboe::AudioApi audioApi) {

}

void AudioOboeEngine::setBufferSizeInBursts(int32_t numBursts) {

}

void AudioOboeEngine::setChannelCount(int channelCount) {

}

void AudioOboeEngine::setDeviceId(int32_t deviceId) {

}


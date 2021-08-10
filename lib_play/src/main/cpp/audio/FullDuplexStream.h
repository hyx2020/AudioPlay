/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#ifndef OBOE_FULL_DUPLEX_STREAM_H
#define OBOE_FULL_DUPLEX_STREAM_H

#include <unistd.h>
#include <sys/types.h>
#include <jni.h>
#include <android/asset_manager.h>

#include "oboe/Oboe.h"
#include "AAssetDataSource.h"

class FullDuplexStream : public oboe::AudioStreamCallback {
public:
    FullDuplexStream(){}

    virtual ~FullDuplexStream() = default;

    void setInputStream(std::shared_ptr<oboe::AudioStream> stream) {
        mInputStream = stream;
    }

    void setOutputStream(std::shared_ptr<oboe::AudioStream> stream) {
        mOutputStream = stream;
    }

    void setAudioMp3(std::shared_ptr<AAssetDataSource> &mp3);

    virtual oboe::Result start();

    virtual oboe::Result stop();

    /**
     * Called when data is available on both streams.
     * App should override this method.
     */
    virtual oboe::DataCallbackResult onBothStreamsReady(
            std::shared_ptr<oboe::AudioStream> inputStream,
            const void *inputData,
            int numInputFrames,
            std::shared_ptr<oboe::AudioStream> outputStream,
            void *outputData,
            int numOutputFrames
    ) = 0;

    /**
     * Called by Oboe when the stream is ready to process audio.
     * This implements the stream synchronization. App should NOT override this method.
     */
    oboe::DataCallbackResult onAudioReady(
            oboe::AudioStream *audioStream,
            void *audioData,
            int numFrames) override;

    int32_t getNumInputBurstsCushion() const;

    /**
     * Number of bursts to leave in the input buffer as a cushion.
     * Typically 0 for latency measurements
     * or 1 for glitch tests.
     *
     * @param mNumInputBurstsCushion
     */
    void setNumInputBurstsCushion(int32_t numInputBurstsCushion);

    jfloatArray getAudioData(JNIEnv *env);

    void getAudioDeal(int numFrames, int32_t numBytes);

    void playAudioLoop(void *outputData, int numOutputFrames);

    void setPlayFlag(bool flag);

    void loopAudio(void *audio, int size);
private:
    bool isPlay = false;

    // TODO add getters and setters
    static constexpr int32_t kNumCallbacksToDrain = 20;
    static constexpr int32_t kNumCallbacksToDiscard = 30;

    // let input fill back up, usually 0 or 1
    int32_t mNumInputBurstsCushion = 1;

    // We want to reach a state where the input buffer is empty and
    // the output buffer is full.
    // These are used in order.
    // Drain several callback so that input is empty.
    int32_t mCountCallbacksToDrain = kNumCallbacksToDrain;
    // Let the input fill back up slightly so we don't run dry.
    int32_t mCountInputBurstsCushion = mNumInputBurstsCushion;
    // Discard some callbacks so the input and output reach equilibrium.
    int32_t mCountCallbacksToDiscard = kNumCallbacksToDiscard;

    std::shared_ptr<oboe::AudioStream> mInputStream;
    std::shared_ptr<oboe::AudioStream> mOutputStream;
    std::shared_ptr<AAssetDataSource>  mp3;

    int32_t mBufferSize = 0;
    std::unique_ptr<float[]> mInputBuffer;
    const int maxMemory = 72000;
    std::unique_ptr<float[]> mOutputBuffer = std::make_unique<float[]>(maxMemory);  //输出录音音频数据
    std::unique_ptr<float[]> mWriteBuffer = std::make_unique<float[]>(maxMemory);  //写入音频数据
    int32_t indexOutputBuffer = 0;
    int32_t indexWriteBuffer = 0;
    int32_t loadReadSize = 0;
    int32_t loadPlaySize = 0;
    std::mutex lock;
};


#endif //OBOE_FULL_DUPLEX_STREAM_H

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

#include "FullDuplexStream.h"

#include <utility>
#include "../../../../../../oboe/src/common/OboeDebug.h"

oboe::DataCallbackResult FullDuplexStream::onAudioReady(
        oboe::AudioStream *outputStream,
        void *audioData,
        int numFrames) {

    oboe::DataCallbackResult callbackResult = oboe::DataCallbackResult::Continue;
    int32_t actualFramesRead = 0;

    // Silence the output.
    int32_t numBytes = numFrames * outputStream->getBytesPerFrame();
    memset(audioData, 0 /* value */, numBytes);

    if (mCountCallbacksToDrain > 0) {
        // Drain the input.
        int32_t totalFramesRead = 0;
        do {
            oboe::ResultWithValue<int32_t> result = mInputStream->read(mInputBuffer.get(),
                                                                       numFrames,
                                                                       0 /* timeout */);
            if (!result) {
                // Ignore errors because input stream may not be started yet.
                break;
            }
            actualFramesRead = result.value();
            totalFramesRead += actualFramesRead;
        } while (actualFramesRead > 0);
        // Only counts if we actually got some data.
        if (totalFramesRead > 0) {
            mCountCallbacksToDrain--;
        }

    } else if (mCountInputBurstsCushion > 0) {
        // Let the input fill up a bit so we are not so close to the write pointer.
        mCountInputBurstsCushion--;

    } else if (mCountCallbacksToDiscard > 0) {
        // Ignore. Allow the input to reach to equilibrium with the output.
        oboe::ResultWithValue<int32_t> result = mInputStream->read(mInputBuffer.get(),
                                                                   numFrames,
                                                                   0 /* timeout */);
        if (!result) {
            callbackResult = oboe::DataCallbackResult::Stop;
        }
        mCountCallbacksToDiscard--;

    } else {
        // Read data into input buffer.
        oboe::ResultWithValue<int32_t> result = mInputStream->read(mInputBuffer.get(),
                                                                   numFrames,
                                                                   0 /* timeout */);
        if (!result) {
            callbackResult = oboe::DataCallbackResult::Stop;
        } else {
            callbackResult = oboe::DataCallbackResult::Continue;
        }

        if (isPlay) {
            playAudioLoop(audioData, numFrames);
        }
    }

    if (callbackResult == oboe::DataCallbackResult::Stop) {
        mInputStream->requestStop();
        return callbackResult;
    }

    lock.lock();
    getAudioDeal(numFrames, numBytes);
    lock.unlock();

    return callbackResult;
}

oboe::Result FullDuplexStream::start() {
    mCountCallbacksToDrain = kNumCallbacksToDrain;
    mCountInputBurstsCushion = mNumInputBurstsCushion;
    mCountCallbacksToDiscard = kNumCallbacksToDiscard;

    // Determine maximum size that could possibly be called.
    int32_t bufferSize = mOutputStream->getBufferCapacityInFrames()
                         * mOutputStream->getChannelCount();
    if (bufferSize > mBufferSize) {
        mInputBuffer = std::make_unique<float[]>(bufferSize);
        mBufferSize = bufferSize;
    }
    oboe::Result result = mInputStream->requestStart();
    if (result != oboe::Result::OK) {
        return result;
    }
    return mOutputStream->requestStart();
}

oboe::Result FullDuplexStream::stop() {
    isPlay = false;
    oboe::Result outputResult = oboe::Result::OK;
    oboe::Result inputResult = oboe::Result::OK;
    if (mOutputStream) {
        outputResult = mOutputStream->requestStop();
    }
    if (mInputStream) {
        inputResult = mInputStream->requestStop();
    }
    if (outputResult != oboe::Result::OK) {
        return outputResult;
    } else {
        return inputResult;
    }
}

int32_t FullDuplexStream::getNumInputBurstsCushion() const {
    return mNumInputBurstsCushion;
}

void FullDuplexStream::setNumInputBurstsCushion(int32_t numBursts) {
    FullDuplexStream::mNumInputBurstsCushion = numBursts;
}

jfloatArray FullDuplexStream::getAudioData(JNIEnv *env) {
    lock.lock();
    jfloatArray res = env->NewFloatArray(indexOutputBuffer);
    env->SetFloatArrayRegion(res, 0, indexOutputBuffer, mOutputBuffer.get());
    memset(mOutputBuffer.get(), 0, indexOutputBuffer);
    indexOutputBuffer = 0;
    LOGE("loadReadSize: %d", loadReadSize);
    loadReadSize = 0;
    lock.unlock();
    return res;
}

void FullDuplexStream::setPlayFlag(bool flag) {
    isPlay = flag;
}

void FullDuplexStream::loopAudio(void *audio, int size) {
    mWriteBuffer = std::make_unique<float[]>(size);
    memcpy(mWriteBuffer.get(), (float *)audio, size);
    loadPlaySize = size;
    indexWriteBuffer = 0;
}

void FullDuplexStream::getAudioDeal(int numFrames, int32_t numBytes) {
    if ((indexOutputBuffer + numFrames) > maxMemory) {
        memset(mOutputBuffer.get(), 0, indexOutputBuffer);
        indexOutputBuffer = 0;
    }
    memcpy(mOutputBuffer.get() + indexOutputBuffer, mInputBuffer.get(), numBytes);
    indexOutputBuffer += numFrames;
    loadReadSize += numFrames;
}

void FullDuplexStream::playAudioLoop(void *outputData,
                                     int numOutputFrames) {

    int size = mOutputStream->getChannelCount() * numOutputFrames;
    const float *inputFloats = mWriteBuffer.get() + indexWriteBuffer;
    auto *outputFloats = static_cast<float *>(outputData);
    for (int32_t i = 0; i < size; i++) {
        if(i < loadPlaySize) {
            *outputFloats++ = *inputFloats++;
        } else {
            *outputFloats++ = 0.0;
        }
    }

    LOGD("------- %d, %d, %d, %f", indexWriteBuffer, size, loadPlaySize, mWriteBuffer.get()[0]);

    if(size < loadPlaySize) {
        indexWriteBuffer = (indexWriteBuffer + size) % loadPlaySize;
    } else {
        indexWriteBuffer = 0;
    }
}

void FullDuplexStream::setAudioMp3(std::shared_ptr<AAssetDataSource> &audio) {
    FullDuplexStream::mp3 = audio;
    loopAudio((void *) mp3->getData(), mp3->getSize());
}

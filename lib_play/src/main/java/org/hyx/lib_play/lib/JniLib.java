package org.hyx.lib_play.lib;

import kotlin.jvm.JvmStatic;

public class JniLib {
    private static final JniLib self;

    static {
        System.loadLibrary("audio-lib-hyx");
        self = new JniLib();
    }

    ////////////////////////////////////////////////////////////

    public static void static_detection() {
        self.detection();
    }

    ////////////////////////////////////////////////////////////

    @JvmStatic
    private native void detection();
}
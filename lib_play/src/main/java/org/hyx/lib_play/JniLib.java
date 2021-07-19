package org.hyx.lib_play;

import kotlin.jvm.JvmStatic;

public class JniLib {
    private static JniLib self;

    static {
        System.loadLibrary("native-lib");
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
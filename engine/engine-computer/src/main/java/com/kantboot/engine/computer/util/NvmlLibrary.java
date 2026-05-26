package com.kantboot.engine.computer.util;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public interface NvmlLibrary extends Library {
    NvmlLibrary INSTANCE = Native.load("nvml", NvmlLibrary.class);

    int nvmlInit();
    int nvmlDeviceGetCount(IntByReference deviceCount);
    int nvmlDeviceGetHandleByIndex(int index, Pointer device);
    int nvmlDeviceGetName(Pointer device, byte[] name, int length);
    int nvmlDeviceGetTemperature(Pointer device, int sensorType, IntByReference temp);
    int nvmlDeviceGetFanSpeed(Pointer device, IntByReference speed);
    int nvmlDeviceGetUtilizationRates(Pointer device, NvmlUtilization utilization);
    int nvmlDeviceGetMemoryInfo(Pointer device, NvmlMemory memory);
    int nvmlShutdown();
}

package com.example;

import uk.ac.manchester.tornado.api.TaskGraph;
import uk.ac.manchester.tornado.api.TornadoDriver;
import uk.ac.manchester.tornado.api.TornadoRuntimeInterface;
import uk.ac.manchester.tornado.api.annotations.Parallel;
import uk.ac.manchester.tornado.api.collections.types.Matrix2DDouble;
import uk.ac.manchester.tornado.api.common.TornadoDevice;
import uk.ac.manchester.tornado.api.runtime.TornadoRuntime;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello and welcome!");

        TornadoRuntimeInterface tornadoRuntime = TornadoRuntime.getTornadoRuntime();
        TornadoDevice tornadoDevice = tornadoRuntime.getDefaultDevice();
        System.out.println("Device name: " + tornadoDevice.getDeviceName());
        System.out.println("Device description: ");
        System.out.println(tornadoDevice.getDescription());
    }
}
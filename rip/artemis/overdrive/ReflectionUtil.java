package rip.artemis.overdrive;

import rip.artemis.overdrive.api.NMSHandler;

import java.lang.reflect.InvocationTargetException;

public final class ReflectionUtil {

    public static NMSHandler getNewNMSHandler() {
        try {
            Class<?> clazz = Class.forName("rip.artemis.overdrive.handler");
            return (NMSHandler) clazz.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}

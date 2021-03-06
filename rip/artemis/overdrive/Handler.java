package rip.artemis.overdrive;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import static org.bukkit.Bukkit.getServer;

import net.minecraft.server.v1_16_R2.MinecraftServer;
import net.minecraft.server.v1_16_R2.SystemUtils;

public class Handler implements NMSHandler {
    private static Field nextTick;

    static {
        try {
            nextTick = MinecraftServer.class.getDeclaredField("nextTick");
            nextTick.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
    }

    protected static long tickLength = 50L;

    public Handler() {
        SystemUtils.a = this::nanoTime;
    }

    @Override
    public long getTickDuration() {
        return tickLength;
    }

    @Override
    public void setTickDuration(long duration) {
        tickLength = duration;
    }

    private long nanoTime() {
        if (needsUpdate()) {
            long time = System.nanoTime() + (TimeUnit.MILLISECONDS.toNanos(tickLength - 50L));

            try {
                long curTick = nextTick.getLong(getServer());

                curTick += (tickLength - 50L);

                nextTick.set(getServer(), curTick);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }

            return time;
        } else {
            return System.nanoTime();
        }
    }

    private boolean needsUpdate() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		for (StackTraceElement element : elements) {
			int lineNumber = element.getLineNumber();
			if ((lineNumber == 849) && element.getClassName().equalsIgnoreCase(MinecraftServer.class.getName())) {
				return true;
			}
		}
        return false;
    }
}

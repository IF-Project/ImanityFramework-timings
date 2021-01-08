package org.imanity.framework.timings;

import org.bukkit.plugin.Plugin;
import org.imanity.framework.PreInitialize;
import org.imanity.framework.Service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Service(name = "timings")
public class TimingService {

    private TimingType timingType;
    private final Map<String, MCTiming> timingCache = new HashMap<>(0);

    @PreInitialize
    public void preInit() {
        if (timingType == null) {
            try {
                Class<?> clazz = Class.forName("co.aikar.timings.Timing");
                Method startTiming = clazz.getMethod("startTiming");
                if (startTiming.getReturnType() != clazz) {
                    timingType = TimingType.MINECRAFT_18;
                } else {
                    timingType = TimingType.MINECRAFT;
                }
            } catch (ClassNotFoundException | NoSuchMethodException ignored1) {
                try {
                    Class.forName("org.spigotmc.CustomTimingsHandler");
                    timingType = TimingType.SPIGOT;
                } catch (ClassNotFoundException ignored2) {
                    timingType = TimingType.EMPTY;
                }
            }
        }
    }

    @SuppressWarnings("WeakerAccess")
    public MCTiming ofStart(Plugin plugin, String name) {
        return ofStart(plugin, name, null);
    }

    @SuppressWarnings("WeakerAccess")
    public MCTiming ofStart(Plugin plugin, String name, MCTiming parent) {
        return of(plugin, name, parent).startTiming();
    }

    @SuppressWarnings("WeakerAccess")
    public MCTiming of(Plugin plugin, String name) {
        return of(plugin, name, null);
    }

    @SuppressWarnings("WeakerAccess")
    public MCTiming of(Plugin plugin, String name, MCTiming parent) {

        MCTiming timing;
        if (timingType.useCache()) {
            synchronized (timingCache) {
                String lowerKey = name.toLowerCase();
                timing = timingCache.get(lowerKey);
                if (timing == null) {
                    timing = timingType.newTiming(plugin, name, parent);
                    timingCache.put(lowerKey, timing);
                }
            }
            return timing;
        }

        return timingType.newTiming(plugin, name, parent);
    }
}

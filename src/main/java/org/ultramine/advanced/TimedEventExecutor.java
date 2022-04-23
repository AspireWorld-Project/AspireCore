package org.ultramine.advanced;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

public class TimedEventExecutor implements EventExecutor {

    private final EventExecutor executor;

    /**
     * Wraps an event executor and associates a timing handler to it.
     *
     * @param executor Executor to wrap
     * @param plugin Owning plugin
     * @param method EventHandler method
     * @param eventClass Owning class
     */
    public TimedEventExecutor(EventExecutor executor, Plugin plugin, @Nullable Method method, Class<? extends Event> eventClass) {
        this.executor = executor;

        if (method == null) {
            if (executor.getClass().getEnclosingClass() != null) { // Oh Skript, how we love you
                method = executor.getClass().getEnclosingMethod();
            }
        }


    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {
        if (event.isAsynchronous() || !Bukkit.isPrimaryThread()) {
            executor.execute(listener, event);
        }
    }
}

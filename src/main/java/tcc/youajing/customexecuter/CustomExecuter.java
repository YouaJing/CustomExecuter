package tcc.youajing.customexecuter;

import crypticlib.BukkitPlugin;
import org.bukkit.event.Listener;

public class CustomExecuter extends BukkitPlugin {

    @Override
    public void enable() {
        //TODO
        Listener CEListener = new CEListener(this);
        getServer().getPluginManager().registerEvents(CEListener, this);
    }

    @Override
    public void disable() {
        //TODO
    }

}
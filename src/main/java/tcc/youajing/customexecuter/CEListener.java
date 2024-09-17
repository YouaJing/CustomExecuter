package tcc.youajing.customexecuter;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import io.papermc.lib.PaperLib;
import org.bukkit.inventory.ItemStack;


public class CEListener implements org.bukkit.event.Listener {
    private final CustomExecuter plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public CEListener(CustomExecuter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().getItemMeta().hasCustomModelData()) {
            //魔镜magic_mirror12700010
            if (event.getItem().getItemMeta().getCustomModelData() == 12700010) {
                Player player = event.getPlayer();
                event.setCancelled(true);
                // 使用PaperLib API 异步获取床位置
                PaperLib.getBedSpawnLocationAsync(player, true).thenAccept(location -> {
                    if (location != null) {
                        player.sendMessage(miniMessage.deserialize("<gradient:#8A2BE2:#00BFFF>一阵神秘的光芒后...</gradient>"));
                        plugin.getPlatform().teleportPlayer(player, location);
                    } else {
                        player.sendMessage(miniMessage.deserialize("<red>你还没有设置床（床是不是被挖了？？）!</red>"));
                    }
                });
            }
            //回忆药水recall_potion12700012
            if (event.getItem().getItemMeta().getCustomModelData() == 12700012) {
                Player player = event.getPlayer();
                // 使用PaperLib API 异步获取床位置
                PaperLib.getBedSpawnLocationAsync(player, true).thenAccept(location -> {
                    if (location != null) {
                        // 给玩家三个空的玻璃瓶并放到主手上
                        ItemStack glassBottle = new ItemStack(Material.GLASS_BOTTLE, 3);
                        player.getInventory().setItemInMainHand(glassBottle);
                        player.sendMessage(miniMessage.deserialize("<gradient:#89f7fe:#66a6ff>带你回到那个地方...</gradient>"));
                        plugin.getPlatform().teleportPlayer(player, location);
                    } else {
                        event.setCancelled(true);
                        player.sendMessage(miniMessage.deserialize("<red>你还没有设置床（床是不是被挖了？？）!</red>"));
                    }
                });
            }
        }
    }
}

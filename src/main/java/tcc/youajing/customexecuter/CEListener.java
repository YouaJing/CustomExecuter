package tcc.youajing.customexecuter;

import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
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
                        // 给1个空瓶子
                        ItemStack emptyBottle = new ItemStack(org.bukkit.Material.GLASS_BOTTLE);
                        player.getInventory().addItem(emptyBottle);
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

    @EventHandler
    public void onPlayerAttack(PrePlayerAttackEntityEvent event) {
        if (event.getPlayer().getItemInHand().getItemMeta().hasCustomModelData()) {
            if (event.getPlayer().getItemInHand().getItemMeta().getCustomModelData() == 12700011) {

            }
        }

    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        // 检查玩家输入的消息是否包含 [i] 或 [item]
        if (message.contains("[i]") || message.contains("[item]")) {
            ItemStack handItem = player.getInventory().getItemInMainHand();
            Component displayName;

            if (handItem != null && handItem.getType() != Material.AIR) {
                displayName = handItem.displayName();
                displayName = displayName.hoverEvent(handItem.asHoverEvent());
            } else {
                displayName = Component.text("空手");
            }

            // 初始化 LegacyComponentSerializer
            LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.builder().hexColors().hexCharacter('#').character('&').build();

            // 将物品信息插入到消息中，并添加玩家名字
            Component playerName = Component.text(player.getName());
            Component itemInfo = Component.text("[")
                    .append(playerName)
                    .append(Component.text("的物品: "))
                    .append(displayName)
                    .append(Component.text("]"));

            Component chatMessage = legacySerializer.deserialize(message);
            chatMessage = chatMessage.replaceText(TextReplacementConfig.builder().matchLiteral("[i]").replacement(itemInfo).build());
            chatMessage = chatMessage.replaceText(TextReplacementConfig.builder().matchLiteral("[item]").replacement(itemInfo).build());

            // 取消原始消息并广播新消息
            event.setCancelled(true);
            Bukkit.broadcast(chatMessage);
        }
    }
}

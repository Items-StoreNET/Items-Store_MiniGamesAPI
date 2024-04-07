package net.items.store.minigames.api.api;

import com.google.common.collect.Lists;
import kotlin.Pair;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommand implements CommandExecutor {

    private List<Pair<ICommandExecutor, Parameter[]>> commandParameters;
    private List<String> commandNames;
    private String permission;

    public AbstractCommand(JavaPlugin javaPlugin, String permission, String commandName, String... commandAliases){
        this.permission = permission != null ? permission : "";
        this.commandParameters = Lists.newArrayList();
        this.commandNames = Lists.newArrayList();
        this.commandNames.add(commandName.toLowerCase());
        this.commandNames.addAll(Arrays.stream(commandAliases).map(x -> x.toLowerCase()).toList());

        register(javaPlugin);
        prepareCommandParameters();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (this.permission.isEmpty() || player.hasPermission(this.permission)) {
                if (this.commandNames.contains(command.getName().toLowerCase())) {
                    Pair<ICommandExecutor, Parameter[]> currentCommand = this.commandParameters.stream()
                            .filter(x -> compareArgs(x.getSecond(), args))
                            .findAny()
                            .orElse(null);

                    if (currentCommand == null) {
                        sendHelpMessage(player);
                        return false;
                    }

                    currentCommand.getFirst().execute(args, player);
                    return true;
                }
            } else {
                player.sendMessage("No_PERMS"); // TODO
            }
        }
        return false;
    }

    protected abstract void prepareCommandParameters();

    protected abstract void sendHelpMessage(Player player);

    protected void addCommandParameterExecutor(ICommandExecutor command, Parameter... commandParameters){
        this.commandParameters.add(new Pair<>(command, commandParameters));
    }

    private boolean compareArgs(Parameter[] commandParameters, String[] args){
        boolean equals = true;

        if (commandParameters.length != args.length){
            equals = false;
        } else {
            for (int i = 0; i < commandParameters.length; i++){
                ParameterType argParameterType = ParameterType.get(args[i].getClass());
                Parameter parameter = commandParameters[i];

                if (parameter.getParameterType() != argParameterType || parameter.compare(args[i]) == false){
                    equals = false;
                    break;
                }
            }
        }

        return equals;
    }

    private void register(JavaPlugin javaPlugin){
        for (String commandAlias : this.commandNames){
            javaPlugin.getCommand(commandAlias).setExecutor(this);
        }
    }
}

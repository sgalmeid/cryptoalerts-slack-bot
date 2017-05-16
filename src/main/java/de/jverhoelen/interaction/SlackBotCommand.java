package de.jverhoelen.interaction;

import com.google.common.base.MoreObjects;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class SlackBotCommand {

    private String command;
    private String subCommand;
    private Map<String, String> arguments = new HashMap<>();
    private String rawArguments;

    public SlackBotCommand(SlackMessagePosted slackMsg) {
        this(slackMsg.getMessageContent());
    }

    public SlackBotCommand(String messagePosted) {
        String[] split = messagePosted.split(" ");

        command = split[0].toLowerCase();

        if (isParamsSection(split[1])) {
            initializeArguments(split[1]);
        } else {
            subCommand = split[1].toLowerCase();
            initializeArguments(split[2]);
        }
    }

    public void initializeArguments(String args) {
        arguments.putAll(extractParams(args));
        rawArguments = args;
    }

    public static Map<String, String> extractParams(String params) {
        HashMap<String, String> result = new HashMap<>();
        String[] kvPairs = params.split(",");

        for (String pair : kvPairs) {
            String[] kvSplit = pair.split("=");
            result.put(kvSplit[0], kvSplit[1]);
        }

        return result;
    }

    public String getRawArguments() {
        return rawArguments;
    }

    public boolean isArgumentsSecondSegment() {
        return StringUtils.isEmpty(subCommand);
    }

    public boolean argumentsContain(String str) {
        return rawArguments.contains(str);
    }

    public String getArgument(String name) {
        return arguments.get(name);
    }

    public String getCommand() {
        return command;
    }

    public String getSubCommand() {
        return subCommand;
    }

    public Map<String, String> getArguments() {
        return arguments;
    }

    static boolean isParamsSection(String s) {
        return s.contains("=");
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("command", command)
                .add("subCommand", subCommand)
                .add("arguments", arguments)
                .add("rawArguments", rawArguments)
                .toString();
    }
}

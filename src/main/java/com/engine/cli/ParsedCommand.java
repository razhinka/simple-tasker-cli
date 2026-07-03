package com.engine.cli;

import java.util.HashMap;
import java.util.List;

public record ParsedCommand(String command, HashMap<String, List<String>> args) {

}

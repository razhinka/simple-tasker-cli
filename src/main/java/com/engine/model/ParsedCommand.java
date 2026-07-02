package com.engine.model;

import java.util.HashMap;

public record ParsedCommand(String command, HashMap<String, String> args) {

}

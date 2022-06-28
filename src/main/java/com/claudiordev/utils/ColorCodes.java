package com.claudiordev.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.HashMap;

public class ColorCodes {

    //Key, ColorValue
    static HashMap<String, ChatColor> color_codes = new HashMap<>(15);

    /**
     * Default Constructor, initialize the Color Codes HashMap Values;
     */
    public ColorCodes() {
        setColor_codes();
    }

    /**
     * Load the Color Codes to the respective values
     */
    void setColor_codes() {
        color_codes.put("&0", ChatColor.BLACK);
        color_codes.put("&1", ChatColor.DARK_BLUE);
        color_codes.put("&2", ChatColor.DARK_GREEN);
        color_codes.put("&3", ChatColor.DARK_AQUA);
        color_codes.put("&4", ChatColor.DARK_RED);
        color_codes.put("&5", ChatColor.DARK_PURPLE);
        color_codes.put("&6", ChatColor.YELLOW);
        color_codes.put("&7", ChatColor.GRAY);
        color_codes.put("&8", ChatColor.DARK_GRAY);
        color_codes.put("&9", ChatColor.BLUE);
        color_codes.put("&a", ChatColor.GREEN);
        color_codes.put("&b", ChatColor.AQUA);
        color_codes.put("&c", ChatColor.RED);
        color_codes.put("&d", ChatColor.LIGHT_PURPLE);
        color_codes.put("&e", ChatColor.GOLD);
        color_codes.put("&f", ChatColor.WHITE);
    }

    /**
     * Execute the replace from the Key String value to
     * the ChatColor code and returns the String withouth the Key String and
     * with the ChatColor code on it's place
     * @param value String to be modified
     * @return String modified
     */
    public static String executeReplace(String value) {
        for (String string_key : color_codes.keySet()) {
            if (value.contains(string_key)) {
                value = color_codes.get(string_key) + value.replace(string_key, "");
            }
        }
        return value;
    }
}
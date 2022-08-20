package com.saidas.saiscaling.core.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.saidas.saiscaling.SaiDifficultyMod;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootSerializers;
import net.minecraft.loot.LootTable;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.NoSuchFileException;

/** Utility to read/write to the json config file.
 *
 *  /config/<modname>/file
 *  /config/<modname>/collection/file
 */
public class SaiConfigUtil {
    /** The Shared GSON object we used to make json data. **/
    public static final Gson GSON = LootSerializers.createLootTableSerializer()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();
    /** The DIRECTORY written to for the config. **/
    public static final String DIRECTORY = Paths.get(".") + "/config/" + SaiDifficultyMod.MOD_ID + "/";

    public static void init() {
        new File(DIRECTORY).mkdirs();
    }
    /** Read the contents of a given file in the config folder.
     * @param filename The name of the file
     */
    public static String readFile(String filename) {
        String result = "{}";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(DIRECTORY + filename))) {
            result = IOUtils.toString(reader);
        }
        catch(Exception e) {
            SaiLogger.exception(e);
        }
        return result;
    }

    public static <T> T readFile(String filename, Class<T> type) {
        T result = null;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(DIRECTORY + filename))) {
            result = GSON.fromJson(reader, type);
        }
        catch(NoSuchFileException e) {
            result = null;
        }
        catch (Exception e) {
            SaiLogger.exception(e);
        }
        return result;
    }

    public static <T> T readCollectionFile(String filename, String collection, Class<T> type) {
        T result = null;
        try(BufferedReader reader = Files.newBufferedReader(Paths.get(DIRECTORY + collection + "/" + filename))) {
            result = GSON.fromJson(reader, type);
        }
        catch(NoSuchFileException e) {
            result = null;
        }
        catch(Exception e) {
            SaiLogger.exception(e);
        }
        return result;
    }

    /** Write the contents to the given file in the config folder.
     * @param filename The name of the file
     * @param fileContents The contents of the file
     */
    public static void writeFile(String filename, String fileContents) {
        try(PrintWriter pw = new PrintWriter(DIRECTORY + filename)) {
            pw.println(fileContents);
        }
        catch (Exception e) {
            SaiLogger.exception(e);
        }
    }

    public static <T> void writeFile(String filename, T object) {
        try(PrintWriter pw = new PrintWriter(DIRECTORY + filename)) {
            GSON.toJson(object, pw);
        }
        catch (Exception e) {
            SaiLogger.exception(e);
        }
    }

    public static boolean exists(String filename) {
        File file = new File(DIRECTORY + filename);
        return file.exists() && !file.isDirectory();
    }
}

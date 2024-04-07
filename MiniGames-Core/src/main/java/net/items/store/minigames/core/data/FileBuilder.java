package net.items.store.minigames.core.data;

import net.items.store.minigames.api.MiniGame;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class FileBuilder {

    private static FileBuilder fileBuilder;

    static {
        fileBuilder = new FileBuilder();
    }

    protected FileBuilder() {}

    public void loadConfiguration(String fileName){
        if(!Files.exists(Paths.get(MiniGame.getJavaPlugin().getDataFolder().getAbsolutePath(), fileName))){
            try {
                Path path = Files.createFile(Paths.get(MiniGame.getJavaPlugin().getDataFolder().getAbsolutePath(), fileName));
                InputStream inputStream = getClass().getResourceAsStream("/" + fileName);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");

                try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                     BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8)){
                    for (String line : bufferedReader.lines().collect(Collectors.toList())){
                        bufferedWriter.append(line);
                        bufferedWriter.newLine();
                    }
                }
            } catch (IOException exception){
                exception.printStackTrace();
            }
        }
    }

    public File loadDefaultFile(String fileName, boolean defaultConfiguration){
        if(defaultConfiguration){
            fileBuilder.loadConfiguration(fileName);
        } else {
            if(Files.notExists(Paths.get(MiniGame.getJavaPlugin().getDataFolder().getAbsolutePath(), fileName))) {
                try {
                    Files.createFile(Paths.get(MiniGame.getJavaPlugin().getDataFolder().getAbsolutePath(), fileName));
                } catch (IOException exception){
                    exception.printStackTrace();
                }
            }
        }

        File file = Paths.get(MiniGame.getJavaPlugin().getDataFolder().getAbsolutePath(), fileName).toFile();
        return file;
    }

    public static JSONArray loadJSONArray(String fileName, boolean defaultConfiguration){
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = null;
        File file = fileBuilder.loadDefaultFile(fileName, defaultConfiguration);

        try (InputStreamReader inputStreamReader =
                     new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            if (file.length() == 0) {
                jsonArray = new JSONArray();
            } else {
                jsonArray = (JSONArray) jsonParser.parse(inputStreamReader);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public static JSONObject loadJSONObject(String fileName, boolean defaultConfiguration){
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        File file = fileBuilder.loadDefaultFile(fileName, defaultConfiguration);

        try (InputStreamReader inputStreamReader =
                     new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            if (file.length() == 0) {
                jsonObject = new JSONObject();
            } else {
                jsonObject = (JSONObject) jsonParser.parse(inputStreamReader);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import dtos.RenameMeDTO;
import java.util.Properties;
import java.util.Set;
import com.google.gson.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 *
 * @author tha
 */
public class Utility {

    private static Gson gson = new GsonBuilder().create();

    public static void printAllProperties() {
        Properties prop = System.getProperties();
        Set<Object> keySet = prop.keySet();
        for (Object obj : keySet) {
            System.out.println("System Property: {"
                    + obj.toString() + ","
                    + System.getProperty(obj.toString()) + "}");
        }
    }

    public static String readFileProperty(String property) throws IOException {
        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";
            InputStream inputStream = Utility.class.getClassLoader().getResourceAsStream("config.properties");

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            String user = prop.getProperty(property);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileNotFoundException("Could not find the property");
        }
    }

    public static RenameMeDTO json2DTO(String json) throws UnsupportedEncodingException {
        return gson.fromJson(new String(json.getBytes("UTF8")), RenameMeDTO.class);
    }

    public static String DTO2json(RenameMeDTO rmDTO) {
        return gson.toJson(rmDTO, RenameMeDTO.class);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        printAllProperties();

        //Test json2DTO and back again
        String str2 = "{'id':1, 'str1':'Dette er den første tekst', 'str2':'Her er den ANDEN'}";
        RenameMeDTO rmDTO = json2DTO(str2);
        System.out.println(rmDTO);

        String backAgain = DTO2json(rmDTO);
        System.out.println(backAgain);
    }

}

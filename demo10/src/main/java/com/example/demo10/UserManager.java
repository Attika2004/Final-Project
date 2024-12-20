package com.example.demo10;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class UserManager {

    private static final String USER_DATA_FILE = "user_data.txt";
    private Map<String, String> users;

    public UserManager() {
        users = new HashMap<>();
        loadUserData();
    }


    public void loadUserData() {
        File file = new File(USER_DATA_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] user = line.split(":");
                if (user.length == 2) {
                    users.put(user[0], user[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveUserData() {
        File file = new File(USER_DATA_FILE);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Map.Entry<String, String> entry : users.entrySet()) {
                    writer.write(entry.getKey() + ":" + entry.getValue());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addUser(String username, String password) {
        if (!users.containsKey(username)) {
            users.put(username, password);
            saveUserData();
        }
    }


    public boolean validateLogin(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }



}

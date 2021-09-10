package controller;

import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManagement implements IGeneralManagement<User>,WriteFileAndReadFile<User> {
    static List<User> userList = new ArrayList<>();

    static {
        userList.add(new User("admin","admin","admin"));
    }

    public List<User> getUserList() {
        return userList;
    }

    @Override
    public void showAll() {
        for (User users : userList) {
            System.out.println(userList);
        }
    }

    @Override
    public void add(User user) {
        userList.add(user);
    }

    @Override
    public void updateById(int index, User user) {
        userList.set(index, user);
    }

    @Override
    public void removeById(int index) {
        userList.remove(index);
    }

    public int isLogin(User user) {
        int index = -1;
        for (User user1 : userList) {
            if (user.getUsername().equals(user1.getUsername())
                    && user.getPassword().equals(user1.getPassword())
                    && user1.getRole().equals("admin")) {
                index = 1;
            } else if (user.getUsername().equals(user1.getUsername())
                    && user.getPassword().equals(user1.getPassword())
                    && user1.getRole().equals("user")) {
                index = 0;
            }
        }
        return index;
    }

    public boolean checkUserName(String username) {
        boolean flag = false;
        for (int i = 0; i < userList.size(); i++) {
            if (username.equals(userList.get(i).getUsername())) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public int findUserByUserName(String username) {
        int index = -1;
        for (int i = 0; i < userList.size(); i++) {
            if (username.equals(userList.get(i).getUsername())) {
                index = i;
            }
        }
        return index;
    }

    @Override
    public void writeFile(String path) {
        try {
            OutputStream os = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(userList);
            os.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> readFile(String path) {
        try {
            InputStream is = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(is);
            userList = (List<User>) ois.readObject();
            is.close();
            ois.close();
        } catch (EOFException e) {
            return null;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userList;
    }
}
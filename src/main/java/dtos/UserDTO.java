/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tha
 */
public class UserDTO {
    private String username;
    private String password;
    private List<String> roles = new ArrayList<>();

    public UserDTO(String username, String password){
        this.username = username;
        this.password = password;
    }
    public UserDTO(User user){
        this.username = user.getUserName();
        this.password = user.getUserPass();
        user.getRoleList().forEach(role->this.roles.add(role.getRoleName()));
        System.out.println(user);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    public void addRole(String role) {
        this.roles.add(role);
    }

    @Override
    public String toString() {
        return "UserDTO{" + "username=" + username + ", password=" + password + ", roles=" + roles + '}';
    }
    
}

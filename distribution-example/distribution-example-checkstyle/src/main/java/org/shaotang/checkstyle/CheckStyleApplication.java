package org.shaotang.checkstyle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CheckStyleApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheckStyleApplication.class, args);
    }

    class User {
        int id;
        String username;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public User() {
        }

        @Override
        public String toString() {
            return "User{" + "id=" + id + ", username='" + username + '\'' + '}';
        }

        public User(int id, String username) {

            this.id = id;
            this.username = username;
        }
    }

}

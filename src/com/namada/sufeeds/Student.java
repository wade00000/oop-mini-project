// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

import java.time.LocalDateTime;

public class Student {
    private Integer id;
    private String admissionNumber;
    private String name;
    private String hashedPassword;
    private LocalDateTime createdAt;


    public Student(String admissionNumber,String name,String password){
        this.admissionNumber = admissionNumber;
        this.name = name;
        this.hashedPassword = PasswordHasher.hashPassword(password) ;
    }

    public Student(Integer id ,String admissionNumber,String name,String hashedPassword,LocalDateTime createdAt){
        this.id = id;
        this.admissionNumber = admissionNumber;
        this.name = name;
        this.hashedPassword = hashedPassword;
        this.createdAt = createdAt;

    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", admissionNumber='" + admissionNumber + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAdmissionNumber() { return admissionNumber; }
    public void setAdmissionNumber(String admissionNumber) { this.admissionNumber = admissionNumber; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getHashedPassword() {return hashedPassword;}
    public void setPassword(String password) {this.hashedPassword = PasswordHasher.hashPassword(password);}

    public boolean verifyPassword (String password,String hashedPassword){
        return PasswordHasher.verifyPassword(password,hashedPassword) ;
    }
}
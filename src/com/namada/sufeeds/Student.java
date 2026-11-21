// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

import java.time.LocalDateTime;

public class Student {
    private Integer id;
    private String admissionNumber;
    private String name;
    private LocalDateTime createdAt;


    public Student(String admissionNumber,String name){
        this.admissionNumber = admissionNumber;
        this.name = name;
    }

    public Student(Integer id ,String admissionNumber,String name,LocalDateTime createdAt){
        this.id = id;
        this.admissionNumber = admissionNumber;
        this.name = name;
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

}
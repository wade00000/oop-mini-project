// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

import java.time.LocalDateTime;

public class Topic {
    private Integer id;
    private Integer classId;
    private Integer studentId;
    private String title;
    private LocalDateTime createdAt;

    public Topic(Integer classId,Integer studentId,String title){
        this.classId = classId;
        this.studentId = studentId;
        this.title = title;
    }

    public Topic(Integer id,Integer classId,Integer studentId,String title,LocalDateTime createdAt){
        this.id = id;
        this.classId = classId;
        this.studentId = studentId;
        this.title = title;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", classId=" + classId +
                ", studentId=" + studentId +
                ", title='" + title + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public Integer getClassId() {return classId;}
    public void setClassId(Integer classId) {this.classId = classId;}

    public Integer getStudentId() {return studentId;}
    public void setStudentId(Integer studentId) {this.studentId = studentId;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
}

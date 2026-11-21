// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

import java.time.LocalDateTime;

public class Feedback {
    private Integer id;
    private Integer topicId;
    private Integer studentId;
    private String content;
    private LocalDateTime createdAt;

    public Feedback(Integer topicId,Integer studentId,String content){
        this.topicId = topicId;
        this.studentId = studentId;
        this.content= content;
    }

    public Feedback(Integer id,Integer topicId,Integer studentId,String content,LocalDateTime createdAt){
        this.id = id;
        this.topicId = topicId;
        this.studentId = studentId;
        this.content= content;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", topicId=" + topicId +
                ", studentId=" + studentId +
                ", content='" + content+ '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public Integer getTopicId() {return topicId;}
    public void setTopicId(Integer topicId) {this.topicId = topicId;}

    public Integer getStudentId() {return studentId;}
    public void setStudentId(Integer studentId) {this.studentId = studentId;}

    public String getContent() {return content;}
    public void setContent(String content) {this.content= content;}

    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
}

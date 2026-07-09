package com.market.finder.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "instructor_detail")
public class InstructorDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "age")
    private int age;
    @Column(name = "youtube_channel")
    private String youtubeChannel;

    public InstructorDetail() {
    }

    public InstructorDetail(int age, String youtubeChannel) {
        this.age = age;
        this.youtubeChannel = youtubeChannel;
    }

    public String getYoutubeChannel() {
        return youtubeChannel;
    }

    public void setYoutubeChannel(String youtubeChannel) {
        this.youtubeChannel = youtubeChannel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setInstructorDetail(InstructorDetail tempInstructorDetail) {
        tempInstructorDetail.age = age;
        tempInstructorDetail.youtubeChannel = youtubeChannel;

    }

    @Override
    public String toString() {
        return "InstructorDetail{" +
                "id=" + id +
                ", age=" + age +
                ", youtubeChannel='" + youtubeChannel + '\'' +
                '}';
    }
}

package com.example.project_software_mobile;

public class QuizData {
    private String title;
    private String answer;
    private String ques1;
    private String ques2;
    private String ques3;
    private boolean tfQues;

    public QuizData(String title, String answer, String ques1, String ques2, String ques3, boolean tfQues){
        this.title = title;
        this.answer = answer;
        this.ques1 = ques1;
        this.ques2 = ques2;
        this.ques3 = ques3;
        this.tfQues = tfQues;
    }
    public String getAnswer() {
        return answer;
    }

    public String getQues1() {
        return ques1;
    }

    public String getQues2() {
        return ques2;
    }

    public String getQues3() {
        return ques3;
    }

    public String getTitle() {
        return title;
    }

    public boolean getTfQues(){return tfQues;}
}

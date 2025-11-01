package com.cma.thesis.cropmanagementapp;

/**
 * Data model for FAQ items
 */
public class Class_FAQ {
    private String question;
    private String answer;
    private String category;

    public Class_FAQ() {
    }

    public Class_FAQ(String question, String answer, String category) {
        this.question = question;
        this.answer = answer;
        this.category = category;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

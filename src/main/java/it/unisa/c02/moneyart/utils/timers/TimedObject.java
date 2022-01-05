package it.unisa.c02.moneyart.utils.timers;

import java.io.Serializable;
import java.util.Date;

public class TimedObject {

  public TimedObject(Serializable data, String taskType, Date taskDate) {

    this.attribute = data;
    this.taskType = taskType;
    this.taskDate = taskDate;

  }

  public TimedObject() {

  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Serializable getAttribute() {
    return attribute;
  }

  public void setAttribute(Serializable attribute) {
    this.attribute = attribute;
  }

  public String getTaskType() {
    return taskType;
  }

  public void setTaskType(String taskType) {
    this.taskType = taskType;
  }

  public Date getTaskDate() {
    return taskDate;
  }

  public void setTaskDate(Date taskDate) {
    this.taskDate = taskDate;
  }

  @Override
  public String toString() {
    return "TimedObject{"
        +
        "id=" + id
        +
        ", beanId=" + attribute
        +
        ", taskType='" + taskType + '\''
        +
        ", taskDate=" + taskDate
        +
        '}';
  }

  private Integer id;
  private Serializable attribute;
  private String taskType;
  private Date taskDate;

}

package it.unisa.c02.moneyart.utils.timers;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Contiene le informazioni necessarie per rappresentare un timer attivo.
 */
public class TimedObject {

  /**
   * Istanzia l'oggetto per rappresentare un timer attivo.
   *
   * @param attribute dati aggiuntivi che vengono usati per l'esecuzione del servizio
   * @param taskType  il tipo di servizio che deve essere eseguito al termine del timer
   * @param taskDate  il momento in cui il timer si attiva e il servizio viene eseguito
   */
  public TimedObject(Serializable attribute, String taskType, Date taskDate) {

    this.attribute = attribute;
    this.taskType = taskType;
    this.taskDate = taskDate;

  }

  /**
   * Istanzia un oggetto vuoto per rappresentare un timer attivo.
   */
  public TimedObject() {

  }

  /**
   * Restituisce l'identificativo del timer.
   *
   * @return l'identificativo del timer
   */
  public Integer getId() {
    return id;
  }

  /**
   * Modifica l'identificativo del timer.
   *
   * @param id il nuovo valore dell'identificativo del timer
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Restituisce i dati aggiuntivi per l'esecuzione del servizio allo scadere del timer.
   *
   * @return i dati aggiuntivi per l'esecuzione del servizio allo scadere del timer
   */
  public Serializable getAttribute() {
    return attribute;
  }

  /**
   * Modifica i dati aggiuntivi per l'esecuzione del servizio allo scadere del timer.
   *
   * @param attribute i dati aggiuntivi per l'esecuzione del servizio allo scadere del timer
   */
  public void setAttribute(Serializable attribute) {
    this.attribute = attribute;
  }

  /**
   * Restituisce il tipo di servizio eseguito allo scadere del timer.
   *
   * @return il tipo di servizio eseguito allo scadere del timer
   */
  public String getTaskType() {
    return taskType;
  }

  /**
   * Modifica il tipo di servizio eseguito allo scadere del timer.
   *
   * @param taskType il tipo di servizio eseguito allo scadere del timer
   */
  public void setTaskType(String taskType) {
    this.taskType = taskType;
  }

  /**
   * Restituisce il momento in cui il timer viene attivato e il servizio viene eseguito.
   *
   * @return il momento in cui il timer viene attivato e il servizio viene eseguito
   */
  public Date getTaskDate() {
    return taskDate;
  }

  /**
   * Modifica il momento in cui il timer viene attivato e il servizio viene eseguito.
   *
   * @param taskDate il momento in cui il timer viene attivato e il servizio viene eseguito
   */
  public void setTaskDate(Date taskDate) {
    this.taskDate = taskDate;
  }

  @Override
  public String toString() {
    return "TimedObject{"
        +
        "id=" + id
        +
        ", attribute=" + attribute
        +
        ", taskType='" + taskType + '\''
        +
        ", taskDate=" + taskDate
        +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TimedObject)) {
      return false;
    }
    TimedObject that = (TimedObject) o;
    return Objects.equals(getId(), that.getId()) &&
        Objects.equals(getAttribute(), that.getAttribute()) &&
        Objects.equals(getTaskType(), that.getTaskType()) &&
        Objects.equals(getTaskDate(), that.getTaskDate());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getAttribute(), getTaskType(), getTaskDate());
  }

  private Integer id;
  private Serializable attribute;
  private String taskType;
  private Date taskDate;

}

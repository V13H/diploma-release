package com.hvs.diploma.turbo_sms;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "vovagolev")
//This class created accordingly to the Turbosms.ua docs
//details at https://turbosms.ua/sql.html
public class TurboSmsMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, insertable = false)
    private int id;
    @Column(name = "msg_id", updatable = false, insertable = false)
    private String msgId;
    private String number;
    private String sign;
    private String message;
    @Column(updatable = false, insertable = false)
    private Double cost;
    @Column(updatable = false, insertable = false)
    private Double balance;
    @Column(updatable = false, insertable = false)
    private Timestamp added;
    @Column(name = "send_time")
    private Timestamp sendTime;
    @Column(updatable = false, insertable = false)
    private Timestamp sended;
    @Column(updatable = false, insertable = false)
    private Timestamp received;
    @Column(name = "error_code", updatable = false, insertable = false)
    private String errorCode;
    @Column(updatable = false, insertable = false)
    private String status;

    public TurboSmsMessage(String number, String message) {
        this.number = number;
        this.message = message;
    }

    public TurboSmsMessage() {
    }

    public int getId() {
        return id;
    }

    public String getMsgId() {
        return msgId;
    }

    public String getNumber() {
        return number;
    }

    public String getSign() {
        return sign;
    }

    public String getMessage() {
        return message;
    }

    public double getCost() {
        return cost;
    }

    public double getBalance() {
        return balance;
    }

    public Timestamp getAdded() {
        return added;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public Timestamp getSended() {
        return sended;
    }

    public Timestamp getRecieved() {
        return received;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setAdded(Timestamp added) {
        this.added = added;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public void setSended(Timestamp sended) {
        this.sended = sended;
    }

    public void setRecieved(Timestamp recieved) {
        this.received = recieved;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TurboSmsMessage{" +
                "number='" + number + '\'' +
                ", sign='" + sign + '\'' +
                ", message='" + message + '\'' +
                ", sendTime=" + sendTime +
                '}';
    }
}


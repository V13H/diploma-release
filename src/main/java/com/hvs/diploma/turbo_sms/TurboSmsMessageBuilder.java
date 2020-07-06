package com.hvs.diploma.turbo_sms;

import com.hvs.diploma.entities.Account;

import java.sql.Timestamp;
import java.time.LocalDateTime;

//Builder just to make it easy to instantiate TurboSmsMessage
public class TurboSmsMessageBuilder {
    private TurboSmsMessage message = new TurboSmsMessage();

    public TurboSmsMessageBuilder(String userSign) {
        if (userSign != null && !userSign.isEmpty()) {
            this.message.setSign(userSign);
        } else {
            throw new IllegalArgumentException("Unable to create TurboSmsMessageBuilder with constructor" +
                    "TurboSmsMessageBuilder(String userSign).The \"userSign\" argument can`t be empty or \"null\"");
        }
    }

    public TurboSmsMessageBuilder() {
    }

    public TurboSmsMessageBuilder sign(String sign) {
        if (sign != null && !sign.isEmpty()) {
            message.setSign(sign);
        } else {
            throw new IllegalArgumentException("\"sign\" can`t be empty or \"null\"");
        }
        return this;
    }

    public TurboSmsMessageBuilder sendTime(Timestamp sendTime) {
        if (sendTime != null) {
            message.setSendTime(sendTime);
        } else {
            message.setSendTime(Timestamp.valueOf(LocalDateTime.now()));
        }
        return this;
    }

    public TurboSmsMessageBuilder phoneNumber(String number) {
        if (number != null && !number.isEmpty()) {
            message.setNumber(number);
        } else {
            throw new IllegalArgumentException("\"phoneNumber\" can`t be empty or \"null\"");
        }
        return this;
    }

    public TurboSmsMessageBuilder phoneNumber(Account account) {
        if (account != null) {
            if (!account.getPassword().startsWith("+38")) {
                message.setNumber("+38" + account.getPhoneNumber());
            } else {
                message.setNumber(account.getPhoneNumber());
            }
        } else {
            throw new IllegalArgumentException("Unable to set phone number due to \"account\" arg == \"null\"");
        }
        return this;
    }

    public TurboSmsMessageBuilder message(String text) {
        message.setMessage(text);
        return this;
    }

    public TurboSmsMessage build() {
        if (message.getSendTime() == null) {
            sendTime(Timestamp.valueOf(LocalDateTime.now()));
        }
        return message;
    }

}

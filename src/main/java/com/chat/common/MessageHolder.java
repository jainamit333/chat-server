package com.chat.common;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by amit on 26/12/16.
 */
@Getter
@Setter
public class MessageHolder implements Serializable{


    private String sourceClientId;
    private String destinationClientId;
    private String message;
    private Date createdAt = new Date();

    public MessageHolder() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageHolder)) return false;
        if (o == null) return false;
        MessageHolder that = (MessageHolder) o;

        if (!sourceClientId.equals(that.sourceClientId)) return false;
        if (!destinationClientId.equals(that.destinationClientId)) return false;
        if (!message.equals(that.message)) return false;

        return  createdAt.getTime() - that.createdAt.getTime() < 5000;

    }

    @Override
    public int hashCode() {
        int result = sourceClientId.hashCode();
        result = 31 * result + destinationClientId.hashCode();
        result = 31 * result + message.hashCode();
        result = 31 * result + createdAt.hashCode();
        return result;
    }
}

package com.lymytz.lymytzsell.business.helpers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseAction<T> {
    private T entity;
    private StatutResponse statutResponse;

    public ResponseAction(T entitty, StatutResponse statutResponse) {
        this.entity = entitty;
        this.statutResponse = statutResponse;
    }
}

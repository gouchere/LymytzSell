/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.service.utils;

/**
 *
 * @author LENOVO
 */
public class LymytzException extends Exception {

    private String message;

    public LymytzException() {
        super();
    }

    public LymytzException(String message) {
        super(message);
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.seerbitapi.dtos;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author HP
 */
public record Transaction(@NotNull(message = "Amount must not be null") BigDecimal amount, 
        @NotNull(message = "timestamp must not be null")  Date timestamp) {
    
}

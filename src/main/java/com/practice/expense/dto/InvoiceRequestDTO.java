package com.practice.expense.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class InvoiceRequestDTO {
    public enum InputType {
        IMAGE, BANK_STATEMENT, USER
    }

    private InputType inputType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private byte[] imageData;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BankStatementData bankStatementData;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserInputData userInput;

   
}

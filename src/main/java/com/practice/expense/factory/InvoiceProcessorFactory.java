package com.practice.expense.factory;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.practice.expense.dto.InvoiceRequestDTO.InputType;
import com.practice.expense.service.InvoiceProcessor;

@Component
public class InvoiceProcessorFactory {

    private final Map<String, InvoiceProcessor> processors;

    public InvoiceProcessorFactory(Map<String, InvoiceProcessor> processors) {
        this.processors = processors;
    }

    public InvoiceProcessor getProcessor(InputType inputType) {
        InvoiceProcessor processor = processors.get(inputType.name());
        if (processor == null) {
            throw new IllegalArgumentException("Invalid invoice type: " + inputType);
        }
        return processor;
    }
}
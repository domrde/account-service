package com.example.accountservice.component.account.core.dto;

import com.example.accountservice.component.account.operation.dto.AddAccountValueOperation;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = AddAccountValueOperation.class),
})
public interface AccountOperation {

}

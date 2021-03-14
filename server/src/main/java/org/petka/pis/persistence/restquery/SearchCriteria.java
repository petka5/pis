package org.petka.pis.persistence.restquery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SearchCriteria {

    private String key;
    private SearchOperation operation;
    private Object value;
}

package com.example.clms.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class DomainInstanceRequest {

        @JsonProperty
        private int instanceId;

        @JsonProperty
        private String domainName;

        public int getInstanceId() {
                return instanceId;
        }

        public String getDomainName() {
                return domainName;
        }
}

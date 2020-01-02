package com.pawban.communicator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class JokeDto {

    private int id;
    private String joke;

    @JsonProperty("value")
    public void mapJoke(Map<String, Object> value) {
        this.id = (int) value.get("id");
        this.joke = (String) value.get("joke");
    }

}

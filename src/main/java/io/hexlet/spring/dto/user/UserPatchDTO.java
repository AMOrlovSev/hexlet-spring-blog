package io.hexlet.spring.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class UserPatchDTO {
    private JsonNullable<String> email = JsonNullable.undefined();
    private JsonNullable<String> firstName = JsonNullable.undefined();
    private JsonNullable<String> lastName = JsonNullable.undefined();
}
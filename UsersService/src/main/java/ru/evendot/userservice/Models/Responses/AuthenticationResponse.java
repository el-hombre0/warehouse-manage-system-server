package ru.evendot.userservice.Models.Responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.evendot.userservice.Models.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    /**
     * Токен, возвращаемый обратно пользователю
     */
    private String token;
    /**
     * Данные пользователя
     */
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Role role;
}

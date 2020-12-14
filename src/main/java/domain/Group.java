package domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Group {

    private Integer id;
    private String name;
    private String groupHeadLogin;
    private List<String> memberLogins;

    public boolean isGrouphead(User user) {
        return user.isGrouphead() && user.getLogin().equals(groupHeadLogin);
    }
}

package pabo.springboot.config.auth.dto;

import lombok.Builder;
import lombok.Getter;
import pabo.springboot.domain.user.Role;
import pabo.springboot.domain.user.User;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String ,Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String ,Object> attributes,String nameAtttributeKey,String name,String email,String picture){
        this.attributes = attributes;
        this.nameAttributeKey = nameAtttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if("naver".equals(registrationId)){
            return ofNaver("id",attributes);
        }

        return ofGoogle(userNameAttributeName,attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .picture((String) attributes.get("picture"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAtttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName,Map<String,Object> attributes){
        Map<String,Object> attributes1 = (Map<String, Object>) attributes.get("response");
        return OAuthAttributes.builder()
                .name((String) attributes1.get("name"))
                .picture((String) attributes1.get("profile_image"))
                .email((String) attributes1.get("email"))
                .attributes(attributes1)
                .nameAtttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .build();
    }
}

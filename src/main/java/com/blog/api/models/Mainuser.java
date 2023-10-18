package com.blog.api.models;

import com.blog.domain.entities.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author claudio.vilas
 * date 09/2023
 */

@AllArgsConstructor
@Getter
@Setter
public class Mainuser implements UserDetails {
    private String username;
    private String password;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;

    public static Mainuser build(ApplicationUser user){
        List<GrantedAuthority> authorities = user.getAuthorities().stream().map(rol ->{
             return new SimpleGrantedAuthority(rol.getAuthority().name());
        }).collect(Collectors.toList());

        return new Mainuser(user.getUsername(), user.getPassword(), user.getEmail(),
                authorities);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

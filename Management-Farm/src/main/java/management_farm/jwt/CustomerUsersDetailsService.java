package management_farm.jwt;

import lombok.extern.slf4j.Slf4j;
import management_farm.dao.UserDao;
import management_farm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Date: 11/24/2023
 * Project: Management-Farm
 * Author: ManhTien
 */
@Service
@Slf4j
public class CustomerUsersDetailsService implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    private User userDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {}", username);
        userDetail = userDao.findUserByUserLogin(username);
        if (!Objects.isNull(userDetail)) {
            return new org.springframework.security.core.userdetails.User(userDetail.getUserLogin(),
                    userDetail.getPassword(), new ArrayList<>());
        } else throw new UsernameNotFoundException("User not found");
    }

    public User getUserDetail() {
        return userDetail;
    }
}

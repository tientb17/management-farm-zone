package management_farm;

import management_farm.dao.RoleDao;
import management_farm.dao.UserDao;
import management_farm.model.Role;
import management_farm.model.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.Objects;

@SpringBootApplication
public class ManagementFarmApplication {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    public static void main(String[] args) {
        SpringApplication.run(ManagementFarmApplication.class, args);
    }

    @Bean
    InitializingBean createDataRoleDefault() {
        Role roleExisted = roleDao.findByRoleName("admin");
        if (Objects.isNull(roleExisted)) {
            Role initRole = new Role();
            initRole.setRoleName("admin");
            initRole.setCreateDate(new Date());
            initRole.setStatus("true");
            return () -> {
                roleDao.save(initRole);
            };
        } else {
            return null;
        }
    }

    @Bean
    InitializingBean createDataUserDefault() {
        User userExisted = userDao.findUserByUserLogin("admin");
        if (Objects.isNull(userExisted)) {
            User initUser = new User();
            initUser.setUserLogin("admin");
            initUser.setPassword("$2a$10$WwXl1ighZTVnra82l.vlMuthQdwNwhmqSDBpw53bx6EVbqwUgsaOu");
            initUser.setStatus("true");
            initUser.setRoleId(roleDao.findById(1l).get());
            initUser.setCreateDate(new Date());

            return () -> {
                userDao.save(initUser);
            };
        } else {
            return null;
        }
    }


}

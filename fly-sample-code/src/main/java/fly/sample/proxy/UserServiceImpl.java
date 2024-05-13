package fly.sample.proxy;

public class UserServiceImpl implements UserService {
    @Override
    public String getUser(Long id) {
        return "user_" + id;
    }
}

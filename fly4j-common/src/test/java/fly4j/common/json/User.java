package fly4j.common.json;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by qryc on 2021/8/31
 */
class User {
    private String name;
    private int age;
    @JsonIgnore
    private String remark;


    public User() {
    }

    public User(String name, int age, String remark) {
        this.name = name;
        this.age = age;
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public User setAge(int age) {
        this.age = age;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public User setRemark(String remark) {
        this.remark = remark;
        return this;
    }
}

package com.oxchains.themis.common.constant;

/**
 * @author ccl
 * @time 2017-11-13 16:37
 * @name UserConstants
 * @desc:
 */
public interface UserConstants {

    enum UserRole{
        MANAGE(1L,"管理员"),SERVICE(2L,"客服"),ARBITRATION(3L,"仲裁者"),USER(4L,"普通用户");
        private String roleName;
        private Long roleId;

        UserRole(Long roleId,String roleName) {
            this.roleName = roleName;
            this.roleId = roleId;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public Long getRoleId() {
            return roleId;
        }

        public void setRoleId(Long roleId) {
            this.roleId = roleId;
        }
    }

}

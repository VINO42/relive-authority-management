CREATE TABLE `data_filed`
(
    `id`            BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `permission_id` BIGINT NOT NULL,
    `filed_name`    VARCHAR(255) NULL,
    `field_type`    VARCHAR(255) NULL,
    `enable`        TINYINT NULL,
    `create_time`   DATETIME NULL,
    `update_time`   DATETIME NULL
);
CREATE TABLE `element`
(
    `id`            BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `permission_id` BIGINT NOT NULL,
    `element_name`  VARCHAR(255) NULL,
    `element_code`  VARCHAR(255) NULL,
    `element_type`  VARCHAR(255) NULL,
    `enable`        VARCHAR(255) NULL,
    `create_time`   DATETIME NULL,
    `update_time`   DATETIME NULL
);
CREATE TABLE `menu`
(
    `id`            BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `permission_id` BIGINT NOT NULL,
    `parent_id`     BIGINT NULL,
    `title`         VARCHAR(255) NULL,
    `path`          VARCHAR(255) NULL,
    `icon`          VARCHAR(255) NULL,
    `sort`          INT NULL,
    `code`          VARCHAR(255) NULL,
    `type`          VARCHAR(255) NULL,
    `enable`        TINYINT NULL,
    `create_time`   DATETIME NULL,
    `update_time`   DATETIME NULL
);
CREATE TABLE `operation`
(
    `id`             BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `permission_id`  BIGINT NULL,
    `operation_name` VARCHAR(255) NULL,
    `operation_code` VARCHAR(255) NULL,
    `url_prefix`     VARCHAR(255) NULL,
    `enable`         TINYINT NULL,
    `create_time`    DATETIME NULL,
    `update_time`    DATETIME NULL
);
CREATE TABLE `permission`
(
    `id`              BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `permission_type` VARCHAR(255) NULL,
    `describe`        VARCHAR(255) NULL,
    `enable`          TINYINT NULL,
    `create_time`     DATETIME NULL,
    `update_time`     DATETIME NULL
);
CREATE TABLE `role_mtm_permission`
(
    `id`            BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `role_id`       BIGINT NULL,
    `permission_id` BIGINT NULL,
    `create_time`   DATETIME NULL,
    `update_time`   DATETIME NULL
);
CREATE TABLE `role`
(
    `id`          BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `parent_id`   BIGINT NULL,
    `role_name`   VARCHAR(255) NULL,
    `role_code`   VARCHAR(255) NULL,
    `enable`      TINYINT NULL,
    `create_time` DATETIME NULL,
    `update_time` DATETIME NULL
);
CREATE TABLE `user_mtm_organization`
(
    `id`              BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `user_id`         BIGINT NULL,
    `organization_id` BIGINT NULL,
    `enable`          TINYINT NULL,
    `create_time`     DATETIME NULL,
    `update_time`     DATETIME NULL
);
CREATE TABLE `organization`
(
    `id`          BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `parent_id`   BIGINT NULL,
    `group_name`  VARCHAR(255) NULL,
    `group_type`  VARCHAR(255) NULL,
    `enable`      TINYINT NULL,
    `create_time` DATETIME NULL,
    `update_time` DATETIME NULL
);
CREATE TABLE `user`
(
    `id`          BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `username`    VARCHAR(255) NOT NULL,
    `password`    VARCHAR(255) NOT NULL,
    `phone`       INT          NOT NULL,
    `email`       VARCHAR(255) NOT NULL,
    `user_type`   VARCHAR(255) NOT NULL,
    `non_locked`  TINYINT      NOT NULL,
    `status`      TINYINT      NOT NULL COMMENT '0:离线1:在线',
    `enable`      TINYINT      NOT NULL,
    `create_time` DATETIME     NOT NULL,
    `update_time` DATETIME     NOT NULL
);
CREATE TABLE `user_mtm_role`
(
    `id`          BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `user_id`     BIGINT   NOT NULL,
    `role_id`     BIGINT   NOT NULL,
    `create_time` DATETIME NOT NULL,
    `update_time` DATETIME NOT NULL
);

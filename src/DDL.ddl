-- auto-generated definition
create table user
(
    id         int auto_increment comment 'PK'
        primary key,
    name       varchar(10)  not null comment '이름',
    email      varchar(100) not null comment '이메일',
    password   varchar(255) not null comment '비밀번호',
    created_at datetime     not null comment '생성일자',
    constraint user_pk
        unique (email)
);

create index user_id_index
    on user (id);

-- auto-generated definition
create table post
(
    id         int auto_increment
        primary key,
    user_id    int                                not null,
    title      varchar(255)                       null,
    content    text                               null,
    author     varchar(10)                        null,
    created_at datetime default CURRENT_TIMESTAMP not null,
    updated_at datetime                           null,
    constraint post_user_id_fk
        foreign key (user_id) references user (id)
);

-- auto-generated definition
create table post_comment
(
    id         int auto_increment
        primary key,
    user_id    int                                not null,
    post_id    int                                not null,
    content    text                               null,
    author     varchar(10)                        null,
    created_at datetime default CURRENT_TIMESTAMP not null,
    updated_at datetime                           null,
    constraint post_comment_post_id_fk
        foreign key (post_id) references post (id),
    constraint post_comment_user_id_fk
        foreign key (user_id) references user (id)
);


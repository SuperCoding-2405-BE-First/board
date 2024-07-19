create table user
(
    user_id    int auto_increment primary key comment '사용자 ID (PK)',
    author     varchar(10)  not null comment '이름',
    email      varchar(100) not null comment '이메일',
    password   varchar(255) not null comment '비밀번호',
    created_at datetime     not null comment '생성일자',
    constraint user_email_unique
        unique (email)
);

create table post
(
    post_id    bigint auto_increment                 primary key comment '게시물 ID (PK)',
    user_id    bigint                                not null comment '사용자 ID (FK)',
    title      varchar(255)                       not null comment '게시물 제목',
    content    text                               null comment '게시물 내용',
    author     varchar(10)                        not null comment '작성자 : user_id',
    created_at datetime default CURRENT_TIMESTAMP not null comment '생성일자',
    updated_at datetime                           null comment '수정일자',
    constraint post_user_id_fk
        foreign key (user_id) references user(user_id)
);

create table comment
(
    comment_id bigint auto_increment                 primary key comment '댓글 ID (PK)',
    user_id    bigint                                not null comment '사용자 ID (FK)',
    post_id    bigint                                not null comment '게시물 ID (FK)',
    content    text                               not null comment '댓글 내용',
    author     varchar(10)                        not null comment '작성자 : user_id',
    created_at datetime default CURRENT_TIMESTAMP not null comment '생성일자',
    updated_at datetime                           null comment '수정일자',
    constraint comment_post_id_fk
        foreign key (post_id) references post (post_id),
    constraint comment_user_id_fk
        foreign key (user_id) references user (user_id)
);

create table great
(
    great_id   bigint auto_increment                 primary key comment '좋아요 ID (PK)',
    user_id    bigint                                not null comment '사용자 ID (FK)',
    post_id    bigint                                not null comment '게시물 ID (FK)',
    constraint like_user_id_fk
        foreign key (user_id) references user (user_id),
    constraint like_post_id_fk
        foreign key (post_id) references post (post_id)
);
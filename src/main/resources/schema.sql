CREATE TABLE IF NOT EXISTS category (
    category_id bigint not null
    , category integer not null
    , primary key (category_id)
)

CREATE TABLE IF NOT EXISTS hashtag (
    hashtag bigint not null
    , hashtag_name varchar(255) not null, primary key (hashtag))

-- CREATE TABLE IF NOT EXISTS hibernate_sequence (next_val bigint)
-- insert into hibernate_sequence values ( 1 )

CREATE TABLE IF NOT EXISTS member (
    member_id bigint not null, created_date datetime(6),
    modified_date datetime(6),
    account_name varchar(255) not null,
    email varchar(255) not null,
    introduce varchar(255),
    is_working varchar(255) not null,
    nickname varchar(255) not null,
    password varchar(255) not null, role integer not null,
    years integer not null,
    member_info_id bigint,
    primary key (member_id)
    )

CREATE TABLE IF NOT EXISTS member_category (
    member_category_id bigint not null,
    category_id bigint, member_id bigint,
    primary key (member_category_id)
    )

CREATE TABLE IF NOT EXISTS member_info (
    member_info_id bigint not null,
    email_verified varchar(255),
    image_src_l varchar(255) not null,
    image_src_m varchar(255) not null,
    image_src_s varchar(255) not null,
    receive_advertisement varchar(255),
    stack integer not null,
    token integer not null,
    primary key (member_info_id)
    )

CREATE TABLE IF NOT EXISTS order_history (
    order_history_id bigint not null,
    order_history_date datetime(6) not null,
    order_status varchar(255) not null,
    primary key (order_history_id)
    )

CREATE TABLE IF NOT EXISTS orders (
    order_id bigint not null,
    amount integer not null,
    billing_key varchar(255) not null,
    card_number bigint not null,
    created_date datetime(6) not null,
    expiration_month integer not null,
    expiration_year integer not null,
    order_status varchar(255) not null,
    order_token varchar(255) not null,
    tax_free_amount integer not null,
    member_id bigint,
    order_history_id bigint,
    primary key (order_id)
    )

CREATE TABLE IF NOT EXISTS post (
    post_id bigint not null,
    created_date datetime(6),
    modified_date datetime(6),
    contents varchar(255) not null,
    is_anonymous varchar(255) not null,
    title varchar(255) not null,
    views integer not null,
    member_id bigint,
    resume_id bigint,
    primary key (post_id)
    )

CREATE TABLE IF NOT EXISTS post_category (
    post_category_id bigint not null,
    category_id bigint, post_id bigint,
    primary key (post_category_id)
    )

CREATE TABLE IF NOT EXISTS post_comment (
    post_comment_id bigint not null,
    created_date datetime(6),
    modified_date datetime(6),
    contents varchar(255) not null,
    is_anonymous varchar(255) not null,
    post_comment_depth integer not null,
    post_comment_group integer not null,
    member_id bigint, post_id bigint,
    primary key (post_comment_id)
    )

CREATE TABLE IF NOT EXISTS post_comment_recommend (
    post_comment_recommend_id bigint not null,
    member_id bigint, post_comment_id bigint,
    primary key (post_comment_recommend_id)
    )

CREATE TABLE IF NOT EXISTS post_comment_report (
    post_comment_report_id bigint not null,
    member_id bigint, post_comment_id bigint,
    primary key (post_comment_report_id)
    )

CREATE TABLE IF NOT EXISTS resume (
    resume_id bigint not null,
    created_date datetime(6),
    modified_date datetime(6),
    contents varchar(255),
    file_link varchar(255) not null,
    title varchar(255) not null,
    views integer not null,
    years integer not null,
    member_id bigint,
    primary key (resume_id)
    )

CREATE TABLE IF NOT EXISTS resume_category (
    resume_category_id bigint not null,
    category_id bigint,
    resume_id bigint,
    primary key (resume_category_id)
    )

CREATE TABLE IF NOT EXISTS resume_comment (
    resume_comment_id bigint not null,
    created_date datetime(6),
    modified_date datetime(6),
    contents varchar(255) not null,
    is_anonymous varchar(255) not null,
    resume_comment_depth integer not null,
    resume_comment_group integer not null,
    member_id bigint, resume_id bigint,
    primary key (resume_comment_id)
    )

CREATE TABLE IF NOT EXISTS resume_comment_recommend (
    resume_comment_recommend_id bigint not null,
     member_id bigint,
     resume_comment_id bigint,
     primary key (resume_comment_recommend_id)
     )

CREATE TABLE IF NOT EXISTS resume_comment_report (
    resume_comment_report_id bigint not null,
    member_id bigint,
    resume_comment_id bigint,
    primary key (resume_comment_report_id)
    )

CREATE TABLE IF NOT EXISTS resume_hashtag (
    resume_hashtag_id bigint not null,
    hashtag_id bigint, resume_id bigint,
    primary key (resume_hashtag_id)
    )

CREATE TABLE IF NOT EXISTS resume_recommend (
    resume_recommend_id bigint not null,
     member_id bigint, resume_id bigint,
     primary key (resume_recommend_id)
     )

CREATE TABLE IF NOT EXISTS resume_scrap (
    resume_scrap_id bigint not null,
    created_date datetime(6) not null,
    member_id bigint,
    resume_id bigint,
    primary key (resume_scrap_id)
    )

CREATE TABLE IF NOT EXISTS token_history (
    token_history_id bigint not null,
    count bigint not null,
    created_date datetime(6) not null,
    reason varchar(255) not null,
    member_id bigint,
    primary key (token_history_id)
    )

-- alter table category drop index UK_category
-- alter table category add constraint UK_category unique (category)
--     alter table hashtag drop index UK_hashtag_name
-- alter table hashtag add constraint UK_hashtag_name unique (hashtag_name)
--     alter table member drop index UK_account_name
-- alter table member add constraint UK_account_name unique (account_name)
--     alter table member drop index UK_nickname
-- alter table member add constraint UK_nickname unique (nickname)
--     alter table member drop index UK_email
-- alter table member add constraint UK_email unique (email)
--     alter table resume drop index UK_file_link
-- alter table resume add constraint UK_file_link unique (file_link)
--     alter table member add constraint FK_member_info_member foreign key (member_info_id) references member_info (member_info_id)
--     alter table member_category add constraint FK_category_member_category foreign key (category_id) references category (category_id)
--     alter table member_category add constraint FK_member_member_category foreign key (member_id) references member (member_id)
--     alter table orders add constraint FK_member_order foreign key (member_id) references member (member_id)
--     alter table orders add constraint FK_order_history_order foreign key (order_history_id) references order_history (order_history_id)
--     alter table post add constraint FK_member_post foreign key (member_id) references member (member_id)
--     alter table post add constraint FK_resume_post foreign key (resume_id) references resume (resume_id)
--     alter table post_category add constraint FK_category_post_category foreign key (category_id) references category (category_id)
--     alter table post_category add constraint FK_post_post_category foreign key (post_id) references post (post_id)
--     alter table post_comment add constraint FK_member_post_comment foreign key (member_id) references member (member_id)
--     alter table post_comment add constraint FK_post_post_comment foreign key (post_id) references post (post_id)
--     alter table post_comment_recommend add constraint FK_member_post_comment_recommend foreign key (member_id) references member (member_id)
--     alter table post_comment_recommend add constraint FK_post_comment_post_comment_recommend foreign key (post_comment_id) references post_comment (post_comment_id)
--     alter table post_comment_report add constraint FK_member_post_comment_report foreign key (member_id) references member (member_id)
--     alter table post_comment_report add constraint FK_post_comment_post_comment_report foreign key (post_comment_id) references post_comment (post_comment_id)
--     alter table resume add constraint FK_member_resume foreign key (member_id) references member (member_id)
--     alter table resume_category add constraint FK_category_resume_category foreign key (category_id) references category (category_id)
--     alter table resume_category add constraint FK_resume_resume_category foreign key (resume_id) references resume (resume_id)
--     alter table resume_comment add constraint FK_member_resume_comment foreign key (member_id) references member (member_id)
--     alter table resume_comment add constraint FK_resume_resume_comment foreign key (resume_id) references resume (resume_id)
--     alter table resume_comment_recommend add constraint FK_member_resume_comment_recommend foreign key (member_id) references member (member_id)
--     alter table resume_comment_recommend add constraint FK_resume_comment_resume_comment_recommend foreign key (resume_comment_id) references resume_comment (resume_comment_id)
--     alter table resume_comment_report add constraint FK_member_resume_comment_report foreign key (member_id) references member (member_id)
--     alter table resume_comment_report add constraint FK_resume_comment_resume_comment_report foreign key (resume_comment_id) references resume_comment (resume_comment_id)
--     alter table resume_hashtag add constraint FK_hashtag_resume_hashtag foreign key (hashtag_id) references hashtag (hashtag)
--     alter table resume_hashtag add constraint FK_resume_resume_hashtag foreign key (resume_id) references resume (resume_id)
--     alter table resume_recommend add constraint FK_member_resume_recommend foreign key (member_id) references member (member_id)
--     alter table resume_recommend add constraint FK_resume_resume_recommend foreign key (resume_id) references resume (resume_id)
--     alter table resume_scrap add constraint FK_member_resume_scrap foreign key (member_id) references member (member_id)
--     alter table resume_scrap add constraint FK_resume_resume_scrap foreign key (resume_id) references resume (resume_id)
--     alter table token_history add constraint FK_member_token_history foreign key (member_id) references member (member_id)
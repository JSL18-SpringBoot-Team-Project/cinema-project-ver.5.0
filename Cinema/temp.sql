create database movie charset utf8mb4 collate utf8mb4_unicode_ci;

use movie;

-- 회원 정보 테이블
create table users(
	id int auto_increment primary key,
    email varchar(100) unique not null,
    name varchar(50),
    phone varchar(15),
    birth date,
    password varchar(200),
    social_provider enum('GOOGLE', 'FACEBOOK', 'NONE') default 'NONE',
    social_id varchar(255) unique, -- 소셜 로그인 고유 ID를 받아서 중복 가입 방지함
    role enum('ROLE_USER', 'ROLE_ADMIN') default 'ROLE_USER',
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp
);

-- alter table users drop gps_consent;
-- alter table users add social_id varchar(255) unique;
-- alter table users modify name varchar(50) null;
-- alter table users modify password varchar(200) null;
-- alter table users modify column phone varchar(15) unique;
-- alter table users add column role enum('ROLE_USER', 'ROLE_ADMIN') default 'ROLE_USER';
commit;

-- alter table users modify social_provider enum('GOOGLE', 'FACEBOOK', 'NONE') DEFAULT 'NONE';

-- 마케팅 동의 테이블
create table mkt_consents (
	id int auto_increment primary key,
    user_id int not null,
    sms_consent tinyint default 0,
    email_consent tinyint default 0,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp,
    foreign key (user_id) references users(id) on delete cascade
);

-- 영화관 지역 테이블
create table theater_area (
	id int auto_increment primary key,
    area_name varchar(50) not null
);

-- 영화관 정보 테이블
create table theaters (
	id int auto_increment primary key,
    theater_name varchar(50) not null,
    area_id int not null,
    foreign key (area_id) references theater_area(id) on delete cascade
);

-- 회원 선호 영화관 테이블 (회원가입 양식에 따라 지워도 됨 ㅇㅇ)
create table users_theaters (
	id int auto_increment primary key,
    user_id int not null,
    theater_id int not null,
    foreign key (user_id) references users(id) on delete cascade,
    foreign key (theater_id) references theaters(id) on delete cascade
);

-- 영화 상영관 테이블
create table screens (
    id int auto_increment primary key,
    screen_name VARCHAR(50) NOT NULL,
    screen_type ENUM('2D', '3D', '4DX', 'IMAX') NOT NULL
);

-- 상영 스케줄 테이블
CREATE TABLE schedules (
    id INT AUTO_INCREMENT PRIMARY KEY,
    screen_id INT NOT NULL,
    movie_id INT NOT NULL,
    watch_date DATE NOT NULL,
    start_time TIME NOT NULL, -- 영화 시작 시간
    end_time TIME NOT NULL,   -- 영화 종료 시간
    FOREIGN KEY (screen_id) REFERENCES screens(id) ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
);

-- 좌석 정보 테이블
CREATE TABLE seats (
    id INT AUTO_INCREMENT PRIMARY KEY,
    screen_id INT NOT NULL,
    schedule_id int not null,
    seat_column CHAR(1) NOT NULL, -- 좌석의 줄 (A, B, C, D)
    seat_row INT NOT NULL,  -- 좌석의 번호 (1~23)
    seat_price INT NOT NULL,
    state tinyint default 0, -- 0일때 예약x, 1일때 예약o, 2일때 예약중
    FOREIGN KEY (screen_id) REFERENCES screens(id) ON DELETE CASCADE,
    FOREIGN KEY (schedule_id) REFERENCES schedules(id) ON DELETE CASCADE
);

-- 영화 정보 테이블
create table movies (
    id int auto_increment primary key,
    title varchar(50) not null,
    running_time int not null,
    release_date date not null,
    end_date date not null,
    audience int,
    country varchar(20),
    production varchar(50),
    distribution varchar(100),
    genre varchar(50),
    director varchar(50),
    actor varchar(200),
    book_rate decimal(3,1),
    poster_img varchar(200),
    content varchar(3000)
);

-- alter table movies drop eng_title;

-- 영화 이미지 및 트레일러 테이블
-- create table movie_pictures (
--     id int auto_increment primary key,
--     movie_id int not null,
--     trailer varchar(3000),
--     movie_img varchar(3000),
--     foreign key (movie_id) references movies(id) on delete cascade
-- );

-- drop table movie_pictures;

-- 영화 디테일 테이블
create table movie_details (
    id int auto_increment primary key,
    movie_id int not null,
    trailer varchar(3000),
    movie_img varchar(3000),
    foreign key (movie_id) references movies(id) on delete cascade
);

-- 예약 정보 테이블
create table bookings (
    id int auto_increment primary key,
    user_id int not null,
    schedule_id int not null,
    timestamp timestamp default current_timestamp,
    foreign key (user_id) references users(id) on delete cascade,
    foreign key (schedule_id) references schedules(id) on delete cascade
);

-- 티켓 정보 테이블
create table tickets (
    id int auto_increment primary key,
    price int not null,
    ticket_status enum('RESERVED', 'CANCELED', 'USED') default 'RESERVED', -- 0 예약됨, 1 취소됨, 2 사용됨
    seats_id int not null,
    booking_id int not null,
    ticket_type int,
    foreign key (seats_id) references seats(id) on delete cascade,
    foreign key (booking_id) references bookings(id) on delete cascade
);

-- 쿠폰 테이블
create table coupons (
   id int auto_increment primary key,
    coupon_title varchar(150) not null,
    coupon_type enum('P', 'Y') not null,
    coupon_price int not null
);

-- 이벤트 테이블
create table events (
   id int auto_increment primary key,
    event_title varchar(200) not null,
    event_content text not null,
   event_img varchar(3000),
    event_start_date date not null,
    event_end_date date not null
);

-- 회원 별 쿠폰 현황 (마이페이지 쿠폰함 등등)
create table memberCoupon(
   id int auto_increment primary key,
    event_title varchar(200) not null,
    event_content text not null,
   event_img varchar(3000),
    event_start_date date not null,
    event_end_date date not null,
    coupon_id int not null,
   coupon_price int not null,
    user_id int not null,
    foreign key (coupon_id) references coupons(id) on delete cascade,
   foreign key (user_id) references users(id) on delete cascade
);

-- 문의 테이블
CREATE TABLE inquiries (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    email varchar(100) not null,
    inquiry_type ENUM(
        'RESERVATION',
        'PAYMENT',
        'ACCOUNT',
        'MOVIE_INFO',
        'SYSTEM_ERROR',
        'COUPONS_EVENTS',
        'OTHERS'
    ) NOT NULL,
    content TEXT NOT NULL,
    status ENUM('PENDING', 'ANSWERED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 문의 답변 테이블
CREATE TABLE inquiry_answers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    inquiry_id INT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (inquiry_id) REFERENCES inquiries(id) ON DELETE CASCADE
);

-- alter table inquiry_answers drop foreign key inquiry_answers_ibfk_2;
-- alter table inquiry_answers drop admin_id;

commit;

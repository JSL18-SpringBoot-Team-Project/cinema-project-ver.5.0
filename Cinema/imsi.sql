use movie;

-- 영화 정보 테이블
create table movies (
    id int auto_increment primary key,
    title varchar(50) not null,
    eng_title varchar(50),
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
   event_img varchar(3000) not null,
    event_start_date date not null,
    event_end_date date not null,
    coupon_id int not null,
    foreign key (coupon_id) references coupons(id)
);

-- 상영관 테이블
CREATE TABLE screens (
    id INT AUTO_INCREMENT PRIMARY KEY,
    screen_name VARCHAR(50) NOT NULL,
    screen_type ENUM('2D', '3D', '4DX', 'IMAX') NOT NULL
);
insert into screens (screen_name, screen_type) values ('2D', '2D');
insert into screens (screen_name, screen_type) values ('3D', '3D');
insert into screens (screen_name, screen_type) values ('4DX', '4DX');
insert into screens (screen_name, screen_type) values ('IMAX', 'IMAX');

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
    FOREIGN KEY (screen_id) REFERENCES screens(id) ON DELETE CASCADE,
    FOREIGN KEY (schedule_id) REFERENCES schedules(id) ON DELETE CASCADE
);
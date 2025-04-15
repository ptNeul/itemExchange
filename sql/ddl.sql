create table user (
  username varchar(50) not null primary key,
  password varchar(255) not null,
  email varchar(100) not null unique,
  nickname varchar(50) not null unique,
  role tinyint not null default 0 -- BUYER(0), SELLER(1), ADMIN(2)
);
alter table user add column balance bigint not null default 0;

create table seller (
  seller_id varchar(50) not null primary key,
  account_number varchar(20) not null,
  business_number varchar(20) not null,
  foreign key (seller_id) references user(username) on delete cascade,
);

create table item_metadata (
  item_id bigint not null auto_increment primary key,
  item_name varchar(50) not null unique,
  image varchar(255) not null unique,
  detail varchar(255) not null
);

create table item_listing (
  listing_id bigint not null auto_increment primary key,
  seller_id varchar(50) not null,
  item_id bigint not null,
  price int not null,
  status tinyint not null default 0, -- SALE(0), COMPLETED(1)
  foreign key (seller_id) references user(username) on delete cascade,
	foreign key (item_id) references item_metadata(item_id) on delete cascade
);

create table item_trade_history (
  history_id bigint not null auto_increment primary key,
  created_at timestamp not null default current_timestamp,
  listing_id bigint not null,
  buyer_id varchar(50) not null,
  foreign key (listing_id) references item_listing(listing_id),
  foreign key (buyer_id) references user(username)
);


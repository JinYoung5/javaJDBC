create table sitem(
	item_num number primary key,
	item_name varchar2(30) not null,
	item_price number(9) not null,
	item_date date default sysdate not null
);
create sequence sitem_seq;

create table customer(
	cust_id varchar2(30) primary key,
	cust_name varchar2(30) not null,
	cust_address varchar2(90) not null,
	cust_tel varchar2(20) not null,
	cust_date date default sysdate not null
);

create table sorder(
	order_num number primary key,
	cust_id varchar2(30) references customer (cust_id),
	item_num number references sitem (item_num),
	order_date date default sysdate not null
);
create sequence sorder_seq;
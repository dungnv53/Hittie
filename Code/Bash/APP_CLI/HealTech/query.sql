select *, count(*) from instant_message_status where read_flg != 1 group by member_id;

select id, phone, first_name , last_name , email , push_noti_token , password  from users;

select id, room_id, file_name, updated_at from instant_message where file_name != '';

select id, room_id, file_name, updated_at from instant_message where file_name != '' and updated_at >= '2017-07-05';

select id, first_name , email, p_photo from users where p_photo != '';

select *, count(*) from instant_message_status where read_flg != 1 group by member_id;

select id, phone, first_name , last_name , email , push_noti_token , password  from users;

select id, phone, first_name , last_name, registration_id , password  from users;

select id, room_id, file_name, updated_at from instant_message where file_name != '';

select id, room_id, file_name, updated_at from instant_message where file_name != '' and updated_at >= '2017-08-11';

select id, first_name , email, p_photo from users where p_photo != '';
select id, first_name , email, p_photo, updated_at from users where p_photo != '' and updated_at >= '2017-08-11' or updated_at IS NULL AND p_photo IS NOT NULL; // = NULL


select id, substr(room_name, 1, 10) , substr(room_description , 1, 10)  from room;
select * from room_member where room_id = 331;

insert into room_member values(1029, 331, 1, NOW(), NOW(), 111, NULL, NULL, NULL, NULL, NULL);

// Dupli room member
select id, room_id, member_id, updated_at , count(member_id) as dupli from room_member group by room_id, member_id having dupli >= 2;

aptible config:set --app live-txp-chat  "SSL_PROTOCOLS_OVERRIDE=TLSv1.1 TLSv1.2"

select id, phone, first_name , last_name , isVerified , password from users;
select id, phone, first_name , last_name , isVerified , organization , verified_org , password from users where isVerified  = 1 and phone like "%6060";

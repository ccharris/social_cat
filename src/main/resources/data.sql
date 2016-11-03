insert into lab302.users (first_name, last_name, password, email, phone_number, active, secret_question, secret_answer) values ('Zach', 'Smith', 'abc123', 'zsmith@socialcat.com', '1234567890', true, 'What is your first dogs name?', 'Fido');
insert into lab302.users (first_name, last_name, password, email, phone_number, active, secret_question, secret_answer) values ('Julie', 'Williams', 'def456', 'jwilliams@socialcat.com', '1234561234', true, 'What is your first dogs name?', 'Fido');
insert into lab302.users (first_name, last_name, password, email, phone_number, active, secret_question, secret_answer) values ('Sven', 'Bjergson', 'lol123', 'sbjergson@socialcat.com', '1234564321', true, 'What is your first dogs name?', 'Fido');
insert into lab302.users (first_name, last_name, password, email, phone_number, active, secret_question, secret_answer) values ('Allison', 'Johns', 'password123', 'ajohns@socialcat.com', '1234565566', true, 'What is your first dogs name?', 'Fido');
insert into lab302.users (first_name, last_name, password, email, phone_number, active, secret_question, secret_answer) values ('Michael', 'Pabst', 'pbr123', 'mpabst@socialcat.com', '1234561289', true, 'What is your first dogs name?', 'Fido');
insert into lab302.users (first_name, last_name, password, email, phone_number, active, secret_question, secret_answer) values ('David', 'Anderson', 'dave123', 'danderson@socialcat.com', '1234165346', true, 'What is your first dogs name?', 'Fido');
insert into lab302.users (first_name, last_name, password, email, phone_number, active, secret_question, secret_answer) values ('Dana', 'Carter', 'fox123', 'dcarter@socialcat.com', '1234162146', true, 'What is your first dogs name?', 'Fido');
insert into lab302.users (first_name, last_name, password, email, phone_number, active, secret_question, secret_answer) values ('Ashley', 'Weston', 'wes123', 'aweston@socialcat.com', '1234165379', true, 'What is your first dogs name?', 'Fido');
insert into lab302.users (first_name, last_name, password, email, phone_number, active, secret_question, secret_answer) values ('Jason', 'Fredrick', 'pass123', 'jfredrick@socialcat.com', '1234165346', true, 'What is your first dogs name?', 'Fido');
insert into lab302.users (first_name, last_name, password, email, phone_number, active, secret_question, secret_answer) values ('Evelynn', 'Rogers', 'eve123', 'erogers@socialcat.com', '1234166269', true, 'What is your first dogs name?', 'Fido');

insert into lab302.user_roles (user_id, role) values ((select id from lab302.users where email = 'zsmith@socialcat.com'), 'USER');
insert into lab302.user_roles (user_id, role) values ((select id from lab302.users where email = 'jwilliams@socialcat.com'), 'USER');
insert into lab302.user_roles (user_id, role) values ((select id from lab302.users where email = 'sbjergson@socialcat.com'), 'USER');
insert into lab302.user_roles (user_id, role) values ((select id from lab302.users where email = 'ajohns@socialcat.com'), 'USER');
insert into lab302.user_roles (user_id, role) values ((select id from lab302.users where email = 'mpabst@socialcat.com'), 'ADMIN');
insert into lab302.user_roles (user_id, role) values ((select id from lab302.users where email = 'danderson@socialcat.com'), 'USER');
insert into lab302.user_roles (user_id, role) values ((select id from lab302.users where email = 'dcarter@socialcat.com'), 'USER');
insert into lab302.user_roles (user_id, role) values ((select id from lab302.users where email = 'aweston@socialcat.com'), 'USER');
insert into lab302.user_roles (user_id, role) values ((select id from lab302.users where email = 'jfredrick@socialcat.com'), 'USER');
insert into lab302.user_roles (user_id, role) values ((select id from lab302.users where email = 'erogers@socialcat.com'), 'ADMIN');

insert into lab302.contacts (first_name, last_name, note, email, phone_number, active, user_id) values ('Summer', 'Blakes', 'Met at ABC Convention', 'SummerSBlakes@socialcat.com', '9734838030', true, (select id from lab302.users where email = 'jfredrick@socialcat.com'));
insert into lab302.contacts (first_name, last_name, note, email, phone_number, active, user_id) values ('Herbert', 'Malone', 'Excellent Trombone Player', 'HerbertMMalone@socialcat.com', '2625328237', true, (select id from lab302.users where email = 'danderson@socialcat.com'));
insert into lab302.contacts (first_name, last_name, note, email, phone_number, active, user_id) values ('John', 'Hathaway', 'Knows really good SQL', 'JohnRHathaway@socialcat.com', '7249661769', true, (select id from lab302.users where email = 'danderson@socialcat.com'));
insert into lab302.contacts (first_name, last_name, note, email, phone_number, active, user_id) values ('Brandon', 'Young', 'Birthday Party on the 16th', 'BrandonHYoung@socialcat.com', '8142635623', true, (select id from lab302.users where email = 'ajohns@socialcat.com'));
insert into lab302.contacts (first_name, last_name, note, email, phone_number, active, user_id) values ('James', 'Harmon', 'Dates Samantha', 'JamesSHarmon@socialcat.com', '9713273709', true, (select id from lab302.users where email = 'dcarter@socialcat.com'));
insert into lab302.contacts (first_name, last_name, note, email, phone_number, active, user_id) values ('Patrice', 'Gress', 'Knows French', 'PatriceAGress@socialcat.us', '6105661496', true, (select id from lab302.users where email = 'sbjergson@socialcat.com'));
insert into lab302.contacts (first_name, last_name, note, email, phone_number, active, user_id) values ('Charles', 'Redd', 'From Germany', 'CharlesERedd@socialcat.com', '8043816085', true, (select id from lab302.users where email = 'jwilliams@socialcat.com'));
insert into lab302.contacts (first_name, last_name, note, email, phone_number, active, user_id) values ('Ashley', 'Weston', 'Good at crocheting', 'aweston@socialcat.com', '1234165379', true, (select id from lab302.users where email = 'jwilliams@socialcat.com'));
insert into lab302.contacts (first_name, last_name, note, email, phone_number, active, user_id) values ('Jason', 'Fredrick', 'Met in San Francisco', 'jfredrick@socialcat.com', '1234165346', true, (select id from lab302.users where email = 'zsmith@socialcat.com'));
insert into lab302.contacts (first_name, last_name, note, email, phone_number, active, user_id) values ('Evelynn', 'Rogers', 'Excellent Fencer', 'erogers@socialcat.com', '1234166269', true, (select id from lab302.users where email = 'zsmith@socialcat.com'));